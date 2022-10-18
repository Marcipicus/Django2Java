package chord.relations.persist;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used to persist chord note relations to
 * xml file using JAXB.
 * @author DAD
 *
 */
@XmlRootElement(name="NoteConsonance")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompleteNoteConsonance {
	private List<NoteConsonance> allChordNoteRelations;
	
	public CompleteNoteConsonance() {
		
	}

	public List<NoteConsonance> getAllChordNoteRelations() {
		return allChordNoteRelations;
	}

	public void setAllChordNoteRelations(List<NoteConsonance> allChordNoteRelations) {
		this.allChordNoteRelations = allChordNoteRelations;
	}
}
