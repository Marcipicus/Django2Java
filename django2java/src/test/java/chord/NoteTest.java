package chord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import chord.Interval;
import chord.NoteName;

public class NoteTest{

	private NoteName testNote,comparisonNote;

	private NoteName intervalNote;
	
	private String expectedMessage;
	
	public NoteTest() {
		
	}

	private static void testNoteStringForKey(NoteName note, String expectedValue) {
		assertEquals(note.toString(),expectedValue);
	}
	
	@Test
	public void testToString() {
		testNoteStringForKey(NoteName.C,"C");

		testNoteStringForKey(NoteName.C_SHARP,"C#");

		testNoteStringForKey(NoteName.D,"D");

		testNoteStringForKey(NoteName.D_SHARP,"D#");

		testNoteStringForKey(NoteName.E,"E");

		testNoteStringForKey(NoteName.F,"F");

		testNoteStringForKey(NoteName.F_SHARP,"F#");

		testNoteStringForKey(NoteName.G,"G");

		testNoteStringForKey(NoteName.G_SHARP,"G#");

		testNoteStringForKey(NoteName.A,"A");

		testNoteStringForKey(NoteName.A_SHARP,"A#");

		testNoteStringForKey(NoteName.B,"B");
		
	}
	
	
	
	@Test
	public void testIncrementOneSemitone() {
		//Begin at first note
		testNote = NoteName.A;

		//increment one at a time through notes and make sure they are all good
		testNote = testNote.incrementOneSemitone();
		assertEquals(testNote,NoteName.A_SHARP);

		testNote = testNote.incrementOneSemitone();
		assertEquals(testNote,NoteName.B);

		testNote = testNote.incrementOneSemitone();
		assertEquals(testNote,NoteName.C);

		testNote = testNote.incrementOneSemitone();
		assertEquals(testNote,NoteName.C_SHARP);

		testNote = testNote.incrementOneSemitone();
		assertEquals(testNote,NoteName.D);

		testNote = testNote.incrementOneSemitone();
		assertEquals(testNote,NoteName.D_SHARP);

		testNote = testNote.incrementOneSemitone();
		assertEquals(testNote,NoteName.E);

		testNote = testNote.incrementOneSemitone();
		assertEquals(testNote,NoteName.F);

		testNote = testNote.incrementOneSemitone();
		assertEquals(testNote,NoteName.F_SHARP);

		testNote = testNote.incrementOneSemitone();
		assertEquals(testNote,NoteName.G);

		testNote = testNote.incrementOneSemitone();
		assertEquals(testNote,NoteName.G_SHARP);

		//we test the rollover from G# to A on another test
	}

	@Test
	public void testRollOverOnIncrementOneSemitone() {
		testNote = testNote.G_SHARP;

		testNote = testNote.incrementOneSemitone();

		assertEquals(testNote,NoteName.A);
	}


	@Test
	public void testDecrementOneSemitone() {
		//Begin at first note
		testNote = NoteName.G_SHARP;


		//increment one at a time through notes and make sure they are all good
		testNote = testNote.decrementOneSemitone();
		assertEquals(testNote,NoteName.	G);

		testNote = testNote.decrementOneSemitone();
		assertEquals(testNote,NoteName.F_SHARP);

		testNote = testNote.decrementOneSemitone();
		assertEquals(testNote,NoteName.F);

		testNote = testNote.decrementOneSemitone();
		assertEquals(testNote,NoteName.E);

		testNote = testNote.decrementOneSemitone();
		assertEquals(testNote,NoteName.D_SHARP);

		testNote = testNote.decrementOneSemitone();
		assertEquals(testNote,NoteName.D);

		testNote = testNote.decrementOneSemitone();
		assertEquals(testNote,NoteName.C_SHARP);

		testNote = testNote.decrementOneSemitone();
		assertEquals(testNote,NoteName.C);

		testNote = testNote.decrementOneSemitone();
		assertEquals(testNote,NoteName.B);

		testNote = testNote.decrementOneSemitone();
		assertEquals(testNote,NoteName.A_SHARP);

		testNote = testNote.decrementOneSemitone();
		assertEquals(testNote,NoteName.A);

		//we test the rollover from A to G# on another test
	}


	@Test
	public void testRollOverOnDecrementtOneSemitone() {
		testNote = testNote.A;

		testNote = testNote.decrementOneSemitone();

		assertEquals(testNote,NoteName.G_SHARP);
	}

	@Test
	public void testGetNoteByInterval() {
		// test method

		testNote = NoteName.C;

		intervalNote = testNote.getNoteByInterval(Interval.UNISON);
		assertEquals(intervalNote, NoteName.C);

		intervalNote = testNote.getNoteByInterval(Interval.MINOR2);
		assertEquals(intervalNote, NoteName.C_SHARP );

		intervalNote = testNote.getNoteByInterval(Interval.MAJOR2);
		assertEquals(intervalNote, NoteName.D );

		intervalNote = testNote.getNoteByInterval(Interval.MINOR3);
		assertEquals(intervalNote, NoteName.D_SHARP );

		intervalNote = testNote.getNoteByInterval(Interval.MAJOR3);
		assertEquals(intervalNote, NoteName.E );

		intervalNote = testNote.getNoteByInterval(Interval.PERFECT4);
		assertEquals(intervalNote, NoteName.F );

		intervalNote = testNote.getNoteByInterval(Interval.DIMINISHED5);
		assertEquals(intervalNote, NoteName.F_SHARP );

		intervalNote = testNote.getNoteByInterval(Interval.PERFECT5);
		assertEquals(intervalNote, NoteName.G );

		intervalNote = testNote.getNoteByInterval(Interval.MINOR6);
		assertEquals(intervalNote, NoteName.G_SHARP );

		intervalNote = testNote.getNoteByInterval(Interval.MAJOR6);
		assertEquals(intervalNote, NoteName.A );

		intervalNote = testNote.getNoteByInterval(Interval.MINOR7);
		assertEquals(intervalNote, NoteName.A_SHARP );

		intervalNote = testNote.getNoteByInterval(Interval.MAJOR7);
		assertEquals(intervalNote, NoteName.B );
	}

	@Test
	public void testGetNoteByIntervalPassedNull() {
		testNote = NoteName.C;

		IllegalArgumentException illArgExc = 
				assertThrows(IllegalArgumentException.class, () -> {
					testNote.getNoteByInterval(null);
				});
		
		assertEquals(illArgExc.getMessage(),NoteName.INTERVAL_MAY_NOT_BE_NULL_MESSAGE);
	}
	
	@Test
	public void testEqualsGivenEqualNotes() {
		testNote = NoteName.A;
		comparisonNote = NoteName.A;
		
		assertEquals(testNote,comparisonNote);
	}
	
	@Test
	public void testEqualsGivenUnEqualNotes() {
		testNote = NoteName.A;
		comparisonNote = NoteName.B;
		
		assertNotEquals(testNote,comparisonNote);
	}


}
