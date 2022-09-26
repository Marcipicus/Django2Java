package chord.relations;

import chord.ConsonanceRating;
import chord.Interval;

public class IntervalRating {

	private Interval intervalBetweenRoots;
	private ConsonanceRating rating;
	
	public IntervalRating(Interval intervalBetweenRoots,ConsonanceRating rating) {
		this.intervalBetweenRoots = intervalBetweenRoots;
		this.rating = rating;
	}
	
	public IntervalRating() {
	}

	public Interval getIntervalBetweenRoots() {
		return intervalBetweenRoots;
	}

	public void setIntervalBetweenRoots(Interval intervalBetweenRoots) {
		this.intervalBetweenRoots = intervalBetweenRoots;
	}

	public ConsonanceRating getRating() {
		return rating;
	}

	public void setRating(ConsonanceRating rating) {
		this.rating = rating;
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
