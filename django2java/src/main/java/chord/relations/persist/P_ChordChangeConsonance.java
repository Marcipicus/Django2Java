package chord.relations.persist;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import chord.Interval;
import chord.ident.ChordSignature;

@XmlRootElement(name="ChordChanges")
@XmlAccessorType(XmlAccessType.FIELD)
public class P_ChordChangeConsonance {
	
	private ChordSignature startSignature;
	private List<P_EndChordRatingList> endChordList;

	public P_ChordChangeConsonance() {
	}

	public ChordSignature getStartSignature() {
		return startSignature;
	}

	public void setStartSignature(ChordSignature startSignature) {
		this.startSignature = startSignature;
	}

	public List<P_EndChordRatingList> getEndChordList() {
		return endChordList;
	}

	public void setEndChordList(List<P_EndChordRatingList> endChordList) {
		this.endChordList = endChordList;
	}
	
	//TODO: Might have to change this method to look at displayText
	@Override
	public String toString() {
		return("StartChord:"+startSignature.toString());
	}

	@Override
	public boolean equals(Object o) {
	    if (o == this)
	        return true;
	    if (!(o instanceof P_ChordChangeConsonance))
	        return false;
	    P_ChordChangeConsonance other = (P_ChordChangeConsonance) o;
	    
	    boolean signaturesEqual, endChordListsEqual;
	    
	    signaturesEqual = this.startSignature.equals(other.startSignature);
	    endChordListsEqual = this.endChordList.equals(other.endChordList);

	    return signaturesEqual && endChordListsEqual;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + ((startSignature==null) ? 0 : startSignature.hashCode());
		result = prime * result + ((endChordList == null) ? 0 : endChordList.hashCode());
		
		return result;
	}

}
