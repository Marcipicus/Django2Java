package chord.relations;

import chord.ConsonanceRating;
import chord.Interval;

public class IntervalRating {

	private Interval intervalBetweenRoots;
	private ConsonanceRating rating;
	
	public IntervalRating(Interval intervalBetweenRoots,ConsonanceRating rating) {
		if(intervalBetweenRoots == null) {
			throw new NullPointerException("intervalBetweenRoots may not be null");
		}
		if( !intervalBetweenRoots.validForRating()) {
			throw new IllegalArgumentException("The interval between the roots of the chords must be between UNISON and MAJOR7");
		}
		if(rating == null) {
			throw new NullPointerException("rating may not be null");
		}
		this.intervalBetweenRoots = intervalBetweenRoots;
		this.rating = rating;
	}

	public Interval getIntervalBetweenRoots() {
		return intervalBetweenRoots;
	}

	public ConsonanceRating getRating() {
		return rating;
	}
	
	//TODO: Might have to change this method to look at displayText
	@Override
	public String toString() {
		return(intervalBetweenRoots.toString()+":"+rating.toString());
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this)
	        return true;
	    if (!(o instanceof IntervalRating))
	        return false;
	    IntervalRating other = (IntervalRating) o;
	    
	    boolean intervalsEqual, ratingsEqual;
	    
	    intervalsEqual = this.intervalBetweenRoots.equals(other.intervalBetweenRoots);
	    ratingsEqual = this.rating.equals(other.rating);
	    return intervalsEqual && ratingsEqual;
	}
	
	@Override
	public int hashCode() {
		final int prime = 17;
		
		int result = 1;
		
		result = prime * result + ((intervalBetweenRoots==null) ? 0 : intervalBetweenRoots.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		
		return result;
	}
}
