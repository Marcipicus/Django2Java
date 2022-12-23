package chord.relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import chord.ConsonanceRating;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;

/**
 * Test suite for the ScaleConsonanceModel.
 * @author DAD
 *
 */
public class ScaleConsonanceModelInternalTest {

	//.tmp file name used to make git ignore on checkins if
	//we forget to delete the test file
	final String testFileName = "testScaleConsonanceFile.tmp";

	/**
	 * Parameters for testing null pointer detection in add method.
	 * @return stream of arguments containing one null argument for addRatingTest
	 */
	static Stream<Arguments> oneNullArgumentForAdd(){
		return Stream.of(
				Arguments.of(null,ScaleSignature.AEOLIAN,ConsonanceRating.MEDIOCRE),
				Arguments.of(ChordSignature.MAJOR,null,ConsonanceRating.MEDIOCRE),
				Arguments.of(ChordSignature.MAJOR,ScaleSignature.AEOLIAN_b5,null)
				);
	}

	/**
	 * Parameters for testing null pointer detection in remove and get method.
	 * @return stream of arguments containing one null argument for removeRatingTest
	 */
	static Stream<Arguments> oneNullArgumentForRemoveAndGet(){
		return Stream.of(
				Arguments.of(null,ScaleSignature.AEOLIAN_BLUES),
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
				Arguments.of(ChordSignature.MAJOR,ScaleSignature.AEOLIAN_NATURAL_7,ConsonanceRating.BAD)
				);
	}

	/**
	 * Take the given ScaleConsonanceModel and fill it with a consistent
	 * rating every time
	 * @param model to be filled....it is assumed that the model is empty.
	 */
	static void populateTestModel(boolean full, ScaleConsonanceModel model) {
		final int halfChordSigIndex = ChordSignature.values().length / 2;
		final int fullChordSigIndex = ChordSignature.values().length;

		final int endIndex = full?fullChordSigIndex:halfChordSigIndex;
		for(int i=0; i<endIndex; i++) {
			ChordSignature chorSig = ChordSignature.values()[i];

			for(ScaleSignature scaleSig : ScaleSignature.values()) {

				ConsonanceRating rating = ConsonanceRating.MEDIOCRE;

				model.addRating(chorSig, scaleSig, rating);
			}
		}
	}

	/**
	 * Main data model used for tests.
	 * Initialized before every test.
	 */
	ScaleConsonanceModel scModel,otherModel;

	/**
	 * Initialization code for main data model.
	 * Data model initialized and unmodified.
	 */
	@BeforeEach
	void init() {
		scModel = new ScaleConsonanceModel();
		otherModel = new ScaleConsonanceModel();
	}

	/**
	 * Ensure that the addRating method throws an exception when
	 * passed a null value for each of the parameters
	 * @param chordSig Chord Signature
	 * @param scaleSig Scale Signature
	 * @param rating rating for the chord change.
	 */
	@ParameterizedTest
	@MethodSource("oneNullArgumentForAdd")
	void testAddNull(ChordSignature chordSig, ScaleSignature scaleSig, ConsonanceRating rating) {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> scModel.addRating(chordSig, scaleSig, rating));
	}

	/**
	 * Ensure that every valid consonance rating can be added.
	 */
	@Test
	void testAddAllValidConsonanceRating() {
		for(ChordSignature chordSig : ChordSignature.values()) {
			for(ScaleSignature scaleSig : ScaleSignature.values()) {
				//adding a rating is trivial so no reason to check every
				//rating
				final ConsonanceRating firstRatingAdded = ConsonanceRating.VERY_GOOD;
				//Make sure that the data model is following
				//the contract, return null on first addition,
				//return last rating on second addition
				assertNull(scModel.addRating(chordSig, scaleSig, firstRatingAdded));
				assertEquals(firstRatingAdded, scModel.addRating(chordSig,scaleSig, ConsonanceRating.VERY_BAD));
			}
		}
	}

	/**
	 * Ensure that the remove method throws an exception if passed a
	 * null argument.
	 * @param chordSig Chord Signature
	 * @param scaleSig Scale Signature
	 */
	@ParameterizedTest
	@MethodSource("oneNullArgumentForRemoveAndGet")
	void testRemoveNull(ChordSignature chordSig, ScaleSignature scaleSig) {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> scModel.removeRating(chordSig, scaleSig));
	}

	/**
	 * Ensure that a rating can be removed successfully when it exists.
	 * @param chordSig Chord Signature
	 * @param scaleSig Scale Signature
	 * @param rating rating for the chord/scale combination
	 */
	@ParameterizedTest
	@MethodSource("defaultArgumentsToReduceCode")
	void testRemoveScaleRatingExistingRating(ChordSignature chordSig, ScaleSignature scaleSig, ConsonanceRating rating) {
		scModel.addRating(chordSig, scaleSig, rating);
		assertEquals(rating, scModel.removeRating(chordSig, scaleSig));
		assertNull(scModel.removeRating(chordSig, scaleSig));
	}

	/**
	 * Ensure that the data model returns null when attempting to
	 * remove a non existent rating
	 * @param chordSig Chord Signature
	 * @param scaleSig Scale Signature
	 * @param rating rating for the chord change.
	 */
	@ParameterizedTest
	@MethodSource("defaultArgumentsToReduceCode")
	void testRemoveScaleRatingNonExistentRating(ChordSignature chordSig, ScaleSignature scaleSig, ConsonanceRating rating) {
		assertNull(scModel.removeRating(chordSig, scaleSig));
	}

	/**
	 * Ensure that the get method throws an exception if passed a
	 * null argument.
	 * @param chordSig Chord Signature
	 * @param scaleSig Scale Signature
	 */
	@ParameterizedTest
	@MethodSource("oneNullArgumentForRemoveAndGet")
	void testGetRatingPassedNull(ChordSignature chordSig, ScaleSignature scaleSig) {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> scModel.getRating(chordSig, scaleSig));
	}

	/**
	 * Ensure that the data model obeys the contract for retrieving
	 * ratings. null if rating doesn't exist, the rating if it does exist.
	 * @param chordSig Chord Signature
	 * @param scaleSig Scale Signature
	 * @param rating rating for the interval.
	 */
	@ParameterizedTest
	@MethodSource("defaultArgumentsToReduceCode")
	void testGetRatingPassedValidValues(ChordSignature chordSig, ScaleSignature scaleSig, ConsonanceRating rating) {
		assertNull(scModel.getRating(chordSig, scaleSig));

		scModel.addRating(chordSig, scaleSig, rating);

		assertEquals(rating, scModel.getRating(chordSig, scaleSig));
	}
}
