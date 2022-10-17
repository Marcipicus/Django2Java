package chord.relations.persist;

import java.util.List;

import chord.ident.ChordSignature;

public class EndChordRatingList {

	private ChordSignature endSignature;
	private List<IntervalRating> intervalRatings;
	
	public EndChordRatingList() {
	}

	public ChordSignature getEndsignature() {
		return endSignature;
	}

	public void setEndsignature(ChordSignature endsignature) {
		this.endSignature = endsignature;
	}

	public List<IntervalRating> getIntervalRatings() {
		return intervalRatings;
	}

	public void setIntervalRatings(List<IntervalRating> intervalRatings) {
		this.intervalRatings = intervalRatings;
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
	    
	    boolean signaturesEqual, intervalRatingsEqual;
	    
	    signaturesEqual = this.endSignature.equals(other.endSignature);
	    intervalRatingsEqual = this.intervalRatings.equals(other.intervalRatings);

	    return signaturesEqual && intervalRatingsEqual;
	}
	
	@Override
	public int hashCode() {
		final int prime = 23;
		
		int result = 1;
		
		result = prime * result + ((endSignature==null) ? 0 : endSignature.hashCode());
		result = prime * result + ((intervalRatings == null) ? 0 : intervalRatings.hashCode());
		
		return result;
	}
}
