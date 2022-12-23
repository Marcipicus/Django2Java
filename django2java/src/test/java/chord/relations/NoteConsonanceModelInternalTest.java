package chord.relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.record.NoteConsonanceRecord;

/**
 * This test class exists to test the NoteConsonanceModel's
 * internal interface.
 * @author DAD
 *
 */
public class NoteConsonanceModelInternalTest {
	
	/**
	 * Parameters for testing null pointer detection in add method.
	 * @return stream of arguments containing one null argument for addRatingTest
	 */
	static Stream<Arguments> oneNullArgumentForAdd(){
		return Stream.of(
				Arguments.of(null,Interval.MINOR2,ConsonanceRating.MEDIOCRE),
				Arguments.of(ChordSignature.MAJOR,null,ConsonanceRating.MEDIOCRE),
				Arguments.of(ChordSignature.MAJOR,Interval.MINOR2,null)
				);
	}

	/**
	 * Parameters for testing null pointer detection in remove method.
	 * @return stream of arguments containing one null argument for removeRatingTest
	 */
	static Stream<Arguments> oneNullArgumentForRemoveAndGet(){
		return Stream.of(
				Arguments.of(null,Interval.MINOR2),
				Arguments.of(ChordSignature.MAJOR,null)
				);
	}

	/**
	 * List of parameters used to reduce amount of code created.
	 * @return stream of arguments that can be used by most tests
	 * All values should be able to be used successfully(no exceptions)
	 */
	static Stream<Arguments> defaultArgumentsToReduceCode(){
		return Stream.of(
				Arguments.of(ChordSignature.MAJOR,Interval.MINOR2,ConsonanceRating.BAD)
				);
	}

	/**
	 * Take the given NoteConsonanceModel and fill it with a consistent
	 * rating every time
	 * @param full fill the data structure if true, half fill if false
	 * @param model to be filled....it is assumed that the model is empty.
	 */
	static void populateTestModel(boolean full, NoteConsonanceModel model) {	
		final int halfChordSigIndex = ChordSignature.values().length / 2;
		final int fullChordSigIndex = ChordSignature.values().length;

		final int endIndex = full?fullChordSigIndex:halfChordSigIndex;
		for(int i=0; i<endIndex; i++) {
			ChordSignature chorSig = ChordSignature.values()[i];

			for(Interval interval : Interval.values()) {
				if( !interval.inFirstOctave()) {
					break;
				}

				ConsonanceRating rating = ConsonanceRating.MEDIOCRE;

				model.addRating(chorSig, interval, rating);
			}
		}
	}

	/**
	 * Main data model used for tests.
	 * Initialized before every test.
	 */
	NoteConsonanceModel ncModel,otherModel;

	/**
	 * Initialization code for main data model.
	 * Data model initialized and unmodified.
	 */
	@BeforeEach
	void init() {
		ncModel = new NoteConsonanceModel();
		otherModel = new NoteConsonanceModel();
	}
	
	/**
	 * Make sure .equals works
	 */
	@Test
	void testEquals() {
		populateTestModel(false,ncModel);
		populateTestModel(false,otherModel);

		assertTrue(ncModel.equals(otherModel));
		assertEquals(ncModel,otherModel);
	}

