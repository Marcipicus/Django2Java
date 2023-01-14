package chord.relations.request;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import chord.ident.ScaleSignature;

public class ScaleRequest implements SimpleRequest<ScaleSignature>{

	public static ScaleRequest allScalesRequest() {
		ScaleRequest request = new ScaleRequest();
		request.addAll();
		return request;
	}

	private Set<ScaleSignature> scalesRequested;

	public ScaleRequest() {
		this.scalesRequested = new HashSet<>();
	}

	public void add(ScaleSignature... scaleSigs ) throws RequestInitializationException{
		if(scaleSigs == null) {
			throw new NullPointerException("intervals may not be null.");
		}
		if(scaleSigs.length == 0) {
			throw new RequestInitializationException("Must add at least one interval.");
		}
		Set<ScaleSignature> scaleSigsPassedSet = new HashSet<>(Arrays.asList(scaleSigs));

		if(scaleSigsPassedSet.contains(null)) {
			throw new RequestInitializationException("null scale signatures may not be added");
		}
		if(scaleSigs.length != scaleSigsPassedSet.size()) {
			throw new RequestInitializationException("Caller passed duplicate intervals....check your code"); 
		}

		this.scalesRequested = scaleSigsPassedSet;
	}

	/**
	 * Add all of the intervals in the first octave to the request.(UNISON to MAJOR7 inclusive.
	 */
	@Override
	public void addAll() {
		try {
			add(ScaleSignature.values());
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

	/**
	 * Test to see if the request is looking for the scale.
	 * @param scaleSignature scaleSignature being checked to see if requested.
	 * @return true if the request is looking for the scaleSignature.
	 */
	public boolean contains(ScaleSignature scaleSignature) {
		//we know that there are no nulls so we don't have to check the
		//method parameter for it.
		return this.scalesRequested.contains(scaleSignature);
	}
	
	@Override
	public int numberRequested() {
		return this.scalesRequested.size();
	}

	@Override
	public boolean isInitialized() {
		return this.scalesRequested.size() > 0;
	}

}
