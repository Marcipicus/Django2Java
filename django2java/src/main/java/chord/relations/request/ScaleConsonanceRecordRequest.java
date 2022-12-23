package chord.relations.request;

import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;

public class ScaleConsonanceRecordRequest extends AbstractRecordRequest{
	
	/**
	 * Get a request that will retrieve all possible records in the
	 * model. A new record is created every time so modify at will.
	 * 
	 * @return request that will retrieve all possible records in the
	 * ScaleConsonanceModel
	 */
	public static ScaleConsonanceRecordRequest allPossibleRecords() {
		return new ScaleConsonanceRecordRequest(
				ChordRequest.allChordsRequest(),
				ScaleRequest.allScalesRequest(),
				RatingRequest.allRatingsRequest());
	}
	
	/**
	 * Get a request to retrieve all pleasant scale ratings
	 * for the reference chord.
	 * @param referenceChord chord that scales are rated against
	 * @return all chord/scale records with a rating of GOOD or VERY_GOOD
	 * @throws RequestInitializationException if there is an error creating the request
	 */
	public static ScaleConsonanceRecordRequest allPleasantRatedRecordsForReferenceChord(ChordSignature referenceChord) throws RequestInitializationException {
		if(referenceChord == null) {
			throw new NullPointerException("referenceChord may not be null");
		}
		ChordRequest chordRequest = new ChordRequest(referenceChord);
		
		return new ScaleConsonanceRecordRequest(
				chordRequest,
				ScaleRequest.allScalesRequest(),
				RatingRequest.allPleasantRatingeRequest());
	}
	
	
	private ScaleRequest scaleRequest;
	
	/**
	 * Create the request with the given parameters.
	 * @param referenceChordsRequest reference Chords Requested
	 * @param intervalRequest intervals requested
	 * @param ratingsRequest ratings requested.
	 */
	public ScaleConsonanceRecordRequest(
			ChordRequest referenceChordsRequest, 
			ScaleRequest scaleRequest, 
			RatingRequest ratingsRequest) {
		this();
		
		addReferenceChordRequest(referenceChordsRequest);
		addScaleRequest(scaleRequest);
		addRatingRequest(ratingsRequest);
	}
	
	
	public ScaleConsonanceRecordRequest() {
		super();
		scaleRequest = new ScaleRequest();
	}
	
	public void addScaleRequest(ScaleRequest request){
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		if( !request.isInitialized()) {
			throw new IllegalArgumentException("request has not been initialized.");
		}
		this.scaleRequest = request;
	}

	/**
	 * Test to see if the request is looking for the interval.
	 * @param interval interval being checked to see if requested.
	 * @return true if the request is looking for the interval.
	 */
	public boolean contains(ScaleSignature scaleSignature) {
		//we know that there are no nulls so we don't have to check the
		//method parameter for it.
		return this.scaleRequest.contains(scaleSignature);
	}

	@Override
	protected boolean allAditionalParametersInitialized() {
		if(scaleRequest == null) {
			return false;
		}
		return this.scaleRequest.isInitialized();
	}
	
	
}
