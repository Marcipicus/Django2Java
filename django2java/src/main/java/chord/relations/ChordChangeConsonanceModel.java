package chord.relations;

import java.util.HashMap;
import java.util.Map;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;

/**
 * Main data structure for rating chord changes.
 * @author DAD
 *
 */
public class ChordChangeConsonanceModel {
	
	//TODO: Create methods to write and read to/from file for ChordChangeConsonanceModel
	//
	//also create method to find the last combination that was rated
	//or the next one that needs to be rated.
	

	

	private Map<ChordSignature,Map<ChordSignature,IntervalRatingMap>> chordChangeConsonanceMap;
	
	/**
	 * Create a new ChordChangeConsonanceModel.
	 */
	public ChordChangeConsonanceModel() {
		this.chordChangeConsonanceMap = new HashMap<>();
	}
	
	/**
	 * Add a rating for the given start chord, end chord, and relation between roots.
	 * @param startChordSig the start chord signature
	 * @param endChordSig the end chord signature
	 * @param intervalBetweenRoots interval between the roots of the start chord and end chord.
	 * @param rating rating to be added
	 * @return the previous rating for the parameters,
	 * null if there was no previous rating.
	 */
	public ConsonanceRating addRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots, ConsonanceRating rating) {
		if(startChordSig == null) {
			throw new NullPointerException("start chord sig may not be null");
		}
		if(endChordSig == null) {
			throw new NullPointerException("end chord sig may not be null");
		}
		if(intervalBetweenRoots == null) {
			throw new NullPointerException("interval between roots may not be null.");
		}
		if( !intervalBetweenRoots.inFirstOctave()) {
			throw new IllegalArgumentException("Interval between roots must be between UNISON and MAJOR7 inclusive");
		}
		if(rating == null) {
			throw new NullPointerException("rating must not be null");
		}
		//if the start and end chords are the same and the interval between roots is unison,
		//then the two chords are exactly the same and should not be rated.
		//TODO: I might throw a created exception here so the calling function can tell
		//that the rating wasn't added and i don't have to confuse the return values of
		//the function
		if(startChordSig.equals(endChordSig) && intervalBetweenRoots.equals(Interval.UNISON)){
			throw new IllegalArgumentException("");
		}
		
		//I have to grab each map one at a time so that I don't
		//dereference a null pointer
		Map<ChordSignature,IntervalRatingMap> startChordToEndChordRatingMap =
				chordChangeConsonanceMap.get(startChordSig);
		
		if(startChordToEndChordRatingMap == null) {
			startChordToEndChordRatingMap = new HashMap<>();
			chordChangeConsonanceMap.put(startChordSig, startChordToEndChordRatingMap);
		}
		
		IntervalRatingMap endChordIntervalRatingMap = 
				startChordToEndChordRatingMap.get(endChordSig);
		
		if(endChordIntervalRatingMap == null) {
			endChordIntervalRatingMap = new IntervalRatingMap();
			startChordToEndChordRatingMap.put(endChordSig, endChordIntervalRatingMap);
		}
		
		return endChordIntervalRatingMap.put(intervalBetweenRoots, rating);
	}
	
	/**
	 * Remove a rating for the given parameters.
	 * @param startChordSig start chord signature
	 * @param endChordSig end chord signature
	 * @param intervalBetweenRoots interval between the roots of the two chords.
	 * @return the rating associated with the parameters, or null if no
	 * rating exists.
	 */
	public ConsonanceRating removeRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots) {
		if(startChordSig == null) {
			throw new NullPointerException("start chord sig may not be null");
		}
		if(endChordSig == null) {
			throw new NullPointerException("end chord sig may not be null");
		}
		if(intervalBetweenRoots == null) {
			throw new NullPointerException("interval between roots may not be null.");
		}
		if( !intervalBetweenRoots.inFirstOctave()) {
			throw new IllegalArgumentException("Interval between roots must be between UNISON and MAJOR7 inclusive");
		}
		
		//Have to get each map one at a time so that we don't
		//dereference a null pointer.
		Map<ChordSignature,IntervalRatingMap> endChordSigToRatingMap =
				this.chordChangeConsonanceMap.get(startChordSig);
		if(endChordSigToRatingMap == null) {
			return null;
		}
		
		IntervalRatingMap chordChangeIntervalRatingMap = 
				endChordSigToRatingMap.get(endChordSig);
		if(chordChangeIntervalRatingMap == null) {
			return null;
		}
		
		return chordChangeIntervalRatingMap.remove(intervalBetweenRoots);
	}
	
	/**
	 * Get the rating for the given parameters.
	 * @param startChordSig the start chord signature
	 * @param endChordSig the end chord signature
	 * @param intervalBetweenRoots interval between the roots of the two chords
	 * @return the rating associated with the parameters,
	 * null if no rating exists
	 */
	public ConsonanceRating getRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots) {
		if(startChordSig == null) {
			throw new NullPointerException("start chord sig may not be null");
		}
		if(endChordSig == null) {
			throw new NullPointerException("end chord sig may not be null");
		}
		if(intervalBetweenRoots == null) {
			throw new NullPointerException("interval between roots may not be null.");
		}
		if( !intervalBetweenRoots.inFirstOctave()) {
			throw new IllegalArgumentException("Interval between roots must be between UNISON and MAJOR7 inclusive");
		}
		
		//Have to get each map one at a time so that we don't
		//dereference a null pointer.
		Map<ChordSignature,IntervalRatingMap> endChordSigToRatingMap =
				this.chordChangeConsonanceMap.get(startChordSig);
		if(endChordSigToRatingMap == null) {
			return null;
		}
		
		IntervalRatingMap chordChangeIntervalRatingMap = 
				endChordSigToRatingMap.get(endChordSig);
		if(chordChangeIntervalRatingMap == null) {
			return null;
		}
		
		return chordChangeIntervalRatingMap.get(intervalBetweenRoots);
	}
}
