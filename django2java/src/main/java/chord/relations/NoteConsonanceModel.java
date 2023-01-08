package chord.relations;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.record.NoteConsonanceRecord;
import chord.relations.request.NoteConsonanceRecordRequest;

/**
 * Main data model for rating the consonance of chord signatures to
 * single notes. The relationship between the notes and chords is defined
 * by the interval between the root note of the chord and the note that is
 * being rated.
 */
public class NoteConsonanceModel implements RatingModel<NoteConsonanceRecord,NoteConsonanceRecordRequest>{

	/**
	 * Main data structure containing the consonance rating between
	 * chords and notes represented by intervals.
	 */
	private Map<ChordSignature, IntervalRatingMap> chordToIntervalRatingMap;

	/**
	 * Create an empty NoteConsonanceModel.
	 */
	public NoteConsonanceModel() {
		this.chordToIntervalRatingMap = new EnumMap<>(ChordSignature.class);
	}

	/**
	 * Add the rating for the given chord signature and interval.
	 * @param chordSig the chord that the interval is being compared to.
	 * @param interval the interval between the root note of the chord to note whose
	 * consonance is being rated
	 * @param rating the rating of how consonant the chord is.
	 * @return the previous rating that existed for the chord signature and interval,
	 * null if no previous rating exists.
	 */
	ConsonanceRating addRating(
			ChordSignature chordSig, 
			Interval interval, 
			ConsonanceRating rating) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(interval == null) {
			throw new NullPointerException("Interval may not be null.");
		}
		if(!interval.inFirstOctave()) {
			throw new IllegalArgumentException("Interval must be between UNISON and MAJOR7 inclusive.");
		}
		if(rating == null) {
			throw new NullPointerException("Rating may not be null.");
		}

		IntervalRatingMap ratingMapForChordSignature = 
				chordToIntervalRatingMap.get(chordSig);

		if(ratingMapForChordSignature == null) {
			ratingMapForChordSignature = new IntervalRatingMap();
			chordToIntervalRatingMap.put(chordSig, ratingMapForChordSignature);
		}

