package chord.relations.request;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ident.ChordSignature;

public class ChordRequestTest extends AbstractSimpleRequestTest<ChordSignature, ChordRequest> {

	@BeforeEach
	@Override
	void init() {
		request = new ChordRequest();
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
								ChordSignature.MAJOR,
								ChordSignature.MINOR,
								null));
	}

	@Test
	@Override
	void testAddArrayWithDuplicates() {
		Exception exception = 
				assertThrows(RequestInitializationException.class, 
						() -> request.add(
								ChordSignature.MAJOR,
								ChordSignature.MINOR,
								ChordSignature._10,
								ChordSignature.MAJOR));
	}

	@Test
	@Override
	void testAddArrayWithValidValues() throws RequestInitializationException {
		ChordSignature[] validValues = {
				ChordSignature.MAJOR,
				ChordSignature.MINOR,
				ChordSignature._10};
		request.add(validValues);
		
		for(ChordSignature passedChordSig : validValues) {
			assertTrue(request.contains(passedChordSig));
		}
	}

	@Test
	@Override
	void testAddAll() {
		request.addAll();
		
		for(ChordSignature passedChordSig : ChordSignature.values()) {
			assertTrue(request.contains(passedChordSig));
		}
	}

	@Test
	@Override
	void testContains() throws RequestInitializationException {
		ChordSignature[] validValues = {
				ChordSignature.MAJOR,
				ChordSignature.MINOR,
				ChordSignature._10};
		request.add(validValues);
		
		for(ChordSignature passedChordSig : validValues) {
			assertTrue(request.contains(passedChordSig));
		}
	}

}
