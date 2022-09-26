package chord;

import chord.exceptions.InvalidMIDIValueException;
import chord.maps.MIDINoteLibrary;

/**
 * This class models a note with a given note name and register and represents it
 * to a MIDI value.
 * 
 * Valid MIDI values are 21-127.
 * 
 * This class is not meant to be instantiated itself.
 * 
 * The MIDINoteLibrary class should be used to obtain the note you want.
 * @author DAD
 *
 */
public class MIDINote implements Comparable<MIDINote>{
	public static final String MIDI_NUMBER_INITIALIZATION_ERROR = "The given MIDI number is invalid";
	
	public static final String INVALID_REGISTER_VALUE = "The given register must be between 0 and 9.";
	
	public static final String INVALID_MIDI_NUMBER_PASSED_THROUGH_CONSTRUCTOR = "The MIDI number was not constructed properly and the constructor validation needs to be checked.";

	private final byte midiNoteNumber;
	
	public static final void checkMIDINumberValidity(int midiNumber) throws InvalidMIDIValueException {
		if(midiNumber<21 || midiNumber>127) {
			throw new InvalidMIDIValueException(MIDI_NUMBER_INITIALIZATION_ERROR, midiNumber);
		}
	}

	
	public MIDINote(int midiNumber) throws InvalidMIDIValueException {
		MIDINote.checkMIDINumberValidity(midiNumber);
		this.midiNoteNumber = (byte)midiNumber;	
	}

	public NoteName getNoteName() {
		return NoteName.values()[(midiNoteNumber-21)%12];
	}

	public byte getNoteRegister() {
		//note has already been checked for validity so no need to check boundaries.
		
		//I know this is a shitty method of finding the register but it's the
		//simplest solution I could think of
		if(midiNoteNumber>=21 && midiNoteNumber<=23) {
			return 0;
		} else if(midiNoteNumber>=24 && midiNoteNumber<=35) {
			return 1;
		}else if(midiNoteNumber>=36 && midiNoteNumber<=47) {
			return 2;
		}else if(midiNoteNumber>=48 && midiNoteNumber<=59) {
			return 3;
		}else if(midiNoteNumber>=60 && midiNoteNumber<=71) {
			return 4;
		}else if(midiNoteNumber>=72 && midiNoteNumber<=83) {
			return 5;
		}else if(midiNoteNumber>=84 && midiNoteNumber<=95) {
			return 6;
		}else if(midiNoteNumber>=96 && midiNoteNumber<=107) {
			return 7;
		}else if(midiNoteNumber>=108 && midiNoteNumber<=119) {
			return 8;
		}else if(midiNoteNumber>=120 && midiNoteNumber<=127) {
			return 9;
		}
		
		throw new IllegalStateException(INVALID_MIDI_NUMBER_PASSED_THROUGH_CONSTRUCTOR);
	}

	public byte getMidiNoteNumber() {
		return midiNoteNumber;
	}
	
	/**
	 * Retrieve the desired MIDINote at the desired interval from this note.
	 * @param i the interval between this note and the desired note
	 * @return the MIDINote the desired interval up from this note.
	 * @throws InvalidMIDIValueException if the note requested from the given interval exceeds
	 * the minimum or maximum midi values (21-127)
	 */
	public MIDINote getRelatedNote(Interval i) throws InvalidMIDIValueException {
		return MIDINoteLibrary.getInstance().getNote(this.getMidiNoteNumber() + i.ordinal());
	}


	@Override
	public int compareTo(MIDINote o) {
		return this.getMidiNoteNumber() - o.getMidiNoteNumber();
	}

}
