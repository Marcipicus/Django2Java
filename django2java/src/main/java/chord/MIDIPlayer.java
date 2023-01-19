package chord;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import chord.exceptions.ChordToneBuildingException;
import chord.exceptions.GenericMIDIException;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;
import chord.maps.ChordLibrary;
import chord.maps.MIDINoteLibrary;
import chord.maps.ScaleLibrary;
import chord.relations.record.ChordChangeConsonanceRecord;
import chord.relations.record.NoteConsonanceRecord;
import chord.relations.record.ScaleConsonanceRecord;

public class MIDIPlayer {

	private static final Logger logger = LogManager.getLogger();

	private static final int DEFAULT_CHORD_REGISTER = 4;
	private static final int DEFAULT_MELODY_REGISTER = DEFAULT_CHORD_REGISTER + 1;

	private static final int DEFAULT_TEMPO_IN_BPM = 160;
	private static final NoteName DEFAULT_REFERENCE_NOTE_FOR_SEQUENCES = NoteName.C;
	//constant used for timing resolution of sequences
	private static final int FOUR_PPQ = 4;

	private static MIDIPlayer instance;

	public static MIDIPlayer getInstance() throws GenericMIDIException {
		if(instance == null) {
			instance = new MIDIPlayer();
		}
		return instance;
	}

	private final ChordLibrary cLib;
	private final ScaleLibrary sLib;
	private final Sequencer sequencer;

