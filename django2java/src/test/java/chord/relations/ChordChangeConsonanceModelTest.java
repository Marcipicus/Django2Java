package chord.relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;

/**
 * Tests for the ChordChangeConsonanceDataModel.
 * @author DAD
 *
 */
public class ChordChangeConsonanceModelTest {

	/**
	 * Parameters for testing null pointer detection in add method.
	 * @return stream of arguments containing one null argument for addRatingTest
	 */
	static Stream<Arguments> oneNullArgumentForAdd(){
		return Stream.of(
				Arguments.of(null,ChordSignature.b2,Interval.MINOR2,ConsonanceRating.MEDIOCRE),
				Arguments.of(ChordSignature.b2,null,Interval.MINOR2,ConsonanceRating.MEDIOCRE),
				Arguments.of(ChordSignature.MAJOR,ChordSignature.b2,null,ConsonanceRating.MEDIOCRE),
				Arguments.of(ChordSignature.MAJOR,ChordSignature.b2,Interval.MINOR2,null)
				);
	}
	
	/**
	 * Parameters for testing null pointer detection in remove method.
	 * @return stream of arguments containing one null argument for removeRatingTest
	 */
	static Stream<Arguments> oneNullArgumentForRemoveAndGet(){
		return Stream.of(
				Arguments.of(null,ChordSignature.b2,Interval.MINOR2),
				Arguments.of(ChordSignature.b2,null,Interval.MINOR2),
				Arguments.of(ChordSignature.MAJOR,ChordSignature.b2,null)
				);
	}
	
	/**
	 * List of parameters used to reduce amount of code created.
	 * @return stream of arguments that can be used by most tests
	 * All values should be able to be used successfully(no exceptions)
	 */
	static Stream<Arguments> defaultArgumentsToReduceCode(){
		return Stream.of(
				Arguments.of(ChordSignature.MAJOR,ChordSignature.b2,Interval.MINOR2,ConsonanceRating.BAD)
				);
	}

	/**
	 * Main data structure created before each test.
	 * It will be empty before every test.
	 */
	ChordChangeConsonanceModel cccModel;

	/**
	 * Create an empty chord change consonance model before
	 * each test.
	 */
	@BeforeEach
	void init() {
		cccModel = new ChordChangeConsonanceModel();
	}

