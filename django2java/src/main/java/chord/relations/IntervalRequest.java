package chord.relations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import chord.Interval;

public class IntervalRequest implements SimpleRequest<Interval>{
	
	public static final IntervalRequest allIntervalsRequest() {
		IntervalRequest request = new IntervalRequest();
		request.addAll();
		return request;
	}
	
	private Set<Interval> intervalsRequested;
	
	public IntervalRequest() {
		this.intervalsRequested = new HashSet<>();
	}
	
	public IntervalRequest(Interval... requestedValues) throws ReqeustInitializationException {
		this();
		add(requestedValues);
	}

	@Override
	public void add(Interval... requestedValues) throws ReqeustInitializationException {
		if(requestedValues == null) {
			throw new NullPointerException("intervals may not be null.");
		}
		if(requestedValues.length == 0) {
			throw new ReqeustInitializationException("Must add at least one interval.");
		}
		
		if( !Interval.allIntervalsInFirstOctave(requestedValues) ) {
			throw new ReqeustInitializationException("requestedValues passed are not in the first octaves, may also contain nulls");
		}
		Set<Interval> intervalsPassedSet = new HashSet<>(Arrays.asList(requestedValues));
		
		if(requestedValues.length != intervalsPassedSet.size()) {
			throw new ReqeustInitializationException("Caller passed duplicate intervals....check your code"); 
		}
		
		this.intervalsRequested = intervalsPassedSet;
	}

	@Override
	public void addAll() {
		try {
			add(Interval.valuesInFirstOctave());
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
	public boolean isInitialized() {
		return this.intervalsRequested.size() > 0;
	}

	@Override
	public boolean contains(Interval value) {
		return this.intervalsRequested.contains(value);
	}

}
