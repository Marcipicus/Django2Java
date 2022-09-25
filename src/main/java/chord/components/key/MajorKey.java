package chord.components.key;

import chord.NoteName;

/**
 * I'm not sure if this class is really necessary since the keys can
 * be dealt with using musicxml or MIDI numbers.
 * @author DAD
 *
 */
public enum MajorKey {
	C(MinorKey.A),
	G(MinorKey.E),
	D(MinorKey.B),
	A(MinorKey.F_S),
	E(MinorKey.C_S),
	B(MinorKey.G_S),
	F_S(MinorKey.D_S),
	C_S(MinorKey.A_S),
	Cb(MinorKey.Ab),
	Gb(MinorKey.Eb),
	Db(MinorKey.Bb),
	Ab(MinorKey.F),
	Eb(MinorKey.C),
	Bb(MinorKey.G),
	F(MinorKey.D);
	
	private static String naturalSign = "\u0266E";
	private static String sharpSign = "\u0266F";
	private static String flatSign = "\u0266D";
	
	private static final String MSG_UNHANDLED_NOTE_NAME_GIVEN = "The given note name is not handled.";
	private static final String MSG_UNHANDLED_MAJOR_KEY_GIVEN = "The given Major Key is not handled.";
	private final MinorKey relativeMinor;
	
	private MajorKey(MinorKey relativeMinor) {
		if(relativeMinor == null) {
			String MSG_RELATIVE_MINOR_CANNOT_BE_NULL = "Relative Minor may not be null";
			throw new IllegalArgumentException(MSG_RELATIVE_MINOR_CANNOT_BE_NULL);
		}
		this.relativeMinor = relativeMinor;
	}
	
	public MinorKey getRelativeMinor() {
		return this.relativeMinor;
	}
	

	public String getNoteStringForKey(NoteName noteName) {
		MajorKey currentKey = MajorKey.values()[this.ordinal()];
		switch(currentKey) {
		case A:
			return getNoteString_Key_A(noteName);
		case Ab:
			break;
		case B:
			return getNoteString_Key_B(noteName);
		case Bb:
			break;
		case C:
			return getNoteString_Key_C(noteName);
		case C_S:
			return getNoteString_Key_C_sharp(noteName);
		case Cb:
			break;
		case D:
			return getNoteString_Key_D(noteName);
		case Db:
			break;
		case E:
			return getNoteString_Key_E(noteName);
		case Eb:
			break;
		case F:
			break;
		case F_S:
			return getNoteString_Key_F_sharp(noteName);
		case G:
			return getNoteString_Key_G(noteName);
		case Gb:
			break;
		default:
			break;
		
		}
		return null;
	}
	
	private String getNoteString_Key_C(NoteName noteName) {
		switch(noteName) {
		case A:
			return "A";
		case A_SHARP:
			return "A"+sharpSign;
		case B:
			return "B";
		case C:
			return "C";
		case C_SHARP:
			return "C"+sharpSign;
		case D:
			return "D";
		case D_SHARP:
			return "D"+sharpSign;
		case E:
			return "E";
		case F:
			return "F";
		case F_SHARP:
			return "F"+sharpSign;
		case G:
			return "G";
		case G_SHARP:
			return "G"+sharpSign;
		default:
			throw new IllegalArgumentException(MSG_UNHANDLED_NOTE_NAME_GIVEN);
		
		}
	}
	
	private String getNoteString_Key_G(NoteName noteName) {
		switch (noteName) {
		case A:
		case A_SHARP:
		case B:
		case C:
		case C_SHARP:
		case D:
		case D_SHARP:
		case E:
		case G:
		case G_SHARP:
			return getNoteString_Key_C(noteName);
		case F:
			return "F"+naturalSign;
		case F_SHARP:
			return "F";
		default:
			throw new IllegalArgumentException(MSG_UNHANDLED_NOTE_NAME_GIVEN);
		}
		
	}
	
	private String getNoteString_Key_D(NoteName noteName) {
		switch (noteName) {
		case A:
		case A_SHARP:
		case B:
		case D:
		case D_SHARP:
		case E:
		case G:
		case G_SHARP:
		case F:
		case F_SHARP:
			return getNoteString_Key_G(noteName);
		case C:
			return "C"+naturalSign;
		case C_SHARP:
			return "C";
		default:
			throw new IllegalArgumentException(MSG_UNHANDLED_NOTE_NAME_GIVEN);
		}
		
	}
	
