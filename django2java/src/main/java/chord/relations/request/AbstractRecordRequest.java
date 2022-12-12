package chord.relations.request;

import chord.ConsonanceRating;
import chord.ident.ChordSignature;

public abstract class AbstractRecordRequest {
	
	protected ChordRequest referenceChordRequest;
	protected RatingRequest ratingRequest;
	
	public AbstractRecordRequest() {
		referenceChordRequest = new ChordRequest();
		ratingRequest = new RatingRequest();
	}
	
	public final void addReferenceChordRequest(ChordRequest request) {
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		if( !request.isInitialized()) {
			throw new IllegalArgumentException("Request has not been initialized.");
		}
		
		this.referenceChordRequest = request;
	}
	
	public final void addRatingRequest(RatingRequest request) {
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		if( !request.isInitialized() ) {
			throw new IllegalArgumentException();
		}
		
		this.ratingRequest = request;
	}

	/**
	 * Test to see if the request is looking for the chord signature.
	 * @param referenceChordSig chordSignature being checked to see if it is requested.
	 * This refers to the chord being compared to(startChord in ChordChangeConsonanceModel)
	 * @return true if the request is looking for the chord signature
	 */
	public final boolean containsReferenceChord(ChordSignature referenceChordSig) {
		//we know that there are no nulls so we don't have to check the
		//method parameter for it.
		return this.referenceChordRequest.contains(referenceChordSig);
	}
	
	/**
	 * Test to see if the request is looking for the rating.
	 * @param rating rating being tested to see if requested
	 * @return true if the request is looking for the rating
	 */
	public final boolean contains(ConsonanceRating rating) {
		//we know that there are no nulls so we don't have to check the
		//method parameter for it.
		return this.ratingRequest.contains(rating);
	}
	
	
	/**
	 * Test to see if the request is ready to use and valid.
	 * Specifically check to see that there is at least one
	 * entry in each of the request parameters.
	 * 
	 * This method exists to simplify debugging. 
	 * @return true if each of the request parameters contain
	 * at least one value.
	 */
	public final boolean isInitialized() {
		if(referenceChordRequest == null || ratingRequest == null) {
			return false;
		}
		return 
				this.referenceChordRequest.isInitialized() &&
				this.ratingRequest.isInitialized() &&
				allAditionalParametersInitialized();

	}
	
	/**
	 * Check to see if all of the parameters in subclasses have
	 * been initialized.
	 * @return true if additional parameters have been initialized
	 * and the request is ready to be used
	 */
	protected abstract boolean allAditionalParametersInitialized();
}
