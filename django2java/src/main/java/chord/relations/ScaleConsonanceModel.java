package chord.relations;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chord.ConsonanceRating;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;
import chord.relations.record.ScaleConsonanceRecord;
import chord.relations.request.ScaleConsonanceRecordRequest;

/**
 * Main data structure for rating the consonance of chords to scales.
 * @author DAD
 *
 */
public class ScaleConsonanceModel implements RatingModel<ScaleConsonanceRecord,ScaleConsonanceRecordRequest>{
	
	private Map<ChordSignature, Map<ScaleSignature,ConsonanceRating>> chordToScaleRatingMap;
	
	/**
	 * Create an empty ScaleConsonanceModel.
	 */
	public ScaleConsonanceModel() {
		this.chordToScaleRatingMap = new EnumMap<>(ChordSignature.class);
	}
	
	/**
	 * Add the rating for the given chord signature and scale signature
	 * @param chordSig the chord that the scale signature is being compared to
	 * @param scaleSig the scale signature being compared to the chord
	 * @param rating the rating of how consonant the chord and scale signatures are
	 * when compared to each other.
	 * @return the previous rating mapped to the chord and scale signatures,
	 * null if no previous rating exists.
	 */
	ConsonanceRating addRating(ChordSignature chordSig, ScaleSignature scaleSig, ConsonanceRating rating) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(scaleSig == null) {
			throw new NullPointerException("Scale Signature may not be null.");
		}
		if(rating == null) {
			throw new NullPointerException("Rating may not be null.");
		}
		Map<ScaleSignature,ConsonanceRating> ratingMapForChordSignature = 
				chordToScaleRatingMap.get(chordSig);

		if(ratingMapForChordSignature == null) {
			ratingMapForChordSignature = new EnumMap<>(ScaleSignature.class);
			chordToScaleRatingMap.put(chordSig, ratingMapForChordSignature);
		}
		
