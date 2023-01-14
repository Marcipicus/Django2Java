package chord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import chord.ident.ScaleSignature;

/**
 * Enumeration used to define chord signatures, scale signatures,
 * and notes relative to a root note.
 * 
 * It declares all simple intervals and all compound intervals in
 * the second octave since those are the only ones needed to define
 * chords, and scales.
 * @author DAD
 *
 */
public enum Interval {
	UNISON("U"),
	MINOR2(UsefulUnicodeCharacters.flatSign + "2"),
	MAJOR2("2"),
	MINOR3(UsefulUnicodeCharacters.flatSign+"3"),
	MAJOR3("3"),
	PERFECT4("4"),
	DIMINISHED5(UsefulUnicodeCharacters.flatSign + "5"),
	PERFECT5("5"),
	MINOR6(UsefulUnicodeCharacters.flatSign + "6"),//Same as aug5
	MAJOR6("6"),
	MINOR7(UsefulUnicodeCharacters.flatSign + "7"),
	MAJOR7("7"),
	PERFECT8("8"),
	MINOR9(UsefulUnicodeCharacters.flatSign + "9"),
	MAJOR9("9"),
	MINOR10(UsefulUnicodeCharacters.flatSign + "10"),
	MAJOR10("10"),
	PERFECT11("11"),
	DIMINISHED12(UsefulUnicodeCharacters.flatSign + "12"),
	PERFECT12("12"),
	MINOR13(UsefulUnicodeCharacters.flatSign + 13),
	MAJOR13("13"),
	MINOR14(UsefulUnicodeCharacters.flatSign + 14),
	MAJOR14("14"),
	PERFECT15("P15");
	
	private static List<Interval> valuesAsList;
	
	/**
	 * Get a list of all intervals sorted by ordinal.
	 * Completely equivalent to Inteval.values
	 * 
	 * USE THIS INSTEAD OF Interval.values().
	 * 
	 * enum.values() creates a new array every time it is called
	 * so there is a massive increase in performance in nested
	 * for loops.
	 * 
	 * @return an unmodifiable list that contains all
	 * Intervals ordered by ordinal.
	 */
	public static List<Interval> valuesAsList(){
		if(valuesAsList == null) {
			valuesAsList = new ArrayList<>(Arrays.asList(Interval.values()));

			Collections.sort(valuesAsList);
			
			valuesAsList = Collections.unmodifiableList(valuesAsList);
		}
		return valuesAsList;
	}

	/**
	 * Populate the array with all of the Intervals in the first
	 * octave excluding duplicate notes(UNISON/PERFECT8
	 */
	private static final Interval[] firstOctaveValuesArray = 
			Arrays.copyOfRange(
					Interval.values(), 
					0, 
					Interval.MAJOR7.ordinal() + 1);
	/**
	 * Get an ordered array of all intervals in the first
	 * octave without duplicates(UNISON to MAJOR7 inclusive)				
	 * @return array of all intervals between UNISON and MAJOR7
	 * inclusive
	 */
	public static Interval[] valuesInFirstOctave() {
		return firstOctaveValuesArray;
	}

	/**
	 * Check to see if all of the intervals are non-null
	 * and in the first octave(UNISON to MAJOR7 inclusive).
	 * 
	 * @param intervals array of intervals to check.
	 * @return true if all of the intervals are in the first
	 * octave and non-null;
	 */
	public static boolean allIntervalsInFirstOctave(Interval... intervals) {
		//we have to iterate here to make sure that
		//the intervals are in the first octave.
		for(Interval interval : intervals) {
			if(interval == null) {
				return false;
			}
			if( !interval.inFirstOctave()) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Name of the interval used to generate display text.
	 */
	private final String intervalName;

	/**
	 * Create the interval with the given interval name
	 * which will be used for display text.
	 * @param intervalName name of interval used for displayText
	 */
	private Interval(String intervalName) {
		this.intervalName  = intervalName;
	}

	/**
	 * Get the text used to display the interval.
	 * 
	 * This is intended to be used with a note name
	 * to specify an interval e.g.Ab2 A with a minor 2nd
	 * @return
	 */
	public String displayText() {
		return intervalName;
	}
	
	/**
	 * Get the inversion of the given interval stated
	 * as a simple interval(Interval within the first octave.
	 * @return the inversion of the interval as a simple interval.
	 */
	public Interval getInversion() {
		//This had to be implemented as a switch
		//statement because I cannot add the constant
		//as a parameter to the constructor since the 
		//inversion cannot be used until it is declared.
		
		//we could calculate the value as well
		//but I can't be bothered to do it and
		//this works just as well
		switch(this) {
		case UNISON:
			return PERFECT8;
		case MINOR2:
			return MAJOR7;
		case MAJOR2:
			return MINOR7;
		case MINOR3:
			return MAJOR6;
		case MAJOR3:
			return MINOR6;
		case PERFECT4:
			return PERFECT5;
		case DIMINISHED5:
			return DIMINISHED5;
		case PERFECT5:
			return PERFECT4;
		case MINOR6:
			return MAJOR3;
		case MAJOR6:
			return MINOR3;
		case MINOR7:
			return MAJOR2;
		case MAJOR7:
			return MINOR2;
		case PERFECT8:
			return UNISON;
		case MINOR9:
			return MAJOR7;
		case MAJOR9:
			return MINOR7;
		case MINOR10:
			return MAJOR6;
		case MAJOR10:
			return MINOR6;
		case PERFECT11:
			return PERFECT5;
		case DIMINISHED12:
			return DIMINISHED5;
		case PERFECT12:
			return PERFECT4;
		case MINOR13:
			return MAJOR3;
		case MAJOR13:
			return MINOR3;
		case MINOR14:
			return MAJOR2;
		case MAJOR14:
			return MINOR2;
		case PERFECT15:
			return UNISON;
		default:
			throw new IllegalStateException("Unhandled case statement");
		}
	}

	/**
	 * Gets the next interval. If this is the last interval then
	 * the function rolls over to the first interval.
	 * @return
	 */
	public Interval getNextInterval() {
		try {
			return Interval.values()[ordinal() + 1];
		}catch(IndexOutOfBoundsException e){
			return Interval.values()[0];
		}
	}

	/**
	 * Get the previous interval. If this is the first interval
	 * then the function rolls over to the last interval.
	 * @return
	 */
	public Interval getPreviousInterval() {
		try {
			return Interval.values()[ordinal() - 1];
		}catch(IndexOutOfBoundsException e) {
			return Interval.values()[Interval.values().length - 1];
		}
	}

	/**
	 * Check to see if the Interval is within the first octave.
	 * This function exists so that duplicate intervals are not
	 * counted....e.g. Interval.UNISON and Interval.PERFECT8 refer
	 * to the same note. The second octave intervals only exist for
	 * extended chords.
	 * @return true if the interval is a major7th or below,
	 * false otherwise
	 */
	public boolean inFirstOctave() {
		return this.ordinal() < Interval.PERFECT8.ordinal();
	}
}
