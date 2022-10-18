package chord.relations.persist;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used to persist chordchanges to xml file using JAXB.
 * @author DAD
 *
 */
@XmlRootElement(name="ChordChanges")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompleteChordChangeConsonance {

	private List<ChordChangeConsonance> chordChanges;
	
	public CompleteChordChangeConsonance() {
		super();
	}

	public List<ChordChangeConsonance> getChordChanges() {
		return chordChanges;
	}

	public void setChordChanges(List<ChordChangeConsonance> chordChanges) {
		this.chordChanges = chordChanges;
	}
}
