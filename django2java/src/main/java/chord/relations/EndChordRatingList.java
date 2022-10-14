package chord.relations;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import chord.Interval;
import chord.ident.ChordSignature;

public class EndChordRatingList {

	private ChordSignature endSignature, startSignature;
	private List<IntervalRating> intervalRatings;
	
	public EndChordRatingList(ChordSignature startSignature, ChordSignature endSignature) {
		if(startSignature == null) {
			throw new NullPointerException("startSignature may not be null");
		}
		if(endSignature == null) {
			throw new NullPointerException("endSignature may not be null");
		}
		this.startSignature = startSignature;
		this.endSignature = endSignature;
		intervalRatings = new LinkedList<IntervalRating>();
	}
	
	public ChordSignature getStartSignature() {
		return startSignature;
	}

	public ChordSignature getEndsignature() {
		return endSignature;
	}
	
	/**
	 * Add the new rating to the existing ratings.
	 * 
	 * @param newRating rating to be added(may not be null and may not already exist.
	 * @return true if the rating was added, false if the added rating represents
	 * the same chord as the startSignature and hence should not be added
	 */
	public boolean addIntervalRating(IntervalRating newRating) {
		if(newRating == null) {
			throw new NullPointerException("newRating may not be null");
		}
		if(intervalRatings.contains(newRating)) {
			throw new IllegalArgumentException("added Rating already exists");
		}
		if(startSignature.equals(endSignature) && 
				(newRating.getIntervalBetweenRoots().equals(Interval.UNISON))) {
			return false;
		}
		intervalRatings.add(newRating);
		return true;
	}

	public List<IntervalRating> getIntervalRatings() {
		return Collections.unmodifiableList(intervalRatings);
	}


	//TODO: Might have to change this method to look at displayText
	@Override
	public String toString() {
		return("EndChord:"+endSignature.toString());
	}

	@Override
	public boolean equals(Object o) {
	    if (o == this)
	        return true;
	    if (!(o instanceof EndChordRatingList))
	        return false;
	    EndChordRatingList other = (EndChordRatingList) o;
	    
	    boolean startSignaturesEqual, endSignaturesEqual, intervalRatingsEqual;
	    
	    startSignaturesEqual = this.startSignature.equals(other.endSignature);
	    endSignaturesEqual = this.endSignature.equals(other.endSignature);
	    intervalRatingsEqual = this.intervalRatings.equals(other.intervalRatings);

	    return startSignaturesEqual && endSignaturesEqual && intervalRatingsEqual;
	}
	
	@Override
	public int hashCode() {
		final int prime = 23;
		
		int result = 1;
		
		result = prime * result + ((startSignature==null) ? 0 : startSignature.hashCode());
		result = prime * result + ((endSignature==null) ? 0 : endSignature.hashCode());
		result = prime * result + ((intervalRatings == null) ? 0 : intervalRatings.hashCode());
		
		return result;
	}
}
