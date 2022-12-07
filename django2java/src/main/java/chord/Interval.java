package chord;

import java.util.Arrays;

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

	private final String intervalName;

	private Interval(String intervalName) {
		this.intervalName  = intervalName;
	}

	public String displayText() {
		return intervalName;
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
