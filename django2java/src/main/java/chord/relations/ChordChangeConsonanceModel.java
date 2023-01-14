package chord.relations;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.record.ChordChangeConsonanceRecord;
import chord.relations.request.ChordChangeConsonanceRecordRequest;

/**
 * Main data structure for rating chord changes.
 * @author DAD
 *
 */
public class ChordChangeConsonanceModel implements RatingModel<ChordChangeConsonanceRecord,ChordChangeConsonanceRecordRequest>{

	private Map<ChordSignature,Map<ChordSignature,IntervalRatingMap>> chordChangeConsonanceMap;

	/**
	 * Create a new ChordChangeConsonanceModel.
	 */
	public ChordChangeConsonanceModel() {
		this.chordChangeConsonanceMap = new EnumMap<>(ChordSignature.class);
	}

	/**
	 * Add a rating for the given start chord, end chord, and relation between roots.
	 * @param startChordSig the start chord signature
	 * @param endChordSig the end chord signature
	 * @param intervalBetweenRoots interval between the roots of the start chord and end chord.
	 * @param rating rating to be added
	 * @return the previous rating for the parameters,
	 * null if there was no previous rating.
	 */
	 ConsonanceRating addRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots, ConsonanceRating rating) {
		if(startChordSig == null) {
			throw new NullPointerException("start chord sig may not be null");
		}
		if(endChordSig == null) {
			throw new NullPointerException("end chord sig may not be null");
		}
		if(intervalBetweenRoots == null) {
			throw new NullPointerException("interval between roots may not be null.");
		}
		if( !intervalBetweenRoots.inFirstOctave()) {
			throw new IllegalArgumentException("Interval between roots must be between UNISON and MAJOR7 inclusive");
		}
		if(rating == null) {
			throw new NullPointerException("rating must not be null");
		}
		//if the start and end chords are the same and the interval between roots is unison,
		//then the two chords are exactly the same and should not be rated.
		//TODO: I might throw a created exception here so the calling function can tell
		//that the rating wasn't added and i don't have to confuse the return values of
		//the function
		if(startChordSig.equals(endChordSig) && intervalBetweenRoots.equals(Interval.UNISON)){
			throw new IllegalArgumentException("");
		}

		//I have to grab each map one at a time so that I don't
		//dereference a null pointer
		Map<ChordSignature,IntervalRatingMap> startChordToEndChordRatingMap =
				chordChangeConsonanceMap.get(startChordSig);

		if(startChordToEndChordRatingMap == null) {
			startChordToEndChordRatingMap = new EnumMap<>(ChordSignature.class);
			chordChangeConsonanceMap.put(startChordSig, startChordToEndChordRatingMap);
		}

		IntervalRatingMap endChordIntervalRatingMap = 
				startChordToEndChordRatingMap.get(endChordSig);

		if(endChordIntervalRatingMap == null) {
			endChordIntervalRatingMap = new IntervalRatingMap();
			startChordToEndChordRatingMap.put(endChordSig, endChordIntervalRatingMap);
		}

		return endChordIntervalRatingMap.put(intervalBetweenRoots, rating);
	}

	/**
	 * Remove a rating for the given parameters.
	 * @param startChordSig start chord signature
	 * @param endChordSig end chord signature
	 * @param intervalBetweenRoots interval between the roots of the two chords.
	 * @return the rating associated with the parameters, or null if no
	 * rating exists.
	 */
	ConsonanceRating removeRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots) {
		if(startChordSig == null) {
			throw new NullPointerException("start chord sig may not be null");
		}
		if(endChordSig == null) {
			throw new NullPointerException("end chord sig may not be null");
		}
		if(intervalBetweenRoots == null) {
			throw new NullPointerException("interval between roots may not be null.");
		}
		if( !intervalBetweenRoots.inFirstOctave()) {
			throw new IllegalArgumentException("Interval between roots must be between UNISON and MAJOR7 inclusive");
		}

		//Have to get each map one at a time so that we don't
		//dereference a null pointer.
		Map<ChordSignature,IntervalRatingMap> endChordSigToRatingMap =
				this.chordChangeConsonanceMap.get(startChordSig);
		if(endChordSigToRatingMap == null) {
			return null;
		}

		IntervalRatingMap chordChangeIntervalRatingMap = 
				endChordSigToRatingMap.get(endChordSig);
		if(chordChangeIntervalRatingMap == null) {
			return null;
		}

		return chordChangeIntervalRatingMap.remove(intervalBetweenRoots);
	}

	/**
	 * Get the rating for the given parameters.
	 * @param startChordSig the start chord signature
	 * @param endChordSig the end chord signature
	 * @param intervalBetweenRoots interval between the roots of the two chords
	 * @return the rating associated with the parameters,
	 * null if no rating exists
	 */
	ConsonanceRating getRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots) {
		if(startChordSig == null) {
			throw new NullPointerException("start chord sig may not be null");
		}
		if(endChordSig == null) {
			throw new NullPointerException("end chord sig may not be null");
		}
		if(intervalBetweenRoots == null) {
			throw new NullPointerException("interval between roots may not be null.");
		}
		if( !intervalBetweenRoots.inFirstOctave()) {
			throw new IllegalArgumentException("Interval between roots must be between UNISON and MAJOR7 inclusive");
		}

		//Have to get each map one at a time so that we don't
		//dereference a null pointer.
		Map<ChordSignature,IntervalRatingMap> endChordSigToRatingMap =
				this.chordChangeConsonanceMap.get(startChordSig);
		if(endChordSigToRatingMap == null) {
			return null;
		}

		IntervalRatingMap chordChangeIntervalRatingMap = 
				endChordSigToRatingMap.get(endChordSig);
		if(chordChangeIntervalRatingMap == null) {
			return null;
		}

		return chordChangeIntervalRatingMap.get(intervalBetweenRoots);
	}

	/**
	 * Get a set of all start chord signatures that have ratings. 
	 * @return set of all start chord signatures with ratings.
	 */
	private Set<ChordSignature> getStartChordSignatureSet(){
		return this.chordChangeConsonanceMap.keySet();
	}

	/**
	 * Get the set of all endchord ratings for the given start chord.
	 * 
	 * @param startChordSig chord for which we are retrieving end chord
	 * ratings. 
	 * @return a set of all end chords that ave been saved and are associated
	 * with the start chord. an empty set if no such ratings exist
	 */
	private Set<ChordSignature> getEndChordSignatureSetForStartChordSignature(
			ChordSignature startChordSig){
		if(startChordSig == null) {
			throw new NullPointerException("start chord signature may not be null");
		}

		Map<ChordSignature,IntervalRatingMap> endChordMapForStartChordSig = 
				this.chordChangeConsonanceMap.get(startChordSig);

		if(endChordMapForStartChordSig == null) {
			return Collections.emptySet();
		}

		return endChordMapForStartChordSig.keySet();
	}

	/**
	 * Remove any empty maps to simplify the equals and isEmpty methods.
	 */
	private void purgeUnusedMaps() {
		//Remove any end maps that have no ratings
		purgeUnusedEndChordMaps();

		//Remove any start chords that no longer have any end chords
		//because of the previous removal
		purgeEmptyStartChordMaps();
	}

	/**
	 * Remove any endChordMaps that no longer have any ratings.
	 */
	private void purgeUnusedEndChordMaps() {
		List<ChordChangeConsonanceRecord> recordsWithNoRatings = new LinkedList<>();

		for(ChordSignature startChord : getStartChordSignatureSet()) {
			for(ChordSignature endChord : getEndChordSignatureSetForStartChordSignature(startChord)) {
				IntervalRatingMap intRatingMap = 
						this.chordChangeConsonanceMap.get(startChord).get(endChord);
				if(intRatingMap.keySet().size() == 0) {
					//Save the startChordSignature and the endChordSignature
					//in a record for later....interval is ignored.
					recordsWithNoRatings.add(
							new ChordChangeConsonanceRecord(
									startChord, 
									endChord, 
									Interval.MAJOR3, 
									null));
				}
			}
		}

		//remove the endChords from the map
		for(ChordChangeConsonanceRecord record : recordsWithNoRatings) {
			this.chordChangeConsonanceMap.get(record.startChordSignature()).remove(record.endChordSignature());
		}
	}

	/**
	 * Remove any maps for start chords that no longer have any
	 * associated end chords because of a removal by purgeUnusedEndChordMaps
	 */
	private void purgeEmptyStartChordMaps() {
		List<ChordSignature> startChordsWithNoEndChordMaps = new LinkedList<>();

		for(ChordSignature startChord : getStartChordSignatureSet()) {
			Map<ChordSignature,IntervalRatingMap> endChordMap = 
					this.chordChangeConsonanceMap.get(startChord);

			if(endChordMap.keySet().size() == 0) {
				startChordsWithNoEndChordMaps.add(startChord);
			}
		}

		for(ChordSignature chordSig : startChordsWithNoEndChordMaps) {
			this.chordChangeConsonanceMap.remove(chordSig);
		}
	}


	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof ChordChangeConsonanceModel))
			return false;

		ChordChangeConsonanceModel other = (ChordChangeConsonanceModel)o;
		this.purgeUnusedMaps();
		other.purgeUnusedMaps();

		return this.chordChangeConsonanceMap.equals(other.chordChangeConsonanceMap);
	}

	@Override
	public ChordChangeConsonanceRecord addRating(ChordChangeConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}
		ConsonanceRating rating = addRating(
				record.startChordSignature(),
				record.endChordSignature(), 
				record.intervalBetweenRoots(), 
				record.rating());
		if(rating == null) {
			return null;
		}else {
			return new ChordChangeConsonanceRecord(
					record.startChordSignature(), 
					record.endChordSignature(), 
					record.intervalBetweenRoots(), 
					rating);
		}
	}

	@Override
	public ChordChangeConsonanceRecord removeRating(ChordChangeConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}
		ConsonanceRating rating = removeRating(
				record.startChordSignature(),
				record.endChordSignature(), 
				record.intervalBetweenRoots());
		if(rating == null) {
			return null;
		}else {
			return new ChordChangeConsonanceRecord(
					record.startChordSignature(), 
					record.endChordSignature(), 
					record.intervalBetweenRoots(), 
					rating);
		}
	}

	@Override
	public ChordChangeConsonanceRecord getRating(ChordChangeConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}
		ConsonanceRating rating = getRating(
				record.startChordSignature(),
				record.endChordSignature(), 
				record.intervalBetweenRoots());
		if(rating == null) {
			return null;
		}else {
			return new ChordChangeConsonanceRecord(
					record.startChordSignature(), 
					record.endChordSignature(), 
					record.intervalBetweenRoots(), 
					rating);
		}
	}

	@Override
	public ChordChangeConsonanceRecord getNextRecordToBeRated() {
		if(isFull()) {
			return null;
		}

		for(ChordSignature startChordSig : ChordSignature.valuesAsList()) {
			for(ChordSignature endChordSig : ChordSignature.valuesAsList()) {
				for(Interval interval : Interval.valuesAsList()) {
					//Make sure we aren't duplicating ratings
					//PERFECT8 refers to the same note as UNISON
					if( !interval.inFirstOctave() ) {
						break;
					}
					//if the start and end chord are the same and
					//the interval is UNISON then the combination
					//represents the same chord
					if(startChordSig.equals(endChordSig) && interval.equals(Interval.UNISON)) {
						continue;
					}
					ConsonanceRating rating = getRating(startChordSig, endChordSig, interval);
					if(rating == null) {
						return new ChordChangeConsonanceRecord(startChordSig, endChordSig, interval, null);
					}
				}
			}
		}

		//this is redundant...I suppose that the 
		//check at the start is unnecessary
		//may have to change this in the future
		return null;
	}

	@Override
	public ChordChangeConsonanceRecord getLastRecordRated() {
		//This check isn't really necessary
		//it works for now...maybe remove it later
		if(isEmpty()) {
			return null;
		}

		ChordChangeConsonanceRecord lastRecordRated = null;

		OUTER_LOOP:
			for(ChordSignature startChord : ChordSignature.valuesAsList()) {
				for(ChordSignature endChord : ChordSignature.valuesAsList()) {
					for(Interval interval : Interval.valuesAsList()) {
						//Make sure we aren't duplicating ratings
						//PERFECT8 refers to the same note as UNISON
						if( !interval.inFirstOctave() ) {
							break;
						}
						//if the start and end chord are the same and
						//the interval is UNISON then the combination
						//represents the same chord
						if(startChord.equals(endChord) && interval.equals(Interval.UNISON)) {
							continue;
						}

						ConsonanceRating rating = getRating(startChord, endChord, interval);
						if(rating == null) {
							break OUTER_LOOP;
						}
						lastRecordRated = new ChordChangeConsonanceRecord(startChord, endChord, interval, rating);
					}
				}
			}

		return lastRecordRated;
	}

	@Override
	public boolean isFull() {
		
		//TODO: These loops are pretty much the same
		//we might need to parameterize this later with
		//delegates or lambda expressions....works for now
		for(ChordSignature startChord : ChordSignature.valuesAsList()) {
			for(ChordSignature endChord : ChordSignature.valuesAsList()) {
				for(Interval interval : Interval.valuesAsList()) {
					//Make sure we aren't duplicating ratings
					//PERFECT8 refers to the same note as UNISON
					if( !interval.inFirstOctave() ) {
						break;
					}
					//if the start and end chord are the same and
					//the interval is UNISON then the combination
					//represents the same chord
					if(startChord.equals(endChord) && interval.equals(Interval.UNISON)) {
						continue;
					}

					ConsonanceRating rating = getRating(startChord, endChord, interval);
					if(rating == null) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		purgeUnusedMaps();
		return this.chordChangeConsonanceMap.keySet().size() == 0;
	}

	@Override
	public Set<ChordChangeConsonanceRecord> getRecords(ChordChangeConsonanceRecordRequest request) {
		if(request == null) {
			throw new NullPointerException("request may not be null.");
		}
		if( !request.isInitialized() ) {
			throw new IllegalStateException("request is not initialized");
		}
		//purge unused maps to simplify the request process
		//by not checking for null values
		purgeUnusedMaps();
		
		Set<ChordChangeConsonanceRecord> recordsRequested = new HashSet<>();
		
		//reference chord loop
		for(ChordSignature referenceChordSig : this.chordChangeConsonanceMap.keySet()) {
			if( ! request.containsReferenceChord(referenceChordSig)) {
				continue;
			}
			
			//no need to check for nulls in these loops since we have already
			//purged all unused maps and ratings
			Map<ChordSignature,IntervalRatingMap> targetChordToIntervalMap =
					this.chordChangeConsonanceMap.get(referenceChordSig);

			for(ChordSignature targetChordSig : targetChordToIntervalMap.keySet()) {
				if( !request.containsTargetChord(targetChordSig)) {
					continue;
				}
				
				IntervalRatingMap intervalRatingMap = 
						targetChordToIntervalMap.get(targetChordSig);
				
				for(Interval interval : intervalRatingMap.keySet()) {
					if( !request.containsIntervalBetweenRoots(interval)) {
						continue;
					}
					
					ConsonanceRating rating = intervalRatingMap.get(interval);
					if( !request.contains(rating)) {
						continue;
					}
					
					recordsRequested.add(
							new ChordChangeConsonanceRecord(
									referenceChordSig, 
									targetChordSig, 
									interval, 
									rating));
				}
			}
		}
		return recordsRequested;
	}
}
