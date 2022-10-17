package chord.relations;

import java.util.HashMap;
import java.util.Map;

import chord.ConsonanceRating;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;

/**
 * Main data structure for rating the consonance of chords to scales.
 * @author DAD
 *
 */
public class ScaleConsonanceModel {
	
	private Map<ChordSignature, Map<ScaleSignature,ConsonanceRating>> chordToScaleRatingMap;
	
	/**
	 * Create an empty ScaleConsonanceModel.
	 */
	public ScaleConsonanceModel() {
		this.chordToScaleRatingMap = new HashMap<>();
	}
	
	/**
	 * Add the rating for the given chord signature and scale signature
	 * @param chordSig the chord that the scale signature is being compared to
	 * @param scaleSig the scale signature being compared to the chord
	 * @param rating the rating of how consonant the chord and scale signatures are
	 * when compared to each other.
	 * @return the previous rating mapped to the chord and scale signatures,
	 * null if no previous rating exists.
	 */
	public ConsonanceRating addRating(ChordSignature chordSig, ScaleSignature scaleSig, ConsonanceRating rating) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(scaleSig == null) {
			throw new NullPointerException("Scale Signature may not be null.");
		}
		if(rating == null) {
			throw new NullPointerException("Rating may not be null.");
		}
		Map<ScaleSignature,ConsonanceRating> ratingMapForChordSignature = 
				chordToScaleRatingMap.get(chordSig);

		if(ratingMapForChordSignature == null) {
			ratingMapForChordSignature = new HashMap<>();
			chordToScaleRatingMap.put(chordSig, ratingMapForChordSignature);
		}
		
		return ratingMapForChordSignature.put(scaleSig,rating);
	}
	
	/**
	 * Remove the ConsonanceRating mapped to the given chordSignature and scale signature.
	 * @param chordSig ChordSignature from which the rating is being removed.
	 * @param scaleSig the scaleSignature from which the rating is being removed.
	 * @return the rating that was removed if successful,
	 * null if the rating does not exist.
	 */
	public ConsonanceRating removeRating(ChordSignature chordSig, ScaleSignature scaleSig) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(scaleSig == null) {
			throw new NullPointerException("scale signature may not be null.");
		}

		Map<ScaleSignature,ConsonanceRating> ratingMapForChordSignature = 
				chordToScaleRatingMap.get(chordSig);

		return ratingMapForChordSignature == null? null : ratingMapForChordSignature.remove(scaleSig);
	}
	
	/**
	 * Retrieve the rating for the given chord signature and scaleSignature.
	 * @param chordSig Chord signature for which the rating is being retrieved
	 * @param scaleSig scaleSignature for which the rating is being retrieved
	 * @return the rating for the parameters if it exists, 
	 * null otherwise
	 */
	public ConsonanceRating getRating(ChordSignature chordSig, ScaleSignature scaleSig) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(scaleSig == null) {
			throw new NullPointerException("Scale signature may not be null.");
		}
		
		Map<ScaleSignature,ConsonanceRating> ratingMapForChordSignature = 
				chordToScaleRatingMap.get(chordSig);

		return ratingMapForChordSignature == null? null : ratingMapForChordSignature.get(scaleSig);
	}
}
