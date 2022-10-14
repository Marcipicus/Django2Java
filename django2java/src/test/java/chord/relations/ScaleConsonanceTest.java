package chord.relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;
import chord.relations.persist.P_ScaleConsonance;
import chord.relations.persist.P_ScaleRating;

class ScaleConsonanceTest {
	
	ScaleConsonance sc, filledScaleConsonance;
	P_ScaleConsonance p_sc;

	@BeforeEach
	void initializeScaleConsonanceVariables() {
		sc  = new ScaleConsonance(ChordSignature.b2);
		
		//Fill
		initializeFilledScaleConsonance();
		
		p_sc = ScaleConsonance.convertErrorCheckedToPersistableScaleConsonance(
				filledScaleConsonance);
	}

	void initializeFilledScaleConsonance() {
		filledScaleConsonance = new ScaleConsonance(ChordSignature.MAJOR);

		for(ScaleSignature scaleSig : ScaleSignature.values()){
			filledScaleConsonance.addScaleRating(
					new ScaleRating(scaleSig,ConsonanceRating.GOOD));
		}
	}
	
	@Test
	void testConstructorPassedNullValue() {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> new ScaleConsonance(null));
	}

	@Test
	void testCurrentScaleSignatureInitialized() {
		assertEquals(ScaleSignature.getFirstSignature(), sc.getCurrentSignature());
	}

	@Test
	void testRemoveNoteRatingWhenEmpty() {
		assertFalse(sc.removeLastScaleRating());
		assertTrue(sc.getScaleRatings().size() == 0);
		assertTrue(sc.getChordSig().equals(ChordSignature.b2));
		assertTrue(sc.getCurrentSignature().equals(ScaleSignature.getFirstSignature()));
	}
	
	@Test
	void testRemoveNoteRatingWhenPartiallyFull() {
		ScaleSignature sig0 = ScaleSignature.values()[0];
		ScaleSignature sig1 = ScaleSignature.values()[1];
		sc.addScaleRating(
				new ScaleRating(sig0, ConsonanceRating.BAD));
		
		assertEquals(sig1,sc.getCurrentSignature());
		
		assertTrue(sc.removeLastScaleRating());
		assertEquals(sig0, sc.getCurrentSignature());
		
	}

	@Test
	void testAddNoteRatingWhenFull() {
		assertTrue(sc.getCurrentSignature().equals(ScaleSignature.getFirstSignature()));

		for(ScaleSignature scaleSig : ScaleSignature.values()) {
			assertTrue(sc.addScaleRating(new ScaleRating(scaleSig, ConsonanceRating.BAD)));

			if(scaleSig.isLastSignature()) {
				break;
			}
			assertTrue(sc.getCurrentSignature().equals(scaleSig.getNextScale()));
		}

		assertFalse(sc.addScaleRating(
				new ScaleRating(ScaleSignature.getLastSignature(), ConsonanceRating.BAD)));
	}
	
	@Test
	void testConvertErrorCheckedConsonanceFileToPersisted(){
		P_ScaleConsonance p_consonance = 
				ScaleConsonance.convertErrorCheckedToPersistableScaleConsonance(
						filledScaleConsonance);
		
		assertEquals(filledScaleConsonance.getChordSig(),p_consonance.getChordSig());
		
		for(ScaleRating filledRating : filledScaleConsonance.getScaleRatings()) {
			boolean equalRatingDetected = false;
			for(P_ScaleRating p_rating : p_consonance.getScaleRatings()) {
				if(filledRating.getScaleSig().equals(p_rating.getScaleSig()) &&
						filledRating.getRating().equals(p_rating.getRating()));
				
				equalRatingDetected = true;
			}
			if( !equalRatingDetected) {
				fail();
			}
		}
	}
	
	@Test
	void testConvertPersistedConsonanceFileToErrorChecked() {
		ScaleConsonance convertedSc = 
				ScaleConsonance.convertPersistableConsonanceToErrorCheckedScaleConsonance(p_sc);
		
		assertEquals(p_sc.getChordSig(),convertedSc.getChordSig());
		
		for(P_ScaleRating p_rating : p_sc.getScaleRatings()) {
			boolean equalRatingDetected = false;
			for(ScaleRating convertedRating : convertedSc.getScaleRatings()) {
				
				if(p_rating.getRating().equals(convertedRating.getRating()) &&
						p_rating.getScaleSig().equals(convertedRating.getScaleSig())) {
					equalRatingDetected = true;
				}
			}
			if( !equalRatingDetected) {
				fail();
			}
		}
	}
	
	@Test
	void testSaveAndLoadScaleConsonanceModelFromFile() throws JAXBException {
		File testFile = new File("test.xml");
		
		ScaleConsonance.saveScaleConsonanceModelToFile(testFile, filledScaleConsonance);
		
		ScaleConsonance loadedSC = ScaleConsonance.loadScaleConsonanceModelFromFile(testFile);
	}
	
}