		return ratingMapForChordSignature.put(interval,rating);
	}

	/**
	 * Remove the ConsonanceRating mapped to the given chordSignature and interval.
	 * @param chordSig ChordSignature from which the rating is being removed.
	 * @param interval the interval from which the rating is being removed.
	 * @return the rating that was removed if successful,
	 * null if the rating does not exist.
	 */
	ConsonanceRating removeRating(ChordSignature chordSig, Interval interval) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(interval == null) {
			throw new NullPointerException("Interval may not be null.");
		}
		if(!interval.inFirstOctave()) {
			throw new IllegalArgumentException("Interval must be between UNISON and MAJOR7 inclusive.");
		}

		IntervalRatingMap ratingMapForChordSignature = 
				chordToIntervalRatingMap.get(chordSig);

		return ratingMapForChordSignature == null? null : ratingMapForChordSignature.remove(interval);
	}

	/**
	 * Retrieve the rating for the given chord signature and interval.
	 * @param chordSig Chord signature for which the interval is rated
	 * @param interval interval between the root note of the chord and the note whose
	 * rating we are retrieving. 
	 * @return the rating for the parameters if it exists, 
	 * null otherwise
	 */
	ConsonanceRating getRating(ChordSignature chordSig, Interval interval) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(interval == null) {
			throw new NullPointerException("Interval may not be null.");
		}
		if(!interval.inFirstOctave()) {
			throw new IllegalArgumentException("Interval must be between UNISON and MAJOR7 inclusive.");
		}

		IntervalRatingMap ratingMapForChordSignature = 
				chordToIntervalRatingMap.get(chordSig);

		return ratingMapForChordSignature == null? null : ratingMapForChordSignature.get(interval);
	}

	/**
	 * Get a set of all chord signatures that have ratings. 
	 * @return set of all chord signatures with ratings.
	 */
	private Set<ChordSignature> getChordSignatureSet(){
		return this.chordToIntervalRatingMap.keySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof NoteConsonanceModel))
			return false;

		NoteConsonanceModel other = (NoteConsonanceModel)o;

		//Because of removal of ratings there may be unfilled
		//rating maps in the data structure
		this.purgeMapsWithoutRatings();
		other.purgeMapsWithoutRatings();

		return this.chordToIntervalRatingMap.equals(other.chordToIntervalRatingMap);
	}

	/**
	 * Remove any intervalMaps for chord signatures with no ratings.
	 * 
	 * Thisis used to simplify the equals method and save/load methods.
	 */
	private void purgeMapsWithoutRatings() {
		List<ChordSignature> chordSignaturesWithoutRatings = new LinkedList<>();

		for(ChordSignature chordSig : chordToIntervalRatingMap.keySet()) {
			IntervalRatingMap ratingMapForChordSig = 
					chordToIntervalRatingMap.get(chordSig);
			if(ratingMapForChordSig.isEmpty()) {
				chordSignaturesWithoutRatings.add(chordSig);
			}
		}

		for(ChordSignature chordSig : chordSignaturesWithoutRatings) {
			chordToIntervalRatingMap.remove(chordSig);
		}

	}

	@Override
	public NoteConsonanceRecord addRating(NoteConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}if(!record.isRated()) {
			throw new IllegalArgumentException("Record must have a rating for the addRating method.");
		}

		ConsonanceRating rating = 
				addRating(
						record.chordSignature(),
						record.interval(),
						record.rating());
		if(rating == null) {
			return null;
		}else {
			return new NoteConsonanceRecord(
					record.chordSignature(),
					record.interval(),
					rating);
		}
	}

	@Override
	public NoteConsonanceRecord removeRating(NoteConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}
		ConsonanceRating rating = 
				removeRating(
						record.chordSignature(),
						record.interval());
		if(rating==null) {
			return null;
		}else {
			return new NoteConsonanceRecord(record.chordSignature(),record.interval(),rating);
		}
	}

	@Override
	public NoteConsonanceRecord getRating(NoteConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}

		ConsonanceRating rating = 
				getRating(
						record.chordSignature(),
						record.interval());
		if(rating==null) {
			return null;
		}else {
			return new NoteConsonanceRecord(record.chordSignature(),record.interval(),rating);
		}	
	}

	@Override
	public NoteConsonanceRecord getNextRecordToBeRated() {
		//we will iterate through all chord signatures until we find one
		//that does not have a rating...we cannot use the keyset since
		//the ordering of the chord signatures is not guaranteed

		for(ChordSignature chordSig : ChordSignature.values()) {
			IntervalRatingMap intervalMapForChordSig = 
					this.chordToIntervalRatingMap.get(chordSig);

			if(intervalMapForChordSig == null) {
				return new NoteConsonanceRecord(
						chordSig, 
						Interval.UNISON, 
						null);
			}
			for(Interval interval : Interval.values()) {
				//make sure that we aren't duplicating
				//interval chord relations by going to
				//the second octave
				if( !interval.inFirstOctave() ) {
					break;
				}

				if(getRating(chordSig, interval) == null) {
					//We are looking for the next chord/interval pair to be
					//rated so the ConsonanceRating is only added to
					//avoid a NullPointerException
					//may be a design flaw
					return new NoteConsonanceRecord(chordSig, interval, null);
				}
			}
		}
		//if we reach here then every rating has been filled
		return null;
	}

	@Override
	public NoteConsonanceRecord getLastRecordRated() {
		ChordSignature chordSignatureOfLastRating,chordSignatureOfCurrentRating;
		Interval intervalOfLastRating,intervalOfCurrentRating;
		ConsonanceRating ratingOfLastRating,ratingOfCurrentRating;

		NoteConsonanceRecord currentNoteConsonanceRecordBeingRated = 
				getNextRecordToBeRated();

		//if all records have been filled return a record
		//with the last chord signature and last interval
		//possible
		if(currentNoteConsonanceRecordBeingRated == null)  {
			chordSignatureOfLastRating = 
					ChordSignature.lastSignature();
			intervalOfLastRating = 
					Interval.MAJOR7;
			ratingOfLastRating = getRating(chordSignatureOfLastRating, intervalOfLastRating);
			return new NoteConsonanceRecord(
					chordSignatureOfLastRating, 
					intervalOfLastRating,
					ratingOfLastRating);
		}

		chordSignatureOfCurrentRating = 
				currentNoteConsonanceRecordBeingRated.chordSignature();
		intervalOfCurrentRating = 
				currentNoteConsonanceRecordBeingRated.interval();
		ratingOfCurrentRating = 
				currentNoteConsonanceRecordBeingRated.rating();

		//if there have not been any ratings added yet then return a null value
		if( chordSignatureOfCurrentRating.isFirstSignature() &&
				intervalOfCurrentRating.equals(Interval.UNISON)) {
			return null;
		}

		//if we reach this point then the last rating is somewhere in the middle of 
		//the total list of chord signatures and intervals
		//
		//if the interval we are currenlty working on is UNISON then
		//the previous rating was on the previous ChordSignature.
		if(intervalOfCurrentRating.equals(Interval.UNISON)) {
			chordSignatureOfLastRating = 
					chordSignatureOfCurrentRating.getPreviousChordSignature();
			intervalOfLastRating = 
					Interval.MAJOR7;
			ratingOfLastRating = 
					getRating(chordSignatureOfLastRating, intervalOfLastRating);


			return new NoteConsonanceRecord(
					chordSignatureOfLastRating,
					intervalOfLastRating,
					ratingOfLastRating);
		}

		chordSignatureOfLastRating =
				chordSignatureOfCurrentRating;
		intervalOfLastRating = 
				intervalOfCurrentRating.getPreviousInterval();
		ratingOfLastRating = 
				getRating(chordSignatureOfLastRating, intervalOfLastRating);

		return new NoteConsonanceRecord(
				chordSignatureOfLastRating,
				intervalOfLastRating,
				ratingOfLastRating);
	}

	@Override
	public boolean isFull() {
		return getNextRecordToBeRated() == null;
	}

	@Override
	public boolean isEmpty() {
		purgeMapsWithoutRatings();
		return this.chordToIntervalRatingMap.size() == 0;
	}

	@Override
	public Set<NoteConsonanceRecord> getRecords(NoteConsonanceRecordRequest request) {
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		//TODO: we might have to change this to a checked exception
		if( !request.isInitialized()) {
			throw new IllegalArgumentException("Request has not been properly initialized");
		}
		
		purgeMapsWithoutRatings();
		
		Set<NoteConsonanceRecord> matchingRecords = new HashSet<>();
		
		for(ChordSignature chordSig : this.chordToIntervalRatingMap.keySet()) {
			//skip any chords that we are not looking for.
			if( !request.containsReferenceChord(chordSig)) {
				continue;
			}
			
			IntervalRatingMap ratingMap = this.chordToIntervalRatingMap.get(chordSig);
			for(Interval interval : ratingMap.keySet()) {
				//skip intervals that we are not looking for
				if( !request.contains(interval)) {
					continue;
				}
				ConsonanceRating rating = ratingMap.get(interval);
				
				//skip ratings we are not looking for
				if( !request.contains(rating)) {
					continue;
				}
				
				NoteConsonanceRecord recordToAdd = 
						new NoteConsonanceRecord(chordSig, interval, rating);
				matchingRecords.add(recordToAdd);
			}
		}
		
		return matchingRecords;
	}
}