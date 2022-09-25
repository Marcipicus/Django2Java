package chord.relations;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import chord.ident.ChordSignature;

@XmlRootElement(name="NoteConsonance")
@XmlAccessorType(XmlAccessType.FIELD)
public class NoteConsonance {

	private ChordSignature chordSig;
	private List<NoteRating> noteRatings;
	
	public NoteConsonance() {
	}

	public ChordSignature getChordSig() {
		return chordSig;
	}

	public void setChordSig(ChordSignature chordSig) {
		this.chordSig = chordSig;
	}

	public List<NoteRating> getNoteRatings() {
		return noteRatings;
	}

	public void setNoteRatings(List<NoteRating> noteRatings) {
		this.noteRatings = noteRatings;
	}
	
	//TODO: Might have to change this method to look at displayText
	@Override
	public String toString() {
		return("Chord:"+chordSig.toString());
	}

	@Override
	public boolean equals(Object o) {
	    if (o == this)
	        return true;
	    if (!(o instanceof NoteConsonance))
	        return false;
	    NoteConsonance other = (NoteConsonance) o;
	    
	    boolean signaturesEqual, noteRatingsEqual;
	    
	    signaturesEqual = this.chordSig.equals(other.chordSig);
	    noteRatingsEqual = this.noteRatings.equals(other.noteRatings);

	    return signaturesEqual && noteRatingsEqual;
	}
	
	@Override
	public int hashCode() {
		final int prime = 19;
		
		int result = 1;
		
		result = prime * result + ((chordSig==null) ? 0 : chordSig.hashCode());
		result = prime * result + ((noteRatings == null) ? 0 : noteRatings.hashCode());
		
		return result;
	}
}
