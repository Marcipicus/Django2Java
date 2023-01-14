package chord.relations.request;

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
	
	public ChordRequest(ChordSignature... requestedValues) throws RequestInitializationException {
		this();
		add(requestedValues);
	}

	@Override
	public void add(ChordSignature... requestedValues) throws RequestInitializationException {
		if(requestedValues == null) {
			throw new NullPointerException("chordSigs may not be null");
		}
		if(requestedValues.length == 0) {
			throw new RequestInitializationException("Must add at least one chord signature");
		}

		Set<ChordSignature> chordSigsPassedSet = new HashSet<>(Arrays.asList(requestedValues));

		if(chordSigsPassedSet.contains(null)) {
			throw new RequestInitializationException("addChordSignature does not accept null values");
		}
		
		if(requestedValues.length != chordSigsPassedSet.size()) {
			throw new RequestInitializationException("Caller passed duplicate chord signatures...Check your code");
		}

		this.chordsRequested = chordSigsPassedSet;
	}

	@Override
	public void addAll() {
		try {
			add(ChordSignature.values());
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
	public boolean contains(ChordSignature value) {
		//we know that there are no nulls so we don't have to check the
		//method parameter for it.
		return this.chordsRequested.contains(value);
	}
	
	@Override
	public boolean isInitialized() {
		return this.chordsRequested.size() > 0;
	}

	@Override
	public int numberRequested() {
		return this.chordsRequested.size();
	}
}
