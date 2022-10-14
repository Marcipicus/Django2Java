package chord.relations;

import chord.ConsonanceRating;
import chord.Interval;

public class NoteRating {

	private Interval noteInterval;
	private ConsonanceRating rating;
	
	public NoteRating(Interval interval, ConsonanceRating rating) {
		if(interval == null) {
			throw new NullPointerException("Note Interval may not be null.");
		}
		if( !interval.validForRating()) {
			throw new IllegalArgumentException("Note Interval must be between UNISON and MAJOR7 inclusive");
		}
		if(rating== null) {
			throw new NullPointerException("Consonance Rating may not be null");
		}
		
		this.noteInterval = interval;
		this.rating = rating;
	}

	public Interval getNoteInterval() {
		return noteInterval;
	}

	public ConsonanceRating getRating() {
		return rating;
	}
	
	/**
	 * This method exists for checking duplicate ratings since you cannot
	 * just use a .equals mehotd
	 * @param newRating
	 * @return
	 */
	public boolean intervalsEqual(NoteRating other) {
		return this.getNoteInterval().equals(other.getNoteInterval());
	}
	
	//TODO: Might have to change this method to look at displayText
	@Override
	public String toString() {
		return(noteInterval.toString()+":"+rating.toString());
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this)
	        return true;
	    if (!(o instanceof NoteRating))
	        return false;
	    NoteRating other = (NoteRating) o;
	    
	    boolean noteIntervalsEqual, ratingsEqual;
	    
	    noteIntervalsEqual = this.noteInterval.equals(other.noteInterval);
	    ratingsEqual = this.rating.equals(other.rating);
	    return noteIntervalsEqual && ratingsEqual;
	}
	
	@Override
	public int hashCode() {
		final int prime = 17;
		
		int result = 1;
		
		result = prime * result + ((noteInterval==null) ? 0 : noteInterval.hashCode());
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		
		return result;
	}
}
