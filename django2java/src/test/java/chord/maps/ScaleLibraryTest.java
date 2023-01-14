package chord.maps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import chord.NoteName;
import chord.Scale;
import chord.ident.ScaleSignature;

public class ScaleLibraryTest {

	@Test
	void testScaleRetrieval() {
		ScaleLibrary scaleLibrary = ScaleLibrary.getInstance();
		
		for(ScaleSignature scaleSig : ScaleSignature.values()) {
			for(NoteName rootNote : NoteName.values()) {
				Scale concreteScale = scaleLibrary.getScale(rootNote, scaleSig);
				
				assertEquals(rootNote,concreteScale.getRootNote());
				assertEquals(scaleSig,concreteScale.getScaleSignature());
			}
		}
	}
}
