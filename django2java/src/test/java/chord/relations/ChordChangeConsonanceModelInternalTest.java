package chord.relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
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
public class ChordChangeConsonanceModelInternalTest {

	//.tmp file name used to make git ignore on checkins if
	//we forget to delete the test file
	static final String testFileName = "testChordChangeConsonanceFile.tmp";

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
	 * Take the given ChordChangeConsonanceModel and fill it with a consistent
	 * rating every time
	 * @param model to be filled....it is assumed that the model is empty.
	 */
	static void populateTestModel(ChordChangeConsonanceModel model) {
		final int halfChordSigIndex = ChordSignature.values().length / 2;
		for(int i=0; i<halfChordSigIndex; i++) {
			ChordSignature startChordSig = ChordSignature.values()[i];
			for(ChordSignature endChordSig : ChordSignature.values()) {
				for(Interval interval : Interval.values()) {
					//a chord change that start and ends with the same chord type
					//and interval between roots is unison represents the same chord
					if(startChordSig.equals(endChordSig) && interval.equals(Interval.UNISON)) {
						continue;
					}
					if( !interval.inFirstOctave()) {
						break;
					}

					//Get a random ConsonanceRating
					ConsonanceRating rating = ConsonanceRating.MEDIOCRE;

					model.addRating(startChordSig, endChordSig, interval, rating);
				}
			}


		}
	}

	/**
	 * Main data structures created before each test.
	 * It will be empty before every test.
	 */
	ChordChangeConsonanceModel cccModel,otherCCCmodel;

	/**
	 * Create an empty chord change consonance model before
	 * each test.
	 */
	@BeforeEach
	void init() {
		cccModel = new ChordChangeConsonanceModel();
		otherCCCmodel = new ChordChangeConsonanceModel();
	}

	/**
	 * Make sure that a string containing a chord signature is
	 * read and written properly.
	 */
	@Test
	void testReadChordSignatureString() {
		ChordSignature readChordSig;
		final ChordSignature writtenChordSig = ChordSignature.MAJOR;

		//Write a startChordSignatureString
		String codedLine = 
				ChordChangeConsonanceModel.createChordSignatureString(true, writtenChordSig);		
		readChordSig = ChordChangeConsonanceModel.readChordSignatureFromLine(codedLine);
		assertEquals(writtenChordSig,readChordSig);

		//write an end chord signature string.
		codedLine = 
				ChordChangeConsonanceModel.createChordSignatureString(false, writtenChordSig);
		readChordSig = ChordChangeConsonanceModel.readChordSignatureFromLine(codedLine);
		assertEquals(writtenChordSig,readChordSig);
	}

	/**
	 * Code a String containing an interval and a rating and
	 * make sure that the data can be read properly
	 */
	@Test
	void testParseIntervalAndRating() {
		Interval readInterval;
		ConsonanceRating readRating;

		final Interval writtenInterval = Interval.PERFECT5;
		final ConsonanceRating writtenRating = ConsonanceRating.GOOD;

		final String codedLine = 
				ChordChangeConsonanceModel.createIntervalRatingString(
						writtenInterval, 
						writtenRating);

		readInterval = ChordChangeConsonanceModel.parseIntervalFromIntervalRatingLine(codedLine);
		assertEquals(writtenInterval,readInterval);

		readRating = ChordChangeConsonanceModel.parseRatingFromIntervalRatingLine(codedLine);
		assertEquals(writtenRating,readRating);
	}

	/**
	 * Make sure that we can save to file and load from file
	 * properly.
	 * @throws FileNotFoundException
	 */
	@Test
	void testSavingToAndLoadingFromFile() throws FileNotFoundException {
		ChordChangeConsonanceModel modelLoadedFromFile;

		populateTestModel(cccModel);

		ChordChangeConsonanceModel.saveToFile(cccModel, testFileName);
		modelLoadedFromFile = ChordChangeConsonanceModel.loadFromFile(testFileName);

		//Make sure we get rid of the file
		File createdFile = new File(testFileName);
		createdFile.delete();

		assertEquals(cccModel,modelLoadedFromFile);
	}


	/**
	 * Make sure .equals works
	 */
	@Test
	void testEquals() {
		populateTestModel(cccModel);
		populateTestModel(otherCCCmodel);

		assertTrue(cccModel.equals(otherCCCmodel));
		assertEquals(cccModel,otherCCCmodel);
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
