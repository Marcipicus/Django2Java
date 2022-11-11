package chord.relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;

public class NoteConsonanceModelTest {
	//.tmp file name used to make git ignore on checkins if
	//we forget to delete the test file
	final String testFileName = "testNoteConsonanceFile.tmp";

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
	 * @param model to be filled....it is assumed that the model is empty.
	 */
	static void populateTestModel(NoteConsonanceModel model) {
		final int halfChordSigIndex = ChordSignature.values().length / 2;
		for(int i=0; i<halfChordSigIndex; i++) {
			ChordSignature chorSig = ChordSignature.values()[i];
			
			for(Interval interval : Interval.values()) {
				if( !interval.inFirstOctave()) {
					break;
				}
				
				//Get a random ConsonanceRating
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
	 * Make sure that a string containing a chord signature is
	 * read and written properly.
	 */
	@Test
	void testReadChordSignatureString() {
		ChordSignature readChordSig;

		final ChordSignature writtenChordSig = ChordSignature.MAJOR;
		final String codedLine = NoteConsonanceModel.createChordSignatureString(writtenChordSig);
		
		readChordSig = NoteConsonanceModel.readChordSignatureFromLine(codedLine);
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
				NoteConsonanceModel.createIntervalRatingString(
						writtenInterval, 
						writtenRating);
		
		readInterval = NoteConsonanceModel.parseIntervalFromIntervalRatingLine(codedLine);
		assertEquals(writtenInterval,readInterval);
		
		readRating = NoteConsonanceModel.parseRatingFromIntervalRatingLine(codedLine);
		assertEquals(writtenRating,readRating);
	}
	
	/**
	 * Make sure that we can save to file and load from file
	 * properly.
	 * @throws FileNotFoundException
	 */
	@Test
	void testSavingToAndLoadingFromFile() throws FileNotFoundException {
		NoteConsonanceModel modelLoadedFromFile;
		
		populateTestModel(ncModel);
		
		NoteConsonanceModel.saveToFile(ncModel, testFileName);
		modelLoadedFromFile = NoteConsonanceModel.loadFromFile(testFileName);
		
		//Make sure we get rid of the file
		File createdFile = new File(testFileName);
		createdFile.delete();
		
		assertEquals(ncModel,modelLoadedFromFile);
	}
	
	/**
	 * Make sure .equals works
	 */
	@Test
	void testEquals() {
		populateTestModel(ncModel);
		populateTestModel(otherModel);
		
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

}