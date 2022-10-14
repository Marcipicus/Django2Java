package chord.relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import chord.ConsonanceRating;
import chord.Interval;

class NoteRatingTest {

	/**
	 * List of all of the valid values for NoteRating constructor.
	 * @return
	 */
	private static Stream<Arguments> validIntervalFromRootValuesForConstructor(){
		return Stream.of(
				Arguments.of(Interval.UNISON),
				Arguments.of(Interval.MINOR2),
				Arguments.of(Interval.MAJOR2),
				Arguments.of(Interval.MINOR3),
				Arguments.of(Interval.MAJOR3),
				Arguments.of(Interval.PERFECT4),
				Arguments.of(Interval.DIMINISHED5),
				Arguments.of(Interval.PERFECT5),
				Arguments.of(Interval.MINOR6),
				Arguments.of(Interval.MAJOR6),
				Arguments.of(Interval.MINOR7),
				Arguments.of(Interval.MAJOR7)
				);
	}

	/**
	 * List of all of the valid values for NoteRating constructor.
	 * @return
	 */
	private static Stream<Arguments> invalidIntervalFromRootValuesForConstructor(){
		return Stream.of(
				Arguments.of(Interval.PERFECT8),
				Arguments.of(Interval.MINOR9),
				Arguments.of(Interval.MAJOR9),
				Arguments.of(Interval.MINOR10),
				Arguments.of(Interval.MAJOR10),
				Arguments.of(Interval.PERFECT11),
				Arguments.of(Interval.DIMINISHED12),
				Arguments.of(Interval.PERFECT12),
				Arguments.of(Interval.MINOR13),
				Arguments.of(Interval.MAJOR13),
				Arguments.of(Interval.MINOR14),
				Arguments.of(Interval.MAJOR14),
				Arguments.of(Interval.PERFECT15)
				);
	}

	@ParameterizedTest
	@MethodSource("validIntervalFromRootValuesForConstructor")
	void testConstructorValidIntervals(Interval intervalFromRootOfChord) {
		for(ConsonanceRating rating : ConsonanceRating.values()) {
			NoteRating testRating = new NoteRating(intervalFromRootOfChord,rating);
		}
	}

	@ParameterizedTest
	@MethodSource("invalidIntervalFromRootValuesForConstructor")
	void testConstructorInvalidIntervals(Interval intervalFromRootOfChord) {
		for(ConsonanceRating rating : ConsonanceRating.values()) {
			Exception exception = 
					assertThrows(IllegalArgumentException.class, 
							() -> new NoteRating(intervalFromRootOfChord,rating));
		}
	}

	@Test
	void testNullPassedToConstructor() {
		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> new NoteRating(null,ConsonanceRating.BAD));

		exception = 
				assertThrows(NullPointerException.class, 
						() -> new NoteRating(Interval.UNISON,null));
	}

	//TODO: Find out how to create a parameterized test that tests
	//for all combinations of multiple parameter lists
	@ParameterizedTest
	@MethodSource("validIntervalFromRootValuesForConstructor")
	void testEqualsMethod(Interval intervalFromRootOfChord) {
		NoteRating ratingOne,ratingTwo;

		for(ConsonanceRating rating : ConsonanceRating.values() ) {
			ratingOne = new NoteRating(intervalFromRootOfChord,rating);
			ratingTwo = new NoteRating(intervalFromRootOfChord,rating);

			assertEquals(ratingOne,ratingTwo);
		}
		
		ratingOne = new NoteRating(Interval.UNISON,ConsonanceRating.BAD);
		assertEquals(ratingOne,ratingOne);
	}

	@Test
	void testEqualsMethodUnequalValues() {
		NoteRating ratingOne,ratingTwo;
		ratingOne = new NoteRating(Interval.UNISON,ConsonanceRating.BAD);
		assertFalse(ratingOne.equals(null));

		ratingOne = new NoteRating(Interval.UNISON,ConsonanceRating.BAD);
		ratingTwo = new NoteRating(Interval.UNISON,ConsonanceRating.GOOD);
		assertNotEquals(ratingOne,ratingTwo);

		ratingOne = new NoteRating(Interval.UNISON,ConsonanceRating.BAD);
		ratingTwo = new NoteRating(Interval.MAJOR2,ConsonanceRating.BAD);
		assertNotEquals(ratingOne,ratingTwo);
		
	}
	
	@Test
	void testGetNoteRating() {
		Interval intervalForNoteRating = Interval.UNISON;
		ConsonanceRating rating = ConsonanceRating.GOOD;
		
		NoteRating testRating = new NoteRating(intervalForNoteRating,rating);
		
		assertEquals(rating, testRating.getRating());
	}

	@Test
	void testGetNoteInterval() {
		Interval intervalForNoteRating = Interval.UNISON;
		ConsonanceRating rating = ConsonanceRating.GOOD;
		
		NoteRating testRating = new NoteRating(intervalForNoteRating,rating);
		
		assertEquals(intervalForNoteRating, testRating.getNoteInterval());
	}
	
	@Test
	void testEquals_Symmetric() {
		NoteRating noteRating1,noteRating2;
		noteRating1 = new NoteRating(Interval.UNISON,ConsonanceRating.BAD);
		noteRating2 = new NoteRating(Interval.UNISON,ConsonanceRating.BAD);
		
		assertTrue(noteRating1.equals(noteRating2) && noteRating2.equals(noteRating1));
		assertTrue(noteRating1.hashCode() == noteRating2.hashCode());
	}
}
