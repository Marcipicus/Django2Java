package chord.relations.record;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;

/**
 * Record meant to be used to return Data from the NoteConsonanceModel.
 * @author DAD
 *
 */
public record NoteConsonanceRecord(
		ChordSignature chordSignature, 
		Interval interval, 
		ConsonanceRating rating) implements Rateable,StringPersistable{
	
	//indices for the persistence strings split by the 
	//StringPersistable.FIELD_SEPARATOR string
	private static final int CHORD_SIG_PERSISTENCE_STRING_INDEX = 0;
	private static final int INTERVAL_PERSISTENCE_STRING_INDEX = 1;
	private static final int CONSONANCE_RATING_PERSISTENCE_STRING_INDEX = 2;
	
	public static final NoteConsonanceRecord createRecordFromString(String persistedString ) {
		if(persistedString == null) {
			throw new NullPointerException("persistedString may not be null");
		}
		final ChordSignature chordSig;
		final Interval interval;
		final ConsonanceRating rating;
		final String persistedRating;
		
		String[] persistedFields = 
				persistedString.split(
						StringPersistable.FIELD_SEPARATOR);
		
		chordSig = ChordSignature.valueOf(persistedFields[CHORD_SIG_PERSISTENCE_STRING_INDEX]);
		interval = Interval.valueOf(persistedFields[INTERVAL_PERSISTENCE_STRING_INDEX]);
		
		persistedRating = persistedFields[CONSONANCE_RATING_PERSISTENCE_STRING_INDEX];
		
		if(persistedRating.equals(StringPersistable.NULL_SIGNATURE)) {
			rating = null;
		}else {
			rating = ConsonanceRating.valueOf(persistedRating);
		}
		
		return new NoteConsonanceRecord(chordSig, interval, rating);
	}
	
	/**
	 * Create a new NoteConsonanceRecord.
	 * 
	 * Only the rating may be null to signify that there
	 * is no rating for the record.
	 * 
	 * @param chordSignature chordSignature for consonance record.
	 * @param interval interval between root of chordSignature and note
	 * (Must be between UNISON and MAJOR7
	 * @param rating rating for the note/chord consonance, null
	 * if no rating exists.
	 */
	public NoteConsonanceRecord {
		if(chordSignature == null)
			throw new NullPointerException("chordSignature May not be null");
		if(interval == null)
			throw new NullPointerException("interval may not be null");
		if(!interval.inFirstOctave())
			throw new IllegalArgumentException("Only intervals within the first octave are allowed (UNISON->MAJOR7) inclusive");
		//assignments created automagically
	}
	


	@Override
	public String createPersistenceString() {
		String separator = StringPersistable.FIELD_SEPARATOR;

		//we have to get the rating separately to avoid null pointer exceptions.
		String consRating = this.rating()==null?StringPersistable.NULL_SIGNATURE:this.rating().toString();

		return this.chordSignature().toString() + 
				separator +
				this.interval().toString() + 
				separator + 
				consRating;

	}

	@Override
	public boolean isRated() {
		return rating!=null;
	}
}
