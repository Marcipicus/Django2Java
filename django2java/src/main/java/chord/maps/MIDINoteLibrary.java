package chord.maps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import chord.MIDINote;
import chord.NoteName;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;

/**
 * The purpose of this class is to create one instance of every MIDINote
 * so that the program is easy on memory when creating multiple chord change
 * maps. 
 * @author DAD
 *
 */
public class MIDINoteLibrary {

	public static final String MIDI_NUMBER_TOO_LOW = "The given MIDI number must be at least 21";
	public static final String MIDI_NUMBER_TOO_HIGH = "The given MIDI number must be 127 or below";

	public static final String NOTE_UNSUPPORTED_IN_REGISTER_0 = "The given note is not supported by midi in register 0.";
	public static final String NOTE_UNSUPPORTED_IN_REGISTER_9 = "The given note is not supported by midi in register 9.";

	public static final String NOTE_NAME_MAY_NOT_BE_NULL = "The given note name may not be null.";
	public static final String INVALID_REGISTER_VALUE = "The given register must be between 0 and 9.";
	public static final String INVALID_NOTE_NAME_GIVEN = "The given note name is note supported";

	private static final String UNHANDLED_CASE_STATEMENT = "There is an unhandled case statement";
	
	private static final Logger logger = LogManager.getLogger();

	public static final int minLowerRegister = 0;
	public static final int maxUpperRegister = 9;


	private static MIDINoteLibrary noteLibrary;

	public static MIDINoteLibrary getInstance() {
		if(noteLibrary==null) {
			logger.debug("Creating MIDI note Library");
			noteLibrary = new MIDINoteLibrary();
			logger.debug("MIDI note library created successfully");
		}

		return noteLibrary;
	}

	/**
	 * Direct mapping between the value of the note in a byte to the MIDINote class
	 * that allows for the conversion to noteNames and registers.
	 */
	private final MIDINote[] allMIDIValues; 

	private MIDINoteLibrary() {
		try {
			allMIDIValues = new MIDINote[128];

			for(int i = 21; i<=127 ; i++) {
				allMIDIValues[i] = new MIDINote((byte)i);
			}
		}
		//throwing a runtime exception so that calling functions do not
		//need to handle the error but it will be detected rather quickly if 
		//the library generation code is changed.
		catch(InvalidMIDIValueException e) {
			throw new IllegalArgumentException("Killing the program because invalid data sent to midi libary",e);
		}
	}

	/**
	 * Return the MIDINote for the given midi number
	 * @param midiNoteValue
	 * @return
	 * @throws InvalidMIDIValueException 
	 */
	public MIDINote getNote(int midiNoteValue) throws InvalidMIDIValueException {
		MIDINote.checkMIDINumberValidity(midiNoteValue);

		return allMIDIValues[midiNoteValue];
	}

	/**
	 * Return the MIDINote for the given note name and register.
	 * @param noteName
	 * @param registerValue
	 * @return
	 * @throws InvalidNoteRegisterException 
	 */
	public MIDINote getNote(NoteName noteName, int registerValue) throws InvalidNoteRegisterException {
		if(noteName == null) {
			throw new IllegalArgumentException(NOTE_NAME_MAY_NOT_BE_NULL);
		}
		ensureNoteSupportedByMIDI(noteName, registerValue);

		return allMIDIValues[(registerValue*12 + convertOrdinalOfNoteNameForMIDIValueCalculation(noteName))];
	}

	private static void ensureNoteSupportedByMIDI(NoteName noteName, int register) throws InvalidNoteRegisterException {
		//Make sure the register is valid...0 - 9
		if(register<0||register>9) {
			throw new InvalidNoteRegisterException(INVALID_REGISTER_VALUE,register,noteName);
		}

		//Make sure that the given note is supported by midi
		if(register==0) {
			switch(noteName) {
			case A:
			case A_SHARP:
			case B:
				//no problem, A,A#, and B are all supported by midi in the 0 register
				break;
			case C:
			case C_SHARP:
			case D:
			case D_SHARP:
			case E:
			case F:
			case F_SHARP:
			case G:
			case G_SHARP:
				//The given note is not supported by midi in this register.
				throw new InvalidNoteRegisterException(NOTE_UNSUPPORTED_IN_REGISTER_0,register,noteName);
			default:
				throw new IllegalArgumentException(UNHANDLED_CASE_STATEMENT);
			}
		}

		//Make sure that the given note is in the list
		if(register==9) {
			switch(noteName) {
			case A:
			case A_SHARP:
			case B:
			case C:
			case C_SHARP:
			case D:
			case D_SHARP:
			case E:
			case F:
			case F_SHARP:
			case G:
				break;
			case G_SHARP:
				//The given note is not supported by midi in this register.
				throw new InvalidNoteRegisterException(NOTE_UNSUPPORTED_IN_REGISTER_9,register,noteName);
			default:
				throw new IllegalArgumentException(UNHANDLED_CASE_STATEMENT);
			}
		}
	}

	/**
	 * MIDI numbers start from C instead of A so I have to convert the value of the note
	 * name before the calculation.
	 * @param noteName name of note
	 * @return
	 */
	private static int convertOrdinalOfNoteNameForMIDIValueCalculation(NoteName noteName) {
		switch(noteName) {
		case A:
			return 9;
		case A_SHARP:
			return 10;
		case B:
			return 11;
		case C:
			return 0;
		case C_SHARP:
			return 1;
		case D:
			return 2;
		case D_SHARP:
			return 3;
		case E:
			return 4;
		case F:
			return 5;
		case F_SHARP:
			return 6;
		case G:
			return 7;
		case G_SHARP:
			return 8;
		default:
			throw new IllegalArgumentException(INVALID_NOTE_NAME_GIVEN);
		}
	}
}
