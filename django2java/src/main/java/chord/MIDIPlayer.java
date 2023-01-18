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
import chord.relations.record.ChordChangeConsonanceRecord;
import chord.relations.record.NoteConsonanceRecord;
import chord.relations.record.ScaleConsonanceRecord;

public class MIDIPlayer {

	private static final Logger logger = LogManager.getLogger();

	private static final int DEFAULT_REGISTER = 4;
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

	private final MIDINoteLibrary mLib;
	private final ChordLibrary cLib;
	private final Sequencer sequencer;

	/**
	 * Create a basic MIDIPlayer that only produces sequences
	 * for ConsonanceRecords.
	 * @throws MidiUnavailableException
	 */
	private MIDIPlayer() throws GenericMIDIException {
		mLib = MIDINoteLibrary.getInstance();
		cLib = ChordLibrary.getInstance();

		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
		}catch(MidiUnavailableException e) {
			throw new GenericMIDIException("MIDI is not available on this system.",e);

		}
	}

	public void playChordChangeConsonanceRecord(ChordChangeConsonanceRecord cccRecord) throws GenericMIDIException {
		if(cccRecord == null) {
			throw new NullPointerException("cccRecord may not be null");
		}
		if(sequencer.isRunning()) {
			sequencer.stop();
		}

		try {
			sequencer.setSequence(createSequenceForChordChangeConsonanceRecord(cccRecord));
		}catch(InvalidMidiDataException e) {
			throw new GenericMIDIException("Error setting sequence", e);
		}
		sequencer.start();
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

		try {
			int channelNumber = 0;
			int velocity = 64;
			
			Sequence ccSequence = new Sequence(Sequence.PPQ, FOUR_PPQ);

			Track chordChangeTrack = ccSequence.createTrack();

			final NoteName startChordRoot,endChordRoot;
			final Chord startChord,endChord;
			final byte[] startChordBytes,endChordBytes;

			startChordRoot = MIDIPlayer.DEFAULT_REFERENCE_NOTE_FOR_SEQUENCES;
			startChord = cLib.getChord(startChordRoot, cccRecord.startChordSignature());

			endChordRoot = startChordRoot.getNoteByInterval(cccRecord.intervalBetweenRoots());
			endChord = cLib.getChord(endChordRoot, cccRecord.endChordSignature());

			startChordBytes = startChord.getChordTonesInBytes(DEFAULT_REGISTER);
			endChordBytes = endChord.getChordTonesInBytes(DEFAULT_REGISTER);

			//create all the note on messages for the start chord
			for(byte midiNoteByte : startChordBytes) {
				ShortMessage noteOnMessage;

				noteOnMessage = new ShortMessage(ShortMessage.NOTE_ON,channelNumber,midiNoteByte,velocity);
				chordChangeTrack.add(new MidiEvent(noteOnMessage, 0));
			}

			//create all the note off messages for the start chord
			for(byte midiNoteByte : startChordBytes) {
				ShortMessage noteOffMessage;

				noteOffMessage = new ShortMessage(ShortMessage.NOTE_OFF,channelNumber,midiNoteByte,velocity);
				chordChangeTrack.add(new MidiEvent(noteOffMessage,8));
			}

			//create all the note on messages for the end chord
			for(byte midiNoteByte : endChordBytes) {
				ShortMessage noteOnMessage;

				noteOnMessage = new ShortMessage(ShortMessage.NOTE_ON,channelNumber,midiNoteByte,velocity);
				chordChangeTrack.add(new MidiEvent(noteOnMessage, 8));
			}

			//create all the note off messages for the end chord
			for(byte midiNoteByte : endChordBytes) {
				ShortMessage noteOffMessage;

				noteOffMessage = new ShortMessage(ShortMessage.NOTE_OFF,channelNumber,midiNoteByte,velocity);
				chordChangeTrack.add(new MidiEvent(noteOffMessage,16));
			}

			return ccSequence;
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

	private Sequence createSequenceForScaleConsonanceRecord(ScaleConsonanceRecord scRecord) {
		return null;
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
