package chord.relations.persist;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import chord.ident.ChordSignature;

@XmlRootElement(name="ScaleConsonance")
@XmlAccessorType(XmlAccessType.FIELD)
public class P_ScaleConsonance {
	private ChordSignature chordSig;
	private List<P_ScaleRating> scaleRatings;

	public P_ScaleConsonance() {
	}

	public ChordSignature getChordSig() {
		return chordSig;
	}

	public void setChordSig(ChordSignature chordSig) {
		this.chordSig = chordSig;
	}

	public List<P_ScaleRating> getScaleRatings() {
		return scaleRatings;
	}

	public void setScaleRatings(List<P_ScaleRating> scaleRatings) {
		this.scaleRatings = scaleRatings;
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
	    if (!(o instanceof P_ScaleConsonance))
	        return false;
	    P_ScaleConsonance other = (P_ScaleConsonance) o;
	    
	    boolean signaturesEqual, scaleRatingsEqual;
	    
	    signaturesEqual = this.chordSig.equals(other.chordSig);
	    scaleRatingsEqual = this.scaleRatings.equals(other.scaleRatings);

	    return signaturesEqual && scaleRatingsEqual;
	}
	
	@Override
	public int hashCode() {
		final int prime = 23;
		
		int result = 1;
		
		result = prime * result + ((chordSig==null) ? 0 : chordSig.hashCode());
		result = prime * result + ((scaleRatings == null) ? 0 : scaleRatings.hashCode());
		
		return result;
	}
}
