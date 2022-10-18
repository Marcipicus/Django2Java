package chord.relations.persist;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class used to persist chord changes to xml 
 * file using JAXB.
 * @author DAD
 *
 */
@XmlRootElement(name="ScaleConsonance")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompleteScaleConsonance {
	
	private List<ScaleConsonance> allChordScaleRelations;
	
	public CompleteScaleConsonance() {
		
	}

	public List<ScaleConsonance> getAllChordScaleRelations() {
		return allChordScaleRelations;
	}

	public void setAllChordScaleRelations(List<ScaleConsonance> allChordScaleRelations) {
		this.allChordScaleRelations = allChordScaleRelations;
	}
}
