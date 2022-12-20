package chord.relations.record;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;

/**
 * Record to represent a single chord change rating defined
 * by the start and end chord types(signatures) and the interval
 * between the roots of the two chords.
 * @author DAD
 *
 */
public record ChordChangeConsonanceRecord(
		ChordSignature startChordSignature,
		ChordSignature endChordSignature,
		Interval intervalBetweenRoots,
		ConsonanceRating rating) implements Rateable,StringPersistable{
	
	//indices for the persistence strings split by the 
	//StringPersistable.FIELD_SEPARATOR string
	private static final int START_CHORD_PERSISTENCE_STRING_INDEX = 0;
	private static final int END_CHORD_PERSISTENCE_STRING_INDEX = 1;
	private static final int INTERVAL_BETWEEN_ROOTS_PERSISTENCE_STRING_INDEX = 2;
	private static final int CONSONANCE_RATING_PERSISTENCE_STRING_INDEX = 3;

	
	public static final ChordChangeConsonanceRecord createRecordFromString(String persistedString ) {
		if(persistedString == null) {
			throw new NullPointerException("persistedString may not be null");
		}
		final ChordSignature startChordSig,endChordSig;
		final Interval intervalBetweenRoots;
		final ConsonanceRating rating;
		final String persistedRating;
		
		String[] persistedFields = 
				persistedString.split(
						StringPersistable.FIELD_SEPARATOR);
		
		startChordSig = ChordSignature.valueOf(persistedFields[START_CHORD_PERSISTENCE_STRING_INDEX]);
		endChordSig = ChordSignature.valueOf(persistedFields[END_CHORD_PERSISTENCE_STRING_INDEX]);
		intervalBetweenRoots = Interval.valueOf(persistedFields[INTERVAL_BETWEEN_ROOTS_PERSISTENCE_STRING_INDEX]);
		
		persistedRating = persistedFields[CONSONANCE_RATING_PERSISTENCE_STRING_INDEX];
		
		if(persistedRating.equals(StringPersistable.NULL_SIGNATURE)) {
			rating = null;
		}else {
			rating = ConsonanceRating.valueOf(persistedRating);
		}
		
		return new ChordChangeConsonanceRecord(startChordSig, endChordSig, intervalBetweenRoots, rating);
	}

	/**
	 * Create a chordChangeConsonanceRecord with the given parameters.
	 * Represents the characteristics of a chord change without specifying
	 * the concrete chords.
	 * 
	 * (e.g. start(MINOR) end (MAJOR) interval (MINOR2)
	 * applied to a start root note of A would result in a chord change of 
	 * Aminor->A#Major
	 * 
	 * No parameters are allowed to be null
	 * @param startChordSignature type of start chord(MAJOR,MINOR..)
	 * @param endChordSignature type of end chord(MAJOR,MINOR,SUS2,SUS4....) 
	 * @param intervalBetweenRoots the interval between the roots of the start
	 * and end chords
	 * @param rating rating for the chord change, may be null for retrieval/removal
	 * of ratings in the ChordChangeConsonanceModel
	 */
	public ChordChangeConsonanceRecord{
		//Null Pointer Checks
		if(startChordSignature == null)
			throw new NullPointerException("start chord signature may not be null");
		if(endChordSignature == null)
			throw new NullPointerException("end chord signature may not be null");
		if(intervalBetweenRoots == null)
			throw new NullPointerException("interval between roots may not be null");

		//interval within bounds check
		if( !intervalBetweenRoots.inFirstOctave() )
			throw new IllegalArgumentException("interval between roots must be between UNISON and MAJOR7 inclusive.");


		//Make sure that the parameters do not create the same start and end chords
		//e.g. startChordSig = MAJOR endChordSig=MAJOR intervalBetweenRoots = UNISON
		//a record with the previous parameters will represent the same chord
		if(startChordSignature.equals(endChordSignature) && 
				intervalBetweenRoots.equals(Interval.UNISON))
			throw new IllegalArgumentException("Chord change record's start and end chords are the same");

		//fields filled automagically
	}


	@Override
	public String createPersistenceString() {
		String separator = StringPersistable.FIELD_SEPARATOR;

		//we have to get the rating separately to avoid null pointer exceptions.
		String consRating = this.rating()==null?StringPersistable.NULL_SIGNATURE:this.rating().toString();

		return startChordSignature.toString() + 
				separator +
				endChordSignature.toString() + 
				separator + 
				intervalBetweenRoots.toString() + 
				separator +
				consRating;

	}
	
	@Override
	public boolean isRated() {
		return this.rating() != null;
	}
}
