package chord.relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;

/**
 * This test class is used to test the external interface of the
 * NoteConsonanceModel.
 * @author DAD
 *
 */
public class NoteConsonanceModelExternalTest {
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
			ChordSignature chordSig = ChordSignature.values()[i];

			for(Interval interval : Interval.values()) {
				if( !interval.inFirstOctave()) {
					break;
				}

				ConsonanceRating rating = ConsonanceRating.MEDIOCRE;
				
				NoteConsonanceRecord recordToBeAdded = 
						new NoteConsonanceRecord(chordSig,interval,rating);

				model.addRating(recordToBeAdded);
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
	 * Add a NoteConsonanceRecord with a null rating and
	 * make sure that an exception is thrown.
	 */
	@Test
	void testAddRatingNullRating() {
		NoteConsonanceRecord record = 
				new NoteConsonanceRecord(
						ChordSignature._10,
						Interval.UNISON,
						null);
		
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> ncModel.addRating(record));
	}
	
	/**
	 * Make sure that the addRating method follows the interface
	 * defined in RatingModel<T>
	 */
	@Test
	void testAddRating() {
		NoteConsonanceRecord recordAdded,recordReturned,recordRetrieved,recordUsedToOverwrite;
		recordAdded =
				new NoteConsonanceRecord(
						ChordSignature._10,
						Interval.UNISON,
						ConsonanceRating.GOOD);
		
		
		//add a new rating and make sure that it returns null
		//in accordance with the RatingModel<T> interface.
		recordReturned = ncModel.addRating(recordAdded);
		assertNull(recordReturned);
		
		//Make sure that the rating was added and is equal to the
		//record added
		recordRetrieved = ncModel.getRating(recordAdded);
		assertEquals(recordAdded,recordRetrieved);
		
		//add a new rating overtop of an existing rating and
		//make sure it returns the old rating
		//MAKE SURE THAT THIS CONSTRUCTOR TAKES A DIFFERENT
		//CONSONANCERATING THAN THE recordAdded variable
		recordUsedToOverwrite = 
				new NoteConsonanceRecord(
						recordAdded.chordSignature(),
						recordAdded.interval(),
						ConsonanceRating.BAD);
		recordReturned = ncModel.addRating(recordUsedToOverwrite);
		
		assertEquals(recordAdded,recordReturned);
		
		//Once again make sure that the rating was added successfully
		recordRetrieved = ncModel.getRating(recordUsedToOverwrite);
		assertEquals(recordUsedToOverwrite,recordRetrieved);
	}
	
	/**
	 * Make sure that the removeRating method follows the interface
	 * defined in RatingModel<T>
	 */
	@Test
	void testRemoveRating() {
		NoteConsonanceRecord recordAdded,recordRemoved;
		recordAdded =
				new NoteConsonanceRecord(
						ChordSignature._10,
						Interval.UNISON,
						ConsonanceRating.GOOD);
		
		//Make sure the model returns null if the rating doesn't exist
		recordRemoved = ncModel.getRating(recordAdded);
		assertNull(recordRemoved);
		
		ncModel.addRating(recordAdded);
		recordRemoved = ncModel.removeRating(recordAdded);
		
		assertEquals(recordAdded,recordRemoved);
		
		//Make sure that the rating was removed successfully
		assertNull(ncModel.getRating(recordAdded));
	}
	
	@Test
	void testGetRating() {
		NoteConsonanceRecord recordAdded,recordRetrieved;
		recordAdded =
				new NoteConsonanceRecord(
						ChordSignature._10,
						Interval.UNISON,
						ConsonanceRating.GOOD);
		
		//Make sure getRating returns null on nonexistent
		//records
		assertNull(ncModel.getRating(recordAdded));
		
		ncModel.addRating(recordAdded);
		
		recordRetrieved = ncModel.getRating(recordAdded);
		
		assertEquals(recordAdded,recordRetrieved);
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