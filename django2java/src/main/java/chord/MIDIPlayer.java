package chord;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

import chord.exceptions.ChordToneBuildingException;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;
import chord.maps.ChordLibrary;
import chord.maps.MIDINoteLibrary;

public class MIDIPlayer {
	
	private static final int DEFAULT_REGISTER = 4;
	private static MIDIPlayer instance;
	
	public static MIDIPlayer getInstance() throws MidiUnavailableException {
		if(instance == null) {
			instance = new MIDIPlayer();
		}
		return instance;
	}
	
	private final MIDINoteLibrary mLib;
	private final ChordLibrary cLib;
	
	private final Synthesizer synth;
	private final MidiChannel[] midiChannels;
	private final Instrument[] instruments;

	private MIDIPlayer() throws MidiUnavailableException {
		mLib = MIDINoteLibrary.getInstance();
		cLib = ChordLibrary.getInstance();
		
		synth = MidiSystem.getSynthesizer();
		synth.open();
		
		midiChannels = synth.getChannels();
		instruments = synth.getDefaultSoundbank().getInstruments();
		
		boolean instrumentsLoadedSuccessfully = synth.loadInstrument(instruments[0]);
	}
	
	public void playNote(MIDINote note) {
		midiChannels[0].noteOn(note.getMidiNoteNumber(), 30);
	}
	
	/**
	 * Play the note at the default Register.
	 * @param note
	 * @throws InvalidNoteRegisterException
	 */
	public void playNote(NoteName note) throws InvalidNoteRegisterException {
		playNote(mLib.getNote(note,DEFAULT_REGISTER));
	}
	
	/**
	 * Play the chord at the default register.
	 * 
	 * @param chordToPlay
	 * @throws InvalidMIDIValueException
	 * @throws InvalidNoteRegisterException
	 * @throws ChordToneBuildingException
	 */
	public void playChord(Chord chordToPlay) throws InvalidMIDIValueException, InvalidNoteRegisterException, ChordToneBuildingException {
		for(MIDINote mNote : chordToPlay.getChordTones(DEFAULT_REGISTER)) {
			midiChannels[0].noteOn(mNote.getMidiNoteNumber(),30);
		}
	}
}
