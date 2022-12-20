package chord.relations.record;

import chord.ConsonanceRating;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;

/**
 * Record to return data from ScaleConsonanceModel that defines
 * a single chord/scale rating
 * @author DAD
 *
 */
public record ScaleConsonanceRecord(
		ChordSignature chordSignature, 
		ScaleSignature scaleSignature, 
		ConsonanceRating rating)  implements Rateable,StringPersistable{

	//indices for the persistence strings split by the 
	//StringPersistable.FIELD_SEPARATOR string
	private static final int CHORD_SIG_PERSISTENCE_STRING_INDEX = 0;
	private static final int SCALE_SIG_PERSISTENCE_STRING_INDEX = 1;
	private static final int CONSONANCE_RATING_PERSISTENCE_STRING_INDEX = 2;

	public static final ScaleConsonanceRecord createRecordFromString(String persistedString ) {
		if(persistedString == null) {
			throw new NullPointerException("persistedString may not be null");
		}
		final ChordSignature chordSig;
		final ScaleSignature scaleSig;
		final ConsonanceRating rating;
		final String persistedRating;

		String[] persistedFields = 
				persistedString.split(
						StringPersistable.FIELD_SEPARATOR);

		chordSig = ChordSignature.valueOf(persistedFields[CHORD_SIG_PERSISTENCE_STRING_INDEX]);
		scaleSig = ScaleSignature.valueOf(persistedFields[SCALE_SIG_PERSISTENCE_STRING_INDEX]);

		persistedRating = persistedFields[CONSONANCE_RATING_PERSISTENCE_STRING_INDEX];

		if(persistedRating.equals(StringPersistable.NULL_SIGNATURE)) {
			rating = null;
		}else {
			rating = ConsonanceRating.valueOf(persistedRating);
		}

		return new ScaleConsonanceRecord(chordSig, scaleSig, rating);
	}

	/**
	 * Create a new ScaleSignatureRecord.
	 * 
	 * No fields may be null
	 * @param chordSignature chordSignature to which the 
	 * scale signature is being prepared
	 * @param scaleSignature scaleSignature to which the
	 * chord signature is being compare
	 * @param rating rating of consonance, may be null for
	 * removal and retrieval of ratings.
	 */
	public ScaleConsonanceRecord{
		if(chordSignature == null)
			throw new NullPointerException("chord signature may not be null");
		if(scaleSignature == null)
			throw new NullPointerException("scale signature may not be null");

		//fields filled automagically
	}
	

	@Override
	public String createPersistenceString() {
		String separator = StringPersistable.FIELD_SEPARATOR;

		//we have to get the rating separately to avoid null pointer exceptions.
		String consRating = this.rating()==null?StringPersistable.NULL_SIGNATURE:this.rating().toString();

		return this.chordSignature().toString() + 
				separator +
				this.scaleSignature().toString() + 
				separator + 
				consRating;

	}

	@Override
	public boolean isRated() {
		return this.rating() != null;
	}

}
