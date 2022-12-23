package chord.relations.request;

import chord.Interval;
import chord.ident.ChordSignature;

public class ChordChangeConsonanceRecordRequest extends AbstractRecordRequest {
	
	/**
	 * Get a request for all records that could possibly be contained 
	 * in a ChordChangeConsonanceModel.
	 * @return request for all possible values contained in a 
	 * ChordChangeConsonanceModel
	 */
	public static final ChordChangeConsonanceRecordRequest allPossibleRecords() {
		return new ChordChangeConsonanceRecordRequest(
				ChordRequest.allChordsRequest(),
				ChordRequest.allChordsRequest(),
				IntervalRequest.allIntervalsRequest(),
				RatingRequest.allRatingsRequest());
	}
	
	/**
	 * Get a request to retrieve all pleasant chord change ratings
	 * for the reference chord.
	 * @param referenceChord chord that scales are rated against
	 * @return all chord changes with a rating of GOOD or VERY_GOOD
	 * @throws RequestInitializationException if there is an error creating the request
	 */
	public static ChordChangeConsonanceRecordRequest allPleasantRatedRecordsForReferenceChord(ChordSignature referenceChord) throws RequestInitializationException {
		if(referenceChord == null) {
			throw new NullPointerException("referenceChord may not be null");
		}
		ChordRequest chordRequest = new ChordRequest(referenceChord);
		
		return new ChordChangeConsonanceRecordRequest(
				chordRequest,
				ChordRequest.allChordsRequest(),
				IntervalRequest.allIntervalsRequest(),
				RatingRequest.allPleasantRatingeRequest());
	}
	
	
	private ChordRequest targetChordRequest;
	private IntervalRequest intervalsBetweenRootsRequest;
	
	public ChordChangeConsonanceRecordRequest(
			ChordRequest referenceChordRequest,
			ChordRequest targetChordRequest,
			IntervalRequest intervalBewtweenRootsRequest,
			RatingRequest ratingRequest) {
		this();
		
		addReferenceChordRequest(referenceChordRequest);
		addTargetChordRequest(targetChordRequest);
		addIntervalsBetweenRootsRequest(intervalBewtweenRootsRequest);
		addRatingRequest(ratingRequest);
	}
	
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
		
		this.targetChordRequest = request;
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
