package chord.relations.request;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.Interval;

public class IntervalRequestTest 
	extends AbstractSimpleRequestTest<
			Interval,
			IntervalRequest> {

	@BeforeEach
	@Override
	void init() {
		request = new IntervalRequest();
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
						() -> request.add(Interval.UNISON,Interval.MINOR2,null));
	}

	@Test
	@Override
	void testAddArrayWithDuplicates() {
		Exception exception = 
				assertThrows(RequestInitializationException.class, 
						() -> request.add(Interval.UNISON,Interval.MINOR2,Interval.MINOR2));
	}
	
	@Test
	void testAddInvalidIntervals() {
		Exception exception = 
				assertThrows(RequestInitializationException.class, 
						() -> request.add(Interval.UNISON,Interval.MINOR2,Interval.MAJOR10));
	}

	@Test
	@Override
	void testAddArrayWithValidValues() throws RequestInitializationException {
		Interval[] validIntervals = {Interval.UNISON,Interval.MINOR2,Interval.MAJOR2};
		request.add(validIntervals);
		
		for(Interval passedInterval : validIntervals) {
			assertTrue(request.contains(passedInterval));
		}
	}

	@Test
	@Override
	void testAddAll() {
		request.addAll();
		for(Interval interval : Interval.values()) {
			if(interval.inFirstOctave()) {
				assertTrue(request.contains(interval));
			}else {
				assertFalse(request.contains(interval));
			}
		}
	}

	//I know this is just a duplicate of the addValidValues but
	//put it here anyways....should I remove it? Let me know.
	@Test
	@Override
	void testContains()  throws RequestInitializationException {
		Interval[] validIntervals = {Interval.UNISON,Interval.MINOR2,Interval.MAJOR2};
		request.add(validIntervals);
		
		for(Interval passedInterval : validIntervals) {
			assertTrue(request.contains(passedInterval));
		}
	}

}
