package chord.relations.request;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.ident.ChordSignature;

/**
 * Class to test the base functionality of the Abstract
 * Record Request
 * @author DAD
 *
 */
public abstract class AbstractRecordRequestTest<REQUEST_TYPE extends AbstractRecordRequest> {

	REQUEST_TYPE request;
	
	/**
	 * Call the request default constructor.
	 */
	@BeforeEach
	abstract void init();
	
	@Test
	final void testAddReferenceChordRequestNullRequest() {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> request.addReferenceChordRequest(null));
	}
	
	@Test
	final void testAddReferenceChordRequestUninitializedRequest() {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> request.addReferenceChordRequest(new ChordRequest()));
	}
	
	@Test
	final void testAddReferenceChordRequestValidValues() {
		ChordRequest chordRequest = new ChordRequest();
		chordRequest.addAll();
		
		request.addReferenceChordRequest(chordRequest);
		
		for(ChordSignature chordSig : ChordSignature.values() ) {
			assertTrue(request.contains(chordSig));
		}
	}
	
	@Test
	final void testAddRatingRequestNullRequest() {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> request.addRatingRequest(null));
	}
	
	@Test
	final void testAddRatingRequestUninitializedRequest() {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> request.addRatingRequest(new RatingRequest()));
	}
	
	@Test
	final void testAddRatingRequestValidValues() {
		RatingRequest ratingRequest = new RatingRequest();
		ratingRequest.addAll();
		
		request.addRatingRequest(ratingRequest);
		
		for(ConsonanceRating rating : ConsonanceRating.values() ) {
			assertTrue(request.contains(rating));
		}
	}
	
	@Test
	final void testIsInitialized() {
		RatingRequest ratingRequest = new RatingRequest();
		ChordRequest chordRequest  = new ChordRequest();
		
		assertFalse(request.isInitialized());
		
		ratingRequest.addAll();
		request.addRatingRequest(ratingRequest);
		assertFalse(request.isInitialized());
		
		chordRequest.addAll();
		request.addReferenceChordRequest(chordRequest);
		assertFalse(request.isInitialized());
		
		addAdditionalParameters();
		assertTrue(request.isInitialized());
	}
	
	/**
	 * Add any additional parameters required by the
	 * request to complete it's initialization
	 */
	abstract void addAdditionalParameters();
	
}