	/**
	 * Ensure that the addRating method throws an exception when
	 * passed a null value for each of the parameters
	 * @param chordSig Chord Signature
	 * @param interval interval between the root of the chord and the note
	 * @param rating rating for the chord change.
	 */
	@ParameterizedTest
	@MethodSource("oneNullArgumentForAdd")
	void testAddNull(ChordSignature chordSig, Interval interval, ConsonanceRating rating) {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> ncModel.addRating(chordSig, interval, rating));
	}

	/**
	 * Ensure that the data model throws an exception when attempting
	 * to add a rating not in the first octave.
	 * (All notes in second octave are duplicates of first)
	 */
	@Test
	void testAddNoteRatingIntervalNotInFirstOctave() {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> ncModel.addRating(
								ChordSignature.b2, 
								Interval.PERFECT8, 
								ConsonanceRating.MEDIOCRE));
	}

	/**
	 * Ensure that every valid consonance rating can be added.
	 */
	@Test
	void testAddAllValidConsonanceRating() {
		for(ChordSignature chordSig : ChordSignature.values()) {
			for(Interval interval : Interval.values()) {
				if( !interval.inFirstOctave() ) {
					break;
				}

				//adding a rating is trivial so no reason to check every
				//rating
				final ConsonanceRating firstRatingAdded = ConsonanceRating.VERY_GOOD;
				//Make sure that the data model is following
				//the contract, return null on first addition,
				//return last rating on second addition
				assertNull(ncModel.addRating(chordSig, interval, firstRatingAdded));
				assertEquals(firstRatingAdded, ncModel.addRating(chordSig,interval, ConsonanceRating.VERY_BAD));
			}
		}
	}

	/**
	 * Ensure that the remove method throws an exception if passed a
	 * null argument.
	 * @param chordSig Chord Signature
	 * @param interval interval between the root of the chord and the note
	 */
	@ParameterizedTest
	@MethodSource("oneNullArgumentForRemoveAndGet")
	void testRemoveNull(ChordSignature chordSig, Interval interval) {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> ncModel.removeRating(chordSig, interval));
	}

	/**
	 * Ensure that the model throws an exception for intervals not in
	 * the first octave.
	 * (All intervals in second octave are duplicate notes of the first)
	 */
	@Test
	void testRemoveNoteRatingIntervalNotInFirstOctave() {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> ncModel.removeRating(
								ChordSignature.b2, 
								Interval.PERFECT8));
	}

	/**
	 * Ensure that a rating can be removed successfully when it exists.
	 * @param chordSig Chord Signature
	 * @param interval interval between the root of the chord and the note
	 * @param rating rating for the chord change
	 */
	@ParameterizedTest
	@MethodSource("defaultArgumentsToReduceCode")
	void testRemoveNoteRatingExistingRating(ChordSignature chordSig, Interval interval, ConsonanceRating rating) {
		ncModel.addRating(chordSig, interval, rating);
		assertEquals(rating, ncModel.removeRating(chordSig, interval));
		assertNull(ncModel.removeRating(chordSig, interval));
	}

	/**
	 * Ensure that the data model returns null when attempting to
	 * remove a non existent rating
	 * @param chordSig Chord Signature
	 * @param interval interval between the root of the chord and the note
	 * @param rating rating for the chord change.
	 */
	@ParameterizedTest
	@MethodSource("defaultArgumentsToReduceCode")
	void testRemoveNoteRatingNonExistentRating(ChordSignature chordSig, Interval interval, ConsonanceRating rating) {
		assertNull(ncModel.removeRating(chordSig, interval));
	}

	/**
	 * Ensure that the get method throws an exception if passed a
	 * null argument.
	 * @param chordSig Chord Signature
	 * @param interval interval between the root of the chord and the note
	 */
	@ParameterizedTest
	@MethodSource("oneNullArgumentForRemoveAndGet")
	void testGetRatingPassedNull(ChordSignature chordSig, Interval interval) {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> ncModel.getRating(chordSig, interval));
	}


	/**
	 * Ensure that the data model throws an exception when attempting to
	 * use an interval beyond the first octave
	 * (All intervals beyond the first octave are duplicate notes.)
	 */
	@Test
	void testGetRatingPassedInvalidInterval() {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> ncModel.getRating(
								ChordSignature.b2,  
								Interval.PERFECT8));
	}

	/**
	 * Ensure that the data model obeys the contract for retrieving
	 * ratings. null if rating doesn't exist, the rating if it does exist.
	 * @param chordSig Chord Signature
	 * @param interval interval between the root of the chord and the note
	 * @param rating rating for the interval.
	 */
	@ParameterizedTest
	@MethodSource("defaultArgumentsToReduceCode")
	void testGetRatingPassedValidValues(ChordSignature chordSig, Interval interval, ConsonanceRating rating) {
		assertNull(ncModel.getRating(chordSig, interval));

		ncModel.addRating(chordSig, interval, rating);

		assertEquals(rating, ncModel.getRating(chordSig, interval));
	}

	/**
	 * Make sure that the getNextRecordToBeRated function
	 * works on an empty data model
	 */
	@Test
	void testGetNextRecordToBeRatedEmpty() {
		final NoteConsonanceRecord record = 
				ncModel.getNextRecordToBeRated();

		assertEquals(ChordSignature.firstSignature(), record.chordSignature());
		assertEquals(Interval.UNISON,record.interval());
	}

	/**
	 * Fill a NoteConsonanceModel and make sure that
	 * the getNextNoteRecordToBeRated returns null.
	 */
	@Test
	void testGetNextRecordToBeRatedFull() {
		populateTestModel(true,ncModel);

		final NoteConsonanceRecord record = 
				ncModel.getNextRecordToBeRated();

		assertNull(record);
	}

	/**
	 * Add a single rating and make sure that the nextNoteRecord to be rated
	 * has the correct interval. The ChordSignature doesn't change hence the
	 * no rollover of chord.
	 */
	@Test
	void testGetNextRecordToBeRatedNoRolloverOfChord() {
		final ChordSignature addedChordSig = ChordSignature.firstSignature();
		final Interval addedInterval = Interval.UNISON;
		ncModel.addRating(addedChordSig, addedInterval, ConsonanceRating.MEDIOCRE);

		final NoteConsonanceRecord nextNoteRecord = 
				ncModel.getNextRecordToBeRated();

		assertEquals(addedChordSig,nextNoteRecord.chordSignature());
		assertEquals(addedInterval.getNextInterval(), nextNoteRecord.interval());
	}

	/**
	 * Fill all ratings for a single chord and make sure that
	 * the getNextNoteRecordToBeRated method returns the next
	 * chord signature and a unison interval.
	 */
	@Test
	void testGetNextRecordToBeRatedRolloverOfChord() {
		final ChordSignature addedChordSig = ChordSignature.firstSignature();
		final ConsonanceRating addedRating = ConsonanceRating.GOOD;

		for(Interval interval : Interval.values()) {
			ncModel.addRating(addedChordSig, interval, addedRating);
			//Make sure we exit after the last 
			if( interval.equals(Interval.MAJOR7) ) {
				break;
			}
		}

		final NoteConsonanceRecord nextToBeRatedRecord =
				ncModel.getNextRecordToBeRated();

		assertEquals(
				addedChordSig.getNextChordSignature(),
				nextToBeRatedRecord.chordSignature());
		assertEquals(
				Interval.UNISON,
				nextToBeRatedRecord.interval());
	}

	/**
	 * Get the last note consonance record of an empty collection
	 * and make sure that it is null.
	 */
	@Test
	void testGetLastRatedRecordEmpty() {
		final NoteConsonanceRecord lastRatedNoteConsonanceRecord= 
				ncModel.getLastRecordRated();

		assertNull(lastRatedNoteConsonanceRecord);
	}

	/**
	 * Fill a NoteConsonanceModel and make sure that
	 * the last rating added contains the last ChordSignature
	 * and an Interval of MAJOR7
	 */
	@Test
	void testGetLastRatedRecordFull() {
		final ChordSignature lastChordSignature = 
				ChordSignature.lastSignature();
		final Interval lastIntervalToBeRated =
				Interval.MAJOR7;

		populateTestModel(true, ncModel);

		final NoteConsonanceRecord lastRatingRecord =
				ncModel.getLastRecordRated();

		assertEquals(
				lastChordSignature,
				lastRatingRecord.chordSignature());
		assertEquals(
				lastIntervalToBeRated,
				lastRatingRecord.interval());
	}

	/**
	 * Add a rating and make sure that it is returned from 
	 * the getRecordOfLastNoteConsonanceRated function.
	 */
	@Test
	void testGetLastRatedRecordNormalEntry() {
		final ChordSignature addedChordSig = ChordSignature.firstSignature();
		final Interval addedInterval = Interval.UNISON;
		final ConsonanceRating addedRating = ConsonanceRating.BAD;

		ncModel.addRating(addedChordSig, addedInterval, addedRating);

		NoteConsonanceRecord addedRecord = 
				ncModel.getLastRecordRated();

		assertEquals(addedChordSig,addedRecord.chordSignature());
		assertEquals(addedInterval,addedRecord.interval());
		assertEquals(addedRating,addedRecord.rating());

		//Make sure that the model returns null after
		//values have been removed.
		ncModel.removeRating(addedChordSig, addedInterval);
		NoteConsonanceRecord lastRecordAfterRemoval =
				ncModel.getLastRecordRated();

		assertNull(lastRecordAfterRemoval);
	}

	/**
	 * Fill all ratings for a single chord and make sure
	 * that the getLastRatingAdded method returns chordSig/MAJOR7 
	 */
	@Test
	void testGetRecordOfLastNoteConsonanceRatedRollover() {
		final ChordSignature addedChordSig = 
				ChordSignature.firstSignature();
		final ConsonanceRating addedRating =
				ConsonanceRating.GOOD;

		//fill all ratings for first chord signature
		//and all intervals.
		for(Interval interval : Interval.values() ) {
			ncModel.addRating(addedChordSig, interval, addedRating);
			if(interval.equals(Interval.MAJOR7)) {
				break;
			}
		}

		final NoteConsonanceRecord lastNoteRatedRecord = 
				ncModel.getLastRecordRated();

		assertEquals(
				addedChordSig,
				lastNoteRatedRecord.chordSignature());
		assertEquals(
				Interval.MAJOR7,
				lastNoteRatedRecord.interval());
	}
}
