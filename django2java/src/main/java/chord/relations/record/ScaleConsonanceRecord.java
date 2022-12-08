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
		ConsonanceRating rating) {
	
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

}
