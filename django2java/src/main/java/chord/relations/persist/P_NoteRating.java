package chord.relations.persist;

import chord.ConsonanceRating;
import chord.Interval;

public class P_NoteRating {
	private Interval noteInterval;
	private ConsonanceRating rating;

	public P_NoteRating() {
	}

	public Interval getNoteInterval() {
		return noteInterval;
	}

	public void setNoteInterval(Interval noteInterval) {
		this.noteInterval = noteInterval;
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
		return(noteInterval.toString()+":"+rating.toString());
	}
	
	@Override
	public boolean equals(Object o) {
	    if (o == this)
	        return true;
	    if (!(o instanceof P_NoteRating))
	        return false;
	    P_NoteRating other = (P_NoteRating) o;
	    
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
