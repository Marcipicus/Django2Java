package chord.relations.request;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ident.ScaleSignature;

public class ScaleConsonanceRecordRequestTest extends AbstractRecordRequestTest<ScaleConsonanceRecordRequest> {

	@BeforeEach
	@Override
	void init() {
		request = new ScaleConsonanceRecordRequest();
	}
	
	
	@Test
	final void testAddScaleRequestNullRequest() {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> request.addScaleRequest(null));
	}
	
	@Test
	final void testAddScaleRequestUninitializedRequest() {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> request.addScaleRequest(new ScaleRequest()));
	}
	
	@Test
	final void testAddScaleRequestValidValues() {
		ScaleRequest scaleRequest = new ScaleRequest();
		scaleRequest.addAll();
		
		request.addScaleRequest(scaleRequest);
		
		for(ScaleSignature scaleSig : ScaleSignature.values() ) {
			assertTrue(request.contains(scaleSig));
		}
	}

	@Override
	void addAdditionalParameters() {
		ScaleRequest scaleRequest = new ScaleRequest();
		scaleRequest.addAll();
		
		request.addScaleRequest(scaleRequest);
	}

}
