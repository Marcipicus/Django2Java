package chord.relations;

import chord.Interval;
import chord.ident.ChordSignature;

public class ChordChangeConsonanceRecordRequest extends AbstractRecordRequest {
	
	private ChordRequest targetChordRequest;
	private IntervalRequest intervalsBetweenRootsRequest;
	
	public ChordChangeConsonanceRecordRequest() {
		super();
		targetChordRequest = new ChordRequest();
		intervalsBetweenRootsRequest = new IntervalRequest();
	}
	
	public void addTargetChordRequest(ChordRequest request) {
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		if( !request.isInitialized()) {
			throw new IllegalArgumentException("Request has not been initialized.");
		}
		
		this.referenceChordRequest = request;
	}
	
	public void addIntervalsBetweenRootsRequest(IntervalRequest request) {
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		if( !request.isInitialized() ) {
			throw new IllegalArgumentException();
		}
		
		this.intervalsBetweenRootsRequest = request;
	}
	
	public boolean containsTargetChord(ChordSignature targetChord) {
		if(targetChord == null) {
			throw new NullPointerException("targetChord may not be null");
		}
		
		return targetChordRequest.contains(targetChord);
	}
	
	public boolean containsIntervalBetweenRoots(Interval interval) {
		if(interval == null) {
			throw new NullPointerException("interval may not be null.");
		}
		if(!interval.inFirstOctave()) {
			throw new IllegalArgumentException("Ratings do not include intervals past MAJOR7");
		}
		
		return intervalsBetweenRootsRequest.contains(interval);
	}

	@Override
	protected boolean allAditionalParametersInitialized() {
		return targetChordRequest.isInitialized() && 
				intervalsBetweenRootsRequest.isInitialized();
	}

}