	/**
	 * Ensure that the addRating method throws an exception when
	 * passed a null value for each of the parameters
	 * @param startChordSig start chord signature
	 * @param endChordSig end chord signature
	 * @param intervalBetweenRoots interval between the roots of each chord
	 * @param rating rating fo the chord change.
	 */
	@ParameterizedTest
	@MethodSource("oneNullArgumentForAdd")
	void testAddNull(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots, ConsonanceRating rating) {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> cccModel.addRating(startChordSig, endChordSig, intervalBetweenRoots, rating));
	}

	/**
	 * Ensure that the data model throws an exception when attempting
	 * to add a chord change between two equal chords.
	 * 
	 * Namely start and end signatures are the same
	 */
	@Test
	void testAddSameChordAtUnison() {
		final ChordSignature sameChord = ChordSignature.MAJOR;
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> cccModel.addRating(
								sameChord, 
								sameChord, 
								Interval.UNISON, 
								ConsonanceRating.MEDIOCRE));
	}

	/**
	 * Ensure that the data model throws an exception when attempting
	 * to add a rating not in the first octave.
	 * (All notes in second octave are duplicates of first)
	 */
	@Test
	void testAddChordRatingIntervalNotInFirstOctave() {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> cccModel.addRating(
								ChordSignature.b2, 
								ChordSignature.MAJOR, 
								Interval.PERFECT8, 
								ConsonanceRating.MEDIOCRE));
	}

	/**
	 * Ensure that every valid consonance rating can be added.
	 */
	@Test
	void testAddAllValidConsonanceRating() {
		for(ChordSignature startChordSig : ChordSignature.values()) {
			for(ChordSignature endChordSig : ChordSignature.values()) {
				for(Interval interval : Interval.values()) {
					if( !interval.inFirstOctave() ) {
						break;
					}
					if( startChordSig.equals(endChordSig) && interval.equals(Interval.UNISON)) {
						continue;
					}
					
					//adding a rating is trivial so no reason to check every
					//rating
					final ConsonanceRating firstRatingAdded = ConsonanceRating.VERY_GOOD;
					//Make sure that the data model is following
					//the contract, return null on first addition,
					//return last rating on second addition
					assertNull(cccModel.addRating(startChordSig, endChordSig, interval, firstRatingAdded));
					assertEquals(firstRatingAdded, cccModel.addRating(startChordSig, endChordSig, interval, ConsonanceRating.VERY_BAD));
				}
			}
		}
	}
	
	/**
	 * Ensure that the remove method throws an exception if passed a
	 * null argument.
	 * @param startChordSig start chord signature
	 * @param endChordSig end chord signature
	 * @param intervalBetweenRoots interval between the roots of the start and end chord.
	 */
	@ParameterizedTest
	@MethodSource("oneNullArgumentForRemoveAndGet")
	void testRemoveNull(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots) {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> cccModel.removeRating(startChordSig, endChordSig, intervalBetweenRoots));
	}
	
	/**
	 * Ensure that the model throws an exception for intervals not in
	 * the first octave.
	 * (All intervals in second octave are duplicate notes of the first)
	 */
	@Test
	void testRemoveChordRatingIntervalNotInFirstOctave() {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> cccModel.removeRating(
								ChordSignature.b2, 
								ChordSignature.MAJOR, 
								Interval.PERFECT8));
	}
	
	/**
	 * Ensure that a rating can be removed succesfully when it exists.
	 * @param startChordSig start chord signature
	 * @param endChordSig end chord signature
	 * @param intervalBetweenRoots interval between the roots of the start and end chord
	 * @param rating rating for the chord change
	 */
	@ParameterizedTest
	@MethodSource("defaultArgumentsToReduceCode")
	void testRemoveChordRatingExistingRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots, ConsonanceRating rating) {
		cccModel.addRating(startChordSig, endChordSig, intervalBetweenRoots, rating);
		assertEquals(rating, cccModel.removeRating(startChordSig, endChordSig, intervalBetweenRoots));
	}
	
	/**
	 * Ensure that the data model returns null when attempting to
	 * remove a non existent rating
	 * @param startChordSig start chord signature
	 * @param endChordSig end chord signature
	 * @param intervalBetweenRoots interval between roots of start and end chord
	 * @param rating rating for the chord change.
	 */
	@ParameterizedTest
	@MethodSource("defaultArgumentsToReduceCode")
	void testRemoveChordRatingNonExistentRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots, ConsonanceRating rating) {
		assertNull(cccModel.removeRating(startChordSig, endChordSig, intervalBetweenRoots));
	}
	
	/**
	 * Ensure that the get method throws an exception if passed a
	 * null argument.
	 * @param startChordSig start chord signature
	 * @param endChordSig end chord signature
	 * @param intervalBetweenRoots interval between the roots of the start and end chord.
	 */
	@ParameterizedTest
	@MethodSource("oneNullArgumentForRemoveAndGet")
	void testGetRatingPassedNull(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots) {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> cccModel.getRating(startChordSig, endChordSig, intervalBetweenRoots));
	}
	

	/**
	 * Ensure that the data model throws an exceptino when attempting to
	 * use an interval beyond the first octave
	 * (All intervals beyond the first octave are duplicate notes.)
	 */
	@Test
	void testGetRatingPassedInvalidInterval() {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> cccModel.getRating(
								ChordSignature.b2, 
								ChordSignature.MAJOR, 
								Interval.PERFECT8));
	}
	
	@ParameterizedTest
	@MethodSource("defaultArgumentsToReduceCode")
	void testGetRatingPassedValidValues(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots, ConsonanceRating rating) {
		assertNull(cccModel.getRating(startChordSig, endChordSig, intervalBetweenRoots));
		
		cccModel.addRating(startChordSig, endChordSig, intervalBetweenRoots, rating);
		
		assertEquals(rating, cccModel.getRating(startChordSig, endChordSig, intervalBetweenRoots));
	}
}