	private String getNoteString_Key_A(NoteName noteName) {
		switch (noteName) {
		case A:
		case A_SHARP:
		case B:
		case D:
		case D_SHARP:
		case E:
		case F:
		case F_SHARP:
		case C:
		case C_SHARP:
			return getNoteString_Key_D(noteName);
		case G:
			return "G"+naturalSign;
		case G_SHARP:
			return "G";
		default:
			throw new IllegalArgumentException(MSG_UNHANDLED_NOTE_NAME_GIVEN);
		}
		
	}
	
	private String getNoteString_Key_E(NoteName noteName) {
		switch (noteName) {
		case A:
		case A_SHARP:
		case B:
		case E:
		case F:
		case F_SHARP:
		case C:
		case C_SHARP:
		case G:
		case G_SHARP:
			return getNoteString_Key_A(noteName);
		case D:
			return "D" + naturalSign;
		case D_SHARP:
			return "D";
		default:
			throw new IllegalArgumentException(MSG_UNHANDLED_NOTE_NAME_GIVEN);
		}
	}
	
	private String getNoteString_Key_B(NoteName noteName) {
		switch (noteName) {
		case B:
		case E:
		case F:
		case F_SHARP:
		case C:
		case C_SHARP:
		case G:
		case G_SHARP:
		case D:
		case D_SHARP:
			return getNoteString_Key_E(noteName);
		case A:
			return "A" + naturalSign;
		case A_SHARP:
			return "A";
		default:
			throw new IllegalArgumentException(MSG_UNHANDLED_NOTE_NAME_GIVEN);
		}
	}
	
	private String getNoteString_Key_F_sharp(NoteName noteName) {
		switch (noteName) {
		case B:
		case E:
		case C:
		case C_SHARP:
		case G:
		case G_SHARP:
		case D:
		case D_SHARP:
		case A:
		case A_SHARP:
			return getNoteString_Key_B(noteName);
		case F:
			return "F" + naturalSign;
		case F_SHARP:
			return "F";
		default:
			throw new IllegalArgumentException(MSG_UNHANDLED_NOTE_NAME_GIVEN);
		}
	}
	
	private String getNoteString_Key_C_sharp(NoteName noteName) {
		switch (noteName) {
		case B:
		case E:
		case G:
		case G_SHARP:
		case D:
		case D_SHARP:
		case A:
		case A_SHARP:
		case F:
		case F_SHARP:
			return getNoteString_Key_F_sharp(noteName);
		case C:
			return "C"+naturalSign;
		case C_SHARP:
			return "C";
		default:
			throw new IllegalArgumentException(MSG_UNHANDLED_NOTE_NAME_GIVEN);
		}
	}
	
	private String getNoteString_Key_F(NoteName noteName) {
		switch (noteName) {
		case E:
		case G:
		case G_SHARP:
		case D:
		case D_SHARP:
		case A:
		case F:
		case F_SHARP:
		case C:
		case C_SHARP:
			return getNoteString_Key_C(noteName);

		case A_SHARP:
		case B:
		default:
			throw new IllegalArgumentException(MSG_UNHANDLED_NOTE_NAME_GIVEN);
		}
	}
	
	
	//TODO: I have to finish this.....later
	private String getNoteString_Key_C_flat(NoteName noteName) {
		switch(noteName) {
		case A:
			return "A"+naturalSign;
		case A_SHARP:
			return "B" + flatSign;
		case B:
			return "C";
		case C:
			return "C"+ naturalSign;
		case C_SHARP:
			return "C"+sharpSign;
		case D:
			return "D";
		case D_SHARP:
			return "D"+sharpSign;
		case E:
			return "E";
		case F:
			return "F";
		case F_SHARP:
			return "F"+sharpSign;
		case G:
			return "G";
		case G_SHARP:
			return "G"+sharpSign;
		default:
			throw new IllegalArgumentException(MSG_UNHANDLED_NOTE_NAME_GIVEN);
		
		}
	}
	

}
