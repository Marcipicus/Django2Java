package chord;

import java.util.List;

import chord.exceptions.ChordToneBuildingException;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;

/**
 * Interface to represent a musical entity that consists
 * of a number of tones.
 * @author DAD
 *
 */
public interface ToneCollection {
	
	/**
	 * Get a list of the notes for the chord in MIDI form.
	 * @param register
	 * @return
	 * @throws InvalidMIDIValueException
	 * @throws InvalidNoteRegisterException
	 * @throws ChordToneBuildingException
	 */
	List<MIDINote> getTones(int register) 
			throws InvalidMIDIValueException, 
			InvalidNoteRegisterException, 
			ChordToneBuildingException;
	
	/**
	 * Get an array of bytes representing midi notes that will
	 * be consumed by the MIDIPlayer
	 * @param register register which will be used to create the notes
	 * (A register is basically a numbered octave) 
	 * @return array of bytes that can be consumed by the MIDIPlayer
	 * @throws InvalidMIDIValueException
	 * @throws InvalidNoteRegisterException
	 * @throws ChordToneBuildingException
	 */
	byte[] getTonesInBytes(int register) 
			throws InvalidMIDIValueException, 
			InvalidNoteRegisterException, 
			ChordToneBuildingException;
	
}