	/**
	 * Create a basic MIDIPlayer that only produces sequences
	 * for ConsonanceRecords.
	 * @throws MidiUnavailableException
	 */
	private MIDIPlayer() throws GenericMIDIException {
		cLib = ChordLibrary.getInstance();
		sLib = ScaleLibrary.getInstance();

		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.setTempoInBPM(DEFAULT_TEMPO_IN_BPM);
		}catch(MidiUnavailableException e) {
			throw new GenericMIDIException("MIDI is not available on this system.",e);
		}
	}

	public void dispose() {
		if(sequencer.isOpen()) {
			sequencer.close();
		}
	}
	
	private void playSequence(Sequence sequence) throws GenericMIDIException {
		if(sequence == null) {
			throw new NullPointerException("sequence may not be null");
		}
		
		if(sequencer.isRunning()) {
			sequencer.stop();
		}

		try {
			sequencer.setSequence(sequence);
		}catch(InvalidMidiDataException e) {
			throw new GenericMIDIException("Error setting sequence", e);
		}
		sequencer.start();
	}

	public void playChordChangeConsonanceRecord(ChordChangeConsonanceRecord cccRecord) throws GenericMIDIException {
		if(cccRecord == null) {
			throw new NullPointerException("cccRecord may not be null");
		}
		
		Sequence chordChangeSequence = createSequenceForChordChangeConsonanceRecord(cccRecord);
		
		playSequence(chordChangeSequence);
	}
	
	public void playScaleConsonanceRecord(ScaleConsonanceRecord scRecord) throws GenericMIDIException{
		if(scRecord == null) {
			throw new NullPointerException("scRecord may not be null");
		}
		
		Sequence scaleChordSequence = createSequenceForScaleConsonanceRecord(scRecord);
		
		playSequence(scaleChordSequence);
	}

	/**
	 * Create a sequence for the given record.
	 * @param cccRecord
	 * @return
	 * @throws GenericMIDIException if there is any problem accessing midi
	 * resources or generating MIDI sequences
	 */
	private Sequence createSequenceForChordChangeConsonanceRecord(ChordChangeConsonanceRecord cccRecord	) throws GenericMIDIException{
		if(cccRecord == null) {
			throw new NullPointerException("cccRecord may not be null");
		}

		int channelNumber = 0;
		int velocity = 64;
		int pulseOffset = 0;
		int pulses = 8;

		Sequence ccSequence;
		try {
			ccSequence = new Sequence(Sequence.PPQ, FOUR_PPQ);
		} catch (InvalidMidiDataException e) {
			throw new GenericMIDIException("Invalid division type.", e);
		}

		Track chordChangeTrack = ccSequence.createTrack();

		final NoteName startChordRoot,endChordRoot;
		final Chord startChord,endChord;

		startChordRoot = MIDIPlayer.DEFAULT_REFERENCE_NOTE_FOR_SEQUENCES;
		startChord = cLib.getChord(startChordRoot, cccRecord.startChordSignature());
		pulseOffset = addChordToTrack(startChord,chordChangeTrack,channelNumber,velocity,pulseOffset,pulses);

		endChordRoot = startChordRoot.getNoteByInterval(cccRecord.intervalBetweenRoots());
		endChord = cLib.getChord(endChordRoot, cccRecord.endChordSignature());
		addChordToTrack(endChord,chordChangeTrack,channelNumber,velocity,pulseOffset,pulses);

		return ccSequence;

	}
	
	private Sequence createSequenceForScaleConsonanceRecord(ScaleConsonanceRecord scRecord) throws GenericMIDIException {
		if(scRecord == null) {
			throw new NullPointerException("scRecord may not be null.");
		}
		
		int chordChannelNumber = 0;
		int scaleChannelNumber = 0;
		int velocity = 64;
		int scaleVelocity = 80;
		int chordPulseOffset = 0;
		int scalePulseOffset = 2;
		int pulsesForChord = 8;
		int pulsesPerNote = 2;
		
		Sequence scSequence;
		try {
			scSequence = new Sequence(Sequence.PPQ, FOUR_PPQ);
		} catch (InvalidMidiDataException e) {
			throw new GenericMIDIException("division type is invalid",e);
		}
		Track chordTrack,melodyTrack;
		
		Chord chord = cLib.getChord(DEFAULT_REFERENCE_NOTE_FOR_SEQUENCES, scRecord.chordSignature());
		
		chordTrack = scSequence.createTrack();
		chordPulseOffset = addChordToTrack(chord,chordTrack,chordChannelNumber,velocity,chordPulseOffset,pulsesForChord);
		chordPulseOffset = addChordToTrack(chord,chordTrack,chordChannelNumber,velocity,chordPulseOffset,pulsesForChord);
		
		
		Scale scale = sLib.getScale(DEFAULT_REFERENCE_NOTE_FOR_SEQUENCES, scRecord.scaleSignature());
		melodyTrack = scSequence.createTrack();
		addScaleToTrack(scale,melodyTrack,scaleChannelNumber,scaleVelocity,scalePulseOffset,pulsesPerNote);
		
		return scSequence;
	}

	/**
	 * Add the given chord to the track in the given channel.
	 * The chord will start at pulseOffset and end at (pulseOffset+pulses)
	 * @param chord chord to be added to the track
	 * @param track track to which the chord will be added 
	 * @param channelNumber channel of the track that the chord will be added to
	 * @param velocity velocity of MIDI NoteOn Message(volume)
	 * @param pulseOffset point in the track at which the chord will start
	 * @param pulses length of time the chord will sound for
	 * @return pulseOffset of the end of the chord
	 * @throws GenericMIDIException if there is any MIDI related exception while
	 * attempting to add the chord to the track.
	 */
	private int addChordToTrack(
			final Chord chord, 
			final Track track,
			final int channelNumber, 
			final int velocity, 
			int pulseOffset,
			final int pulses ) throws GenericMIDIException {
		if(chord == null || track == null) {
			throw new NullPointerException("neither chord nor rhythm track may be null");
		}

		try {
			final byte[] startChordBytes;

			startChordBytes = chord.getTonesInBytes(DEFAULT_CHORD_REGISTER);
			//create all the note on messages for the start chord
			for(byte midiNoteByte : startChordBytes) {
				ShortMessage noteOnMessage;

				noteOnMessage = new ShortMessage(ShortMessage.NOTE_ON,channelNumber,midiNoteByte,velocity);
				track.add(new MidiEvent(noteOnMessage,pulseOffset));
			}
			
			pulseOffset += pulses;

			//create all the note off messages for the start chord
			for(byte midiNoteByte : startChordBytes) {
				ShortMessage noteOffMessage;

				noteOffMessage = new ShortMessage(ShortMessage.NOTE_OFF,channelNumber,midiNoteByte,velocity);
				track.add(new MidiEvent(noteOffMessage,pulseOffset));
			}
			
			return pulseOffset;
		}catch(ChordToneBuildingException e) {
			//TODO:this might be a throw back to old code
			//figure out later if it is needed,
			throw new GenericMIDIException("Error building chord tones", e);
		}catch(InvalidMidiDataException e) {
			throw new GenericMIDIException("The division type used for the sequence is invalid." , e);
		}catch(InvalidMIDIValueException e) {
			throw new GenericMIDIException("A generated MIDIEvent is not valid",e);
		}catch(InvalidNoteRegisterException e) {
			throw new GenericMIDIException("The register used to generate chord tones is too high.",e);
		}
	}

	/**
	 * Add all of the notes in the scale in sequence to the given track and return
	 * the pulse offset of the last noteOff Message(end of scale).
	 * @param scale scale to add to track
	 * @param track track to add the scale to
	 * @param channelNumber channel of the track to add the scale to
	 * @param velocity volume
	 * @param pulseOffset initial pulse offset that defines the start of the scale 
	 * @param pulsesBetweenNotes number of pulses between each note in the scale.
	 * @return a pulse offset defining the end of the last scale note
	 * @throws GenericMIDIException if there is any exception adding notes to
	 * the track
	 */
	private int addScaleToTrack(
			final Scale scale, 
			final Track track,
			final int channelNumber, 
			final int velocity, 
			int pulseOffset,
			final int pulsesBetweenNotes ) throws GenericMIDIException {
		if(scale == null || track == null) {
			throw new NullPointerException("Neither scale nor track may be null");
		}

		final byte[] scaleBytes;

		try {
			scaleBytes = scale.getTonesInBytes(DEFAULT_MELODY_REGISTER);

			for(byte midiScaleByte : scaleBytes) {
				ShortMessage noteOnMessage;

				noteOnMessage = new ShortMessage(ShortMessage.NOTE_ON,channelNumber,midiScaleByte,velocity);
				track.add(new MidiEvent(noteOnMessage,pulseOffset));

				pulseOffset += pulsesBetweenNotes;

				ShortMessage noteOffMessage;

				noteOffMessage = new ShortMessage(ShortMessage.NOTE_OFF,channelNumber,midiScaleByte,velocity);
				track.add(new MidiEvent(noteOffMessage,pulseOffset));
			}
			
			return pulseOffset;
		}catch(ChordToneBuildingException e) {
			//TODO:this might be a throw back to old code
			//figure out later if it is needed,
			throw new GenericMIDIException("Error building scale tones", e);
		}catch(InvalidMidiDataException e) {
			throw new GenericMIDIException("The division type used for the sequence is invalid." , e);
		}catch(InvalidMIDIValueException e) {
			throw new GenericMIDIException("A generated MIDIEvent is not valid",e);
		}catch(InvalidNoteRegisterException e) {
			throw new GenericMIDIException("The register used to generate scale tones is too high.",e);
		}
	}
	
	private Sequence createSequenceForNoteConsonanceRecord(NoteConsonanceRecord ncRecord) {
		return null;
	}

	private Track createTrackForChordChange(ChordSignature startSig, ChordSignature endSig, Interval intervalBetweenRoots) {
		return null;
	}

	private Track createTrackForChord(ChordSignature chordSig, int numBars) {
		return null;
	}

	private Track createTrackForScale(ScaleSignature scaleSig,int numBars) {
		return null;
	}

	private Track createTrackForNote(NoteName note, int numBars) {
		return null;
	}

}
