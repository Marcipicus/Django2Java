package chord.relations.request;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import chord.Interval;

public class IntervalRequest implements SimpleRequest<Interval>{
	
	/**
	 * Get a request for all intervals in the first octave.
	 * @return
	 */
	public static final IntervalRequest allIntervalsRequest() {
		IntervalRequest request = new IntervalRequest();
		request.addAll();
		return request;
	}
	
	private Set<Interval> intervalsRequested;
	
	public IntervalRequest() {
		this.intervalsRequested = new HashSet<>();
	}
	
	public IntervalRequest(Interval... requestedValues) throws RequestInitializationException {
		this();
		add(requestedValues);
	}

	@Override
	public void add(Interval... requestedValues) throws RequestInitializationException {
		if(requestedValues == null) {
			throw new NullPointerException("intervals may not be null.");
		}
		if(requestedValues.length == 0) {
			throw new RequestInitializationException("Must add at least one interval.");
		}
		
		if( !Interval.allIntervalsInFirstOctave(requestedValues) ) {
			throw new RequestInitializationException("requestedValues passed are not in the first octaves, may also contain nulls");
		}
		Set<Interval> intervalsPassedSet = new HashSet<>(Arrays.asList(requestedValues));
		
		if(requestedValues.length != intervalsPassedSet.size()) {
			throw new RequestInitializationException("Caller passed duplicate intervals....check your code"); 
		}
		
		this.intervalsRequested = intervalsPassedSet;
	}

	/**
	 * Add all intervals in the first octave.
	 */
	@Override
	public void addAll() {
		try {
			add(Interval.valuesInFirstOctave());
		}catch(RequestInitializationException e) {
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
	public boolean isInitialized() {
		return this.intervalsRequested.size() > 0;
	}

	@Override
	public boolean contains(Interval value) {
		return this.intervalsRequested.contains(value);
	}

	@Override
	public int numberRequested() {
		return this.intervalsRequested.size();
	}

}
