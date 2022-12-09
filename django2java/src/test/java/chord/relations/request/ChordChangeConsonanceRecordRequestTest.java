package chord.relations.request;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.Interval;
import chord.ident.ChordSignature;

public class ChordChangeConsonanceRecordRequestTest
		extends AbstractRecordRequestTest<ChordChangeConsonanceRecordRequest> {

	@BeforeEach
	@Override
	void init() {
		request = new ChordChangeConsonanceRecordRequest();
	}
	
	@Test
	final void testAddTargetChordRequestNullRequest() {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> request.addTargetChordRequest(null));
	}
	
	@Test
	final void testAddTargetChordRequestUninitializedRequest() {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> request.addTargetChordRequest(new ChordRequest()));
	}
	
	@Test
	final void testAddTargetChordRequestValidValues() {
		ChordRequest chordRequest = new ChordRequest();
		chordRequest.addAll();
		
		request.addTargetChordRequest(chordRequest);
		
		for(ChordSignature chordSig : ChordSignature.values() ) {
			assertTrue(request.containsTargetChord(chordSig));
		}
	}
	
	@Test
	final void testAddIntervalRequestNullRequest() {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> request.addIntervalsBetweenRootsRequest(null));
	}
	
	@Test
	final void testAddIntervalRequestUninitializedRequest() {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> request.addIntervalsBetweenRootsRequest(new IntervalRequest()));
	}
	
	@Test
	final void testAddIntervalRequestValidValues() {
		IntervalRequest intervalRequest = new IntervalRequest();
		intervalRequest.addAll();
		
		request.addIntervalsBetweenRootsRequest(intervalRequest);
		
		for(Interval interval : Interval.valuesInFirstOctave() ) {
			assertTrue(request.containsIntervalBetweenRoots(interval));
		}
	}
	
	

	@Override
	void addAdditionalParameters() {
		ChordRequest targetChordRequest = new ChordRequest();
		targetChordRequest.addAll();
		request.addTargetChordRequest(targetChordRequest);
		
		IntervalRequest intervalsRequest = new IntervalRequest();
		intervalsRequest.addAll();
		request.addIntervalsBetweenRootsRequest(intervalsRequest);
	}

}
