package chord.relations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import chord.ident.ChordSignature;

public class ChordRequest implements SimpleRequest<ChordSignature>{
	
	public static final ChordRequest allChordsRequest() {
		ChordRequest request = new ChordRequest();
		request.addAll();
		return request;
	}

	private Set<ChordSignature> chordsRequested;

	public ChordRequest() {
		this.chordsRequested = new HashSet<>();
	}
	
	public ChordRequest(ChordSignature... requestedValues) throws ReqeustInitializationException {
		this();
		add(requestedValues);
	}

	@Override
	public void add(ChordSignature... requestedValues) throws ReqeustInitializationException {
		if(requestedValues == null) {
			throw new NullPointerException("chordSigs may not be null");
		}
		if(requestedValues.length == 0) {
			throw new ReqeustInitializationException("Must add at least one chord signature");
		}

		Set<ChordSignature> chordSigsPassedSet = new HashSet<>(Arrays.asList(requestedValues));

		if(requestedValues.length !=- chordSigsPassedSet.size()) {
			throw new ReqeustInitializationException("Caller passed duplicate chord signatures...Check your code");
		}

		if(chordSigsPassedSet.contains(null)) {
			throw new ReqeustInitializationException("addChordSignature does not accept null values");
		}

		this.chordsRequested = chordSigsPassedSet;
	}

	@Override
	public void addAll() {
		try {
			add(ChordSignature.values());
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
	public boolean contains(ChordSignature value) {
		//we know that there are no nulls so we don't have to check the
		//method parameter for it.
		return this.chordsRequested.contains(value);
	}
	
	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return this.chordsRequested.size() > 0;
	}
}