		return ratingMapForChordSignature.put(scaleSig,rating);
	}
	
	/**
	 * Remove the ConsonanceRating mapped to the given chordSignature and scale signature.
	 * @param chordSig ChordSignature from which the rating is being removed.
	 * @param scaleSig the scaleSignature from which the rating is being removed.
	 * @return the rating that was removed if successful,
	 * null if the rating does not exist.
	 */
	ConsonanceRating removeRating(ChordSignature chordSig, ScaleSignature scaleSig) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(scaleSig == null) {
			throw new NullPointerException("scale signature may not be null.");
		}

		Map<ScaleSignature,ConsonanceRating> ratingMapForChordSignature = 
				chordToScaleRatingMap.get(chordSig);

		return ratingMapForChordSignature == null? null : ratingMapForChordSignature.remove(scaleSig);
	}
	
	/**
	 * Retrieve the rating for the given chord signature and scaleSignature.
	 * @param chordSig Chord signature for which the rating is being retrieved
	 * @param scaleSig scaleSignature for which the rating is being retrieved
	 * @return the rating for the parameters if it exists, 
	 * null otherwise
	 */
	ConsonanceRating getRating(ChordSignature chordSig, ScaleSignature scaleSig) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(scaleSig == null) {
			throw new NullPointerException("Scale signature may not be null.");
		}
		
		Map<ScaleSignature,ConsonanceRating> ratingMapForChordSignature = 
				chordToScaleRatingMap.get(chordSig);

		return ratingMapForChordSignature == null? null : ratingMapForChordSignature.get(scaleSig);
	}
	
	/**
	 * Get a set of all chord signatures that have ratings. 
	 * @return set of all chord signatures with ratings.
	 */
	private Set<ChordSignature> getChordSignatureSet(){
		return this.chordToScaleRatingMap.keySet();
	}

	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof ScaleConsonanceModel))
			return false;
		
		ScaleConsonanceModel other = (ScaleConsonanceModel)o;
		
		this.purgeUnratedScales();
		other.purgeUnratedScales();
		
		return this.chordToScaleRatingMap.equals(other.chordToScaleRatingMap);
	}
	
	/**
	 * Find any chords with no scale ratings and remove them to simplify
	 * equals comparison and save/load methods.
	 */
	private void purgeUnratedScales() {
		List<ChordSignature> chordsWithNoRatings = new LinkedList<>();
		
		for(ChordSignature chordSig : this.chordToScaleRatingMap.keySet()) {
			Map<ScaleSignature,ConsonanceRating> scaleRatingMap = 
					this.chordToScaleRatingMap.get(chordSig);
			
			if(scaleRatingMap.size() == 0) {
				chordsWithNoRatings.add(chordSig);
			}
		}
		
		for(ChordSignature chordSig : chordsWithNoRatings) {
			this.chordToScaleRatingMap.remove(chordSig);
		}
	}

	@Override
	public ScaleConsonanceRecord addRating(ScaleConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}
		if(record.rating()==null) {
			throw new IllegalArgumentException("The rating field in record may not be null.");
		}
		ConsonanceRating rating = this.addRating(record.chordSignature(), record.scaleSignature(), record.rating());
		if(rating == null) {
			return null;
		}else {
			return new ScaleConsonanceRecord(record.chordSignature(),record.scaleSignature(),rating);
		}
	}

	@Override
	public ScaleConsonanceRecord removeRating(ScaleConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}
		ConsonanceRating rating = this.removeRating(record.chordSignature(), record.scaleSignature());
		if(rating == null) {
			return null;
		}else {
			return new ScaleConsonanceRecord(record.chordSignature(),record.scaleSignature(),rating);
		}
	}

	@Override
	public ScaleConsonanceRecord getRating(ScaleConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null.");
		}
		ConsonanceRating rating = this.getRating(record.chordSignature(), record.scaleSignature());
		if(rating == null) {
			return null;
		}else {
			return new ScaleConsonanceRecord(record.chordSignature(),record.scaleSignature(),rating);
		}
	}

	@Override
	public ScaleConsonanceRecord getNextRecordToBeRated() {
		if(isFull()) {
			return null;
		}
		for(ChordSignature chordSig : ChordSignature.values()) {
			for(ScaleSignature scaleSig : ScaleSignature.values()) {
				ConsonanceRating rating = getRating(chordSig,scaleSig);
				if(rating == null) {
					return new ScaleConsonanceRecord(chordSig, scaleSig, null);
				}
			}
		}

		//this return statement is redundant since we check to
		//see if the model is full at the start.
		//leaving it here so the code compiles
		return null;
	}

	@Override
	public ScaleConsonanceRecord getLastRecordRated() {
		if(isEmpty()) {
			return null;
		}
		ChordSignature chordSigOfPreviousRecord;
		ScaleSignature scaleSigOfPreviousRecord;
		ConsonanceRating ratingOfPreviousRecord;
		
		for(ChordSignature chordSig : ChordSignature.values()) {
			for(ScaleSignature scaleSig : ScaleSignature.values()) {
				ConsonanceRating rating = getRating(chordSig,scaleSig);
				if(rating == null) {
					if(chordSig.isFirstSignature() && scaleSig.isFirstSignature()) {
						return null;
					}
					if(scaleSig.isFirstSignature()) {
						chordSigOfPreviousRecord = chordSig.getPreviousChordSignature();
						scaleSigOfPreviousRecord = ScaleSignature.lastSignature();
						ratingOfPreviousRecord = this.getRating(chordSigOfPreviousRecord, scaleSigOfPreviousRecord);
						return new ScaleConsonanceRecord(
								chordSigOfPreviousRecord, 
								scaleSigOfPreviousRecord, 
								ratingOfPreviousRecord);
					}else {
						chordSigOfPreviousRecord = chordSig;
						scaleSigOfPreviousRecord = scaleSig.getPreviousScale();
						ratingOfPreviousRecord = this.getRating(chordSigOfPreviousRecord, scaleSigOfPreviousRecord);
					}
					
					return new ScaleConsonanceRecord(chordSigOfPreviousRecord, scaleSigOfPreviousRecord, ratingOfPreviousRecord);
				}
			}
		}
		//if we have iterated through the whole data structure then the
		//model must be full so we return the last possible record
		chordSigOfPreviousRecord = ChordSignature.lastSignature();
		scaleSigOfPreviousRecord = ScaleSignature.lastSignature();
		ratingOfPreviousRecord = this.getRating(chordSigOfPreviousRecord, scaleSigOfPreviousRecord);
		return new ScaleConsonanceRecord(
				chordSigOfPreviousRecord, 
				scaleSigOfPreviousRecord, 
				ratingOfPreviousRecord);
	}

	@Override
	public boolean isFull() {
		for(ChordSignature chordSig : ChordSignature.values()) {
			for(ScaleSignature scaleSig : ScaleSignature.values()) {
				ConsonanceRating rating = this.getRating(chordSig, scaleSig);
				if(rating == null) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		this.purgeUnratedScales();
		return this.chordToScaleRatingMap.keySet().size() == 0;
	}

	@Override
	public Set<ScaleConsonanceRecord> getRecords(ScaleConsonanceRecordRequest request) {
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		if( !request.isInitialized()) {
			throw new IllegalArgumentException("request is not initialized..check your code");
		}
		
		purgeUnratedScales();
		
		Set<ScaleConsonanceRecord> recordsRequested = new HashSet<>();
		
		for(ChordSignature chordSig : chordToScaleRatingMap.keySet()) {
			if(! request.containsReferenceChord(chordSig)) {
				continue;
			}
			
			Map<ScaleSignature,ConsonanceRating> scaleToRatingMap = chordToScaleRatingMap.get(chordSig);
			for(ScaleSignature scaleSig : scaleToRatingMap.keySet()) {
				if( !request.contains(scaleSig)) {
					continue;
				}
				
				ConsonanceRating rating = scaleToRatingMap.get(scaleSig);
				if( !request.contains(rating)) {
					continue;
				}
				ScaleConsonanceRecord requestedRecord = 
						new ScaleConsonanceRecord(
								chordSig, 
								scaleSig, 
								rating);
				
				recordsRequested.add(requestedRecord);
			}
			
			
		}

		return recordsRequested;
	}
}
