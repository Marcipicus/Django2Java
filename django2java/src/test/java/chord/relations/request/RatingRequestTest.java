package chord.relations.request;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;

public class RatingRequestTest 
	extends AbstractSimpleRequestTest<
		ConsonanceRating, 
		RatingRequest> {

	@BeforeEach
	@Override
	void init() {
		request = new RatingRequest();
	}

	@Test
	@Override
	void testAddNullArray() {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> request.add(null));
	}

	@Test
	@Override
	void testAddArrayWithNull() {
		Exception exception = 
				assertThrows(RequestInitializationException.class, 
						() -> request.add(
								ConsonanceRating.VERY_BAD,
								ConsonanceRating.BAD,
								null));
	}

	@Test
	@Override
	void testAddArrayWithDuplicates() {
		Exception exception = 
				assertThrows(RequestInitializationException.class, 
						() -> request.add(
								ConsonanceRating.VERY_BAD,
								ConsonanceRating.BAD,
								ConsonanceRating.BAD));
	}

	@Test
	@Override
	void testAddArrayWithValidValues() throws RequestInitializationException {
		ConsonanceRating[] validValues = {
				ConsonanceRating.GOOD,
				ConsonanceRating.MEDIOCRE,
				ConsonanceRating.VERY_BAD
		};
		
		request.add(validValues);
		
		for(ConsonanceRating rating : validValues) {
			assertTrue(request.contains(rating));
		}
		
	}

	@Test
	@Override
	void testAddAll() {
		request.addAll();
		
		for(ConsonanceRating rating : ConsonanceRating.values()) {
			assertTrue(request.contains(rating));
		}
	}

	@Test
	@Override
	void testContains() throws RequestInitializationException {
		ConsonanceRating[] validValues = {
				ConsonanceRating.GOOD,
				ConsonanceRating.MEDIOCRE,
				ConsonanceRating.VERY_BAD
		};
		
		request.add(validValues);
		
		for(ConsonanceRating rating : validValues) {
			assertTrue(request.contains(rating));
		}
		
	}

}
