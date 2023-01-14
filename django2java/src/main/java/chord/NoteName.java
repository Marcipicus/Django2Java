package chord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The Note class models a musical note without octave information. Plain notes and that is it.
 * @author DAD
 * 
 * @apiNote Note display limitation for keys containing #'s and b's. 
 * Natural notes will be displayed as follows (Key of G--F will be displayed as "F" not "f natural"
 *
 */
public enum NoteName{
	A,
	A_SHARP,
	B,
	C,
	C_SHARP,
	D,
	D_SHARP,
	E,
	F,
	F_SHARP,
	G,
	G_SHARP;


	public static final String INTERVAL_MAY_NOT_BE_NULL_MESSAGE  =  "Interval may not be null";
	public static final String GIVEN_STRING_DOES_NOT_CORRESPOND_TO_NOTE  =  "The given string does not correspond to any note.";

	
	private static List<NoteName> valuesAsList;
	
	/**
	 * Get a list of all NoteNames sorted by ordinal.
	 * Completely equivalent to NoteName.values
	 * 
	 * USE THIS INSTEAD OF NoteName.values().
	 * 
	 * enum.values() creates a new array every time it is called
	 * so there is a massive increase in performance in nested
	 * for loops.
	 * 
	 * @return an unmodifiable list that contains all
	 * NoteNames ordered by ordinal.
	 */
	public static List<NoteName> valuesAsList(){
		if(valuesAsList == null) {
			valuesAsList = new ArrayList<>(Arrays.asList(NoteName.values()));

			Collections.sort(valuesAsList);
			
			valuesAsList = Collections.unmodifiableList(valuesAsList);
		}
		return valuesAsList;
	}
	
	/**
	 * Get the largest possible ordinal value for a note.
	 * @return
	 */
	private static int getLargestOrdinal() {
		return NoteName.values().length-1;
	}


	/**
	 * Return the Note one semitone higher than the current note.
	 * @return
	 */
	public NoteName incrementOneSemitone() {
		if(this.ordinal()+1 > NoteName.getLargestOrdinal()) {
			return values()[0];
		}else {
			return values()[this.ordinal()+1];
		}
	}

	/**
	 * Return the note one semitone lower than the current note.
	 * @return
	 */
	public NoteName decrementOneSemitone() {
		if(this.ordinal()-1 < 0) {
			return values()[NoteName.getLargestOrdinal()];
		}else {
			return values()[this.ordinal()-1];
		}
	}

	/**
	 * Return the note the specified interval away from this Note.
	 * @param interval
	 * @return
	 */
	public NoteName getNoteByInterval(Interval interval) {
		if(interval == null){
			throw new IllegalArgumentException(INTERVAL_MAY_NOT_BE_NULL_MESSAGE);
		}
		NoteName noteHolder = this;

		for(int i = 0; i < interval.ordinal(); i++) {
			noteHolder = noteHolder.incrementOneSemitone();
		}

		return noteHolder;
	}
	
	public String displayText() {
		return displayText(true);
	}

	public String displayText(boolean useSharps) {
		switch(this) {
		case A:
			return("A");
		case A_SHARP:
			if(useSharps) {
				return "A"+UsefulUnicodeCharacters.sharpSign;
			}
			else {
				return "B" + UsefulUnicodeCharacters.flatSign;
			}
		case B:
			return "B";
		case C:
			return "C";
		case C_SHARP:
			if(useSharps) {
				return "C"+UsefulUnicodeCharacters.sharpSign;
			}
			else {
				return "D" + UsefulUnicodeCharacters.flatSign;
			}
		case D:
			return "D";
		case D_SHARP:
			if(useSharps) {
				return "D"+UsefulUnicodeCharacters.sharpSign;
			}
			else {
				return "E" + UsefulUnicodeCharacters.flatSign;
			}
		case E:
			return "E";
		case F:
			return "F";
		case F_SHARP:
			if(useSharps) {
				return "F"+UsefulUnicodeCharacters.sharpSign;
			}
			else {
				return "G" + UsefulUnicodeCharacters.flatSign;
			}
		case G:
			return "G";
		case G_SHARP:
			if(useSharps) {
				return "G"+UsefulUnicodeCharacters.sharpSign;
			}
			else {
				return "A" + UsefulUnicodeCharacters.flatSign;
			}
		default:
			throw new RuntimeException("Unhandled case statement");
		}
	}
}