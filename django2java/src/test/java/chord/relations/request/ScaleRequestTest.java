package chord.relations.request;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ident.ScaleSignature;

public class ScaleRequestTest 
extends AbstractSimpleRequestTest<
ScaleSignature,
ScaleRequest>{

	@BeforeEach
	@Override
	void init() {
		request = new ScaleRequest();
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
								ScaleSignature.AEOLIAN,
								ScaleSignature.MIXOLYDIAN, 
								null));
	}

	@Test
	@Override
	void testAddArrayWithDuplicates() {
		Exception exception = 
				assertThrows(RequestInitializationException.class, 
						() -> request.add(
								ScaleSignature.AEOLIAN,
								ScaleSignature.MIXOLYDIAN, 
								ScaleSignature.MIXOLYDIAN));
	}

	@Test
	@Override
	void testAddArrayWithValidValues() throws RequestInitializationException {
		ScaleSignature[] validScaleSigs = {
				ScaleSignature.AEOLIAN,
				ScaleSignature.MIXOLYDIAN, 
				ScaleSignature.MIXODORIAN};
		request.add(validScaleSigs);
		for(ScaleSignature scaleAdded : validScaleSigs) {
			assertTrue(request.contains(scaleAdded));
		}
	}

	@Test
	@Override
	void testAddAll() {
		request.addAll();

		for(ScaleSignature scaleSig : ScaleSignature.values()) {
			assertTrue(request.contains(scaleSig));
		}
	}

	@Test
	@Override
	void testContains() throws RequestInitializationException {
		ScaleSignature[] validScaleSigs = {
				ScaleSignature.AEOLIAN,
				ScaleSignature.MIXOLYDIAN, 
				ScaleSignature.MIXODORIAN};
		request.add(validScaleSigs);
		for(ScaleSignature scaleAdded : validScaleSigs) {
			assertTrue(request.contains(scaleAdded));
		}
	}

}
