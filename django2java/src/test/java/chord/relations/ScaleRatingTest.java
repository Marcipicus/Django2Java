package chord.relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.ident.ScaleSignature;

class ScaleRatingTest {
	
	@Test
	void testNullPassedToConstructor() {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> new ScaleRating(null,ConsonanceRating.BAD));

		exception = 
				assertThrows(NullPointerException.class, 
						() -> new ScaleRating(ScaleSignature.AEOLIAN,null));
	}
	
	@Test
	void testGetScaleSignature() {
		final ScaleSignature scaleSig = ScaleSignature.AEOLIAN;
		ScaleRating rating = new ScaleRating(scaleSig,ConsonanceRating.GOOD);
		
		assertEquals(scaleSig,rating.getScaleSig());
	}
	
	@Test
	void testGetRating() {
		final ConsonanceRating rating = ConsonanceRating.BAD;
		ScaleRating scaleRating = new ScaleRating(ScaleSignature.AEOLIAN,rating);
		
		assertEquals(rating,scaleRating.getRating());
	}
	
	@Test
	void testEquals_Symmetric() {
		ScaleRating scaleRating1,scaleRating2;
		scaleRating1 = new ScaleRating(ScaleSignature.AEOLIAN,ConsonanceRating.BAD);
		scaleRating2 = new ScaleRating(ScaleSignature.AEOLIAN,ConsonanceRating.BAD);
		
		assertTrue(scaleRating1.equals(scaleRating2) && scaleRating2.equals(scaleRating1));
		assertTrue(scaleRating1.hashCode() == scaleRating2.hashCode());
	}
}
