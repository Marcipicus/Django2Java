package chord.relations.request;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import chord.ConsonanceRating;

public class RatingRequest implements SimpleRequest<ConsonanceRating>{
	
	public static final RatingRequest allRatingsRequest() {
		RatingRequest request = new RatingRequest();
		request.addAll();
		return request;
	}
	
	/**
	 * Get a rating request with GOOD and VERY GOOD
	 * @return
	 */
	public static final RatingRequest allPleasantRatingeRequest() {
		RatingRequest request = new RatingRequest();
		try {
			return new RatingRequest(ConsonanceRating.GOOD,ConsonanceRating.VERY_GOOD);
		}catch(ReqeustInitializationException e) {
			//I know this won't happen so I am not 
			//notifying callers with the exception
			//assignment exists to avoid compilere errors
			return null;
		}
	}
	
	private Set<ConsonanceRating> ratingsRequested;
	
	public RatingRequest() {
		this.ratingsRequested = new HashSet<>();
	}
	
	public RatingRequest(ConsonanceRating... requestedValues ) throws ReqeustInitializationException {
		this();
		add(requestedValues);
	}
	
	@Override
	public void add(ConsonanceRating... requestedValues) throws ReqeustInitializationException {
		if(requestedValues == null) {
			throw new NullPointerException("consonanceRatings may not be null");
		}
		if(requestedValues.length == 0) {
			throw new ReqeustInitializationException("Must add at least one rating");
		}

		Set<ConsonanceRating> passedRatings = new HashSet<>(Arrays.asList(requestedValues));
		if(passedRatings.contains(null)) {
			throw new ReqeustInitializationException("addRatings cannot accept null values.");
		}
		if(requestedValues.length != passedRatings.size()) {
			throw new ReqeustInitializationException("Caller passed duplicate ConsonanceRatings...Check your code");
		}
		
		this.ratingsRequested = passedRatings;
	}

	@Override
	public void addAll() {
		try {
			add(ConsonanceRating.values());

		}catch(ReqeustInitializationException e) {
			//parameters are formed properly so we don't have to
			//worry about this......ummm yeah I know I shouldn't
			//do this but the unit tests should catch any errors
			//in the method called and I'm sure that
			//you will agree that you are happy that you don't have
			//piss around with your own try catch block
			//
			//
			//You're welcome
		}
	}
	
	@Override
	public final boolean contains(ConsonanceRating rating) {
		//we know that there are no nulls so we don't have to check the
		//method parameter for it.
		return this.ratingsRequested.contains(rating);
	}

	@Override
	public boolean isInitialized() {
		return	this.ratingsRequested.size() > 0 ;
	}
}
