package chord.relations.request;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.Interval;

public class NoteConsonanceRecordRequestTest 
	extends AbstractRecordRequestTest<NoteConsonanceRecordRequest>{

	@BeforeEach
	@Override
	void init() {
		request = new NoteConsonanceRecordRequest();
	}
	
	@Test
	final void testAddIntervalRequestNullRequest() {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> request.addIntervalRequest(null));
	}
	
	@Test
	final void testAddIntervalRequestUninitializedRequest() {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> request.addIntervalRequest(new IntervalRequest()));
	}
	
	@Test
	final void testAddIntervalRequestValidValues() {
		IntervalRequest intervalRequest = new IntervalRequest();
		intervalRequest.addAll();
		
		request.addIntervalRequest(intervalRequest);
		
		for(Interval interval : Interval.valuesInFirstOctave() ) {
			assertTrue(request.contains(interval));
		}
	}

	@Override
	void addAdditionalParameters() {
		IntervalRequest intervalRequest = new IntervalRequest();
		intervalRequest.addAll();
		request.addIntervalRequest(intervalRequest);
	}

}
