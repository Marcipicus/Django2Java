package chord.relations;

import java.util.HashMap;
import java.util.Map;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;

/**
 * Main data model for rating the consonance of chord signatures to
 * single notes. The relationship between the notes and chords is defined
 * by the interval between the root note of the chord and the note that is
 * being rated.
 */
public class NoteConsonanceModel {
	
	private Map<ChordSignature, IntervalRatingMap> chordToIntervalRatingMap;
	
	/**
	 * Create an empty NoteConsonanceModel.
	 */
	public NoteConsonanceModel() {
		this.chordToIntervalRatingMap = new HashMap<>();
	}
	
	/**
	 * Add the rating for the given chord signature and interval.
	 * @param chordSig the chord that the interval is being compared to.
	 * @param interval the interval between the root note of the chord to note whose
	 * consonance is being rated
	 * @param rating the rating of how consonant the chord is.
	 * @return the previous rating that existed for the chord signature and interval,
	 * null if no previous rating exists.
	 */
	public ConsonanceRating addRating(ChordSignature chordSig, Interval interval, ConsonanceRating rating) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(interval == null) {
			throw new NullPointerException("Interval may not be null.");
		}
		if(!interval.inFirstOctave()) {
			throw new IllegalArgumentException("Interval must be between UNISON and MAJOR7 inclusive.");
		}
		if(rating == null) {
			throw new NullPointerException("Rating may not be null.");
		}
		
		IntervalRatingMap ratingMapForChordSignature = 
				chordToIntervalRatingMap.get(chordSig);
		
		if(ratingMapForChordSignature == null) {
			ratingMapForChordSignature = new IntervalRatingMap();
			chordToIntervalRatingMap.put(chordSig, ratingMapForChordSignature);
		}
		
		return ratingMapForChordSignature.put(interval,rating);
	}
	
	/**
	 * Remove the ConsonanceRating mapped to the given chordSignature and interval.
	 * @param chordSig ChordSignature from which the rating is being removed.
	 * @param interval the interval from which the rating is being removed.
	 * @return the rating that was removed if successful,
	 * null if the rating does not exist.
	 */
	public ConsonanceRating removeRating(ChordSignature chordSig, Interval interval) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(interval == null) {
			throw new NullPointerException("Interval may not be null.");
		}
		if(!interval.inFirstOctave()) {
			throw new IllegalArgumentException("Interval must be between UNISON and MAJOR7 inclusive.");
		}
		
		IntervalRatingMap ratingMapForChordSignature = 
				chordToIntervalRatingMap.get(chordSig);
		
		return ratingMapForChordSignature == null? null : ratingMapForChordSignature.remove(interval);
	}
	
	/**
	 * Retrieve the rating for the given chord signature and interval.
	 * @param chordSig Chord signature for which the interval is rated
	 * @param interval interval between the root note of the chord and the note whose
	 * rating we are retrieving. 
	 * @return the rating for the parameters if it exists, 
	 * null otherwise
	 */
	public ConsonanceRating getRating(ChordSignature chordSig, Interval interval) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(interval == null) {
			throw new NullPointerException("Interval may not be null.");
		}
		if(!interval.inFirstOctave()) {
			throw new IllegalArgumentException("Interval must be between UNISON and MAJOR7 inclusive.");
		}
		
		IntervalRatingMap ratingMapForChordSignature = 
				chordToIntervalRatingMap.get(chordSig);

		return ratingMapForChordSignature == null? null : ratingMapForChordSignature.get(interval);
	}
}
