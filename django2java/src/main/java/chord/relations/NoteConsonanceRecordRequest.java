package chord.relations;

import chord.Interval;
import chord.ident.ChordSignature;

/**
 * Class used as a request parameter to the NoteConsonanceModel.
 * 
 * To use...
 * 1.Create the Request using the no-args constructor
 * 2.call each of the three add methods and make sure 
 * that there are no duplicated arguments
 * 3.pass the instance to the NoteConsonanceModel.getRecords(param) method
 * 
 * MAKE SURE THAT YOU DO NOT PASS ANY NULLS OR DUPLICATE PARAMETERS TO
 * ANY OF THE ADD METHODS..THERE WILL BE AN EXCEPTION IF YOU DO.
 * YOU ALSO HAVE TO INITIALIZE ALL 3 PARAMETERS.
 * 
 * @author DAD
 *
 */
public class NoteConsonanceRecordRequest extends AbstractRecordRequest{


	/**
	 * Request to return all records that exist.
	 * @return Request for all records that exist
	 */
	public static NoteConsonanceRecordRequest allExistingRatingsRequest() {
		return new NoteConsonanceRecordRequest(
				ChordRequest.allChordsRequest(),
				IntervalRequest.allIntervalsRequest(),
				RatingRequest.allRatingsRequest());
	}

	/**
	 * Request all records with the given chord sig and a rating of GOOD Or VERY_GOOD
	 * @param chordSig chord signature that we are requesting records for, may not be null
	 * @return request for all records that have the chord signature and ratings of
	 * GOOD or VERY_GOOD
	 * @throws ReqeustInitializationException 
	 */
	public static NoteConsonanceRecordRequest allHighRatedIntervalRequest(ChordSignature chordSig) throws ReqeustInitializationException {
		if(chordSig == null) {
			throw new NullPointerException("chordSig may not be null");
		}
		
		ChordRequest chordRequest = new ChordRequest(chordSig);
		
		return new NoteConsonanceRecordRequest(
				chordRequest,
				IntervalRequest.allIntervalsRequest(),
				RatingRequest.allPleasantRatingeRequest());
	}

	private IntervalRequest intervalRequest;

	/**
	 * Create a new NoteConsonanceRecordRequest. The 
	 * request is not ready to use until the chords,
	 * intervals, and ratings have at least one value
	 * added to the records requested.
	 */
	public NoteConsonanceRecordRequest() {
		super();
		intervalRequest = new IntervalRequest();
	}
	
	/**
	 * Create the request with the given parameters.
	 * @param referenceChordsRequest reference Chords Requested
	 * @param intervalRequest intervals requested
	 * @param ratingsRequest ratings requested.
	 */
	public NoteConsonanceRecordRequest(
			ChordRequest referenceChordsRequest, 
			IntervalRequest intervalRequest, 
			RatingRequest ratingsRequest) {
		this();
		
		addReferenceChordRequest(referenceChordsRequest);
		addIntervalRequest(intervalRequest);
		addRatingRequest(ratingsRequest);
	}
	
	public void addIntervalRequest(IntervalRequest request) {
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		if( !request.isInitialized()) {
			throw new IllegalArgumentException("request has not been initialized");
		}
		
		this.intervalRequest = request;
	}

	/**
	 * Test to see if the request is looking for the interval.
	 * @param interval interval being checked to see if requested.
	 * @return true if the request is looking for the interval.
	 */
	public boolean contains(Interval interval) {
		//we know that there are no nulls so we don't have to check the
		//method parameter for it.
		return this.intervalRequest.contains(interval);
	}

	@Override
	protected boolean allAditionalParametersInitialized() {
		if(intervalRequest == null) {
			return false;
		}
		return this.intervalRequest.isInitialized();
	}
	
}
