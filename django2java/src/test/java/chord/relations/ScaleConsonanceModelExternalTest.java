package chord.relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;
import chord.relations.record.ScaleConsonanceRecord;
import chord.relations.request.ScaleConsonanceRecordRequest;

public class ScaleConsonanceModelExternalTest {
	/**
	 * Take the given ScaleConsonanceModel and fill it with a consistent
	 * rating every time
	 * @param full fill the data structure if true, half fill if false
	 * @param model to be filled....it is assumed that the model is empty.
	 */
	static void populateTestModel(boolean full, ScaleConsonanceModel model) {	
		final int halfChordSigIndex = ChordSignature.values().length / 2;
		final int fullChordSigIndex = ChordSignature.values().length;

		final int endIndex = full?fullChordSigIndex:halfChordSigIndex;
		for(int i=0; i<endIndex; i++) {
			ChordSignature chordSig = ChordSignature.values()[i];

			for(ScaleSignature scaleSig : ScaleSignature.values()) {

				ConsonanceRating rating = ConsonanceRating.MEDIOCRE;
				
				ScaleConsonanceRecord recordToBeAdded = 
						new ScaleConsonanceRecord(chordSig,scaleSig,rating);

				model.addRating(recordToBeAdded);
			}
		}
	}
	
	/**
	 * Main data model used for tests.
	 * Initialized before every test.
	 */
	ScaleConsonanceModel scModel,otherModel;
	ScaleConsonanceRecord scRecord;

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
	 * Add a ScaleConsonanceRecord with a null rating and
	 * make sure that an exception is thrown.
	 */
	@Test
	void testAddRatingNullRating() {
		ScaleConsonanceRecord record = 
				new ScaleConsonanceRecord(
						ChordSignature._10,
						ScaleSignature.AEOLIAN,
						null);
		
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> scModel.addRating(record));
	}
	
	/**
	 * Make sure that the addRating method follows the interface
	 * defined in RatingModel<T>
	 */
	@Test
	void testAddRating() {
		ScaleConsonanceRecord recordAdded,recordReturned,recordRetrieved,recordUsedToOverwrite;
		recordAdded =
				new ScaleConsonanceRecord(
						ChordSignature._10,
						ScaleSignature.AEOLIAN,
						ConsonanceRating.GOOD);
		
		
		//add a new rating and make sure that it returns null
		//in accordance with the RatingModel<T> interface.
		recordReturned = scModel.addRating(recordAdded);
		assertNull(recordReturned);
		
		//Make sure that the rating was added and is equal to the
		//record added
		recordRetrieved = scModel.getRating(recordAdded);
		assertEquals(recordAdded,recordRetrieved);
		
		//add a new rating overtop of an existing rating and
		//make sure it returns the old rating
		//MAKE SURE THAT THIS CONSTRUCTOR TAKES A DIFFERENT
		//CONSONANCERATING THAN THE recordAdded variable
		recordUsedToOverwrite = 
				new ScaleConsonanceRecord(
						recordAdded.chordSignature(),
						recordAdded.scaleSignature(),
						ConsonanceRating.BAD);
		recordReturned = scModel.addRating(recordUsedToOverwrite);
		
		assertEquals(recordAdded,recordReturned);
		
		//Once again make sure that the rating was added successfully
		recordRetrieved = scModel.getRating(recordUsedToOverwrite);
		assertEquals(recordUsedToOverwrite,recordRetrieved);
	}
	
	/**
	 * Make sure that the removeRating method follows the interface
	 * defined in RatingModel<T>
	 */
	@Test
	void testRemoveRating() {
		ScaleConsonanceRecord recordAdded,recordRemoved;
		recordAdded =
				new ScaleConsonanceRecord(
						ChordSignature._10,
						ScaleSignature.AEOLIAN,
						ConsonanceRating.GOOD);
		
		//Make sure the model returns null if the rating doesn't exist
		recordRemoved = scModel.getRating(recordAdded);
		assertNull(recordRemoved);
		
		scModel.addRating(recordAdded);
		recordRemoved = scModel.removeRating(recordAdded);
		
		assertEquals(recordAdded,recordRemoved);
		
		//Make sure that the rating was removed successfully
		assertNull(scModel.getRating(recordAdded));
	}
	
	@Test
	void testGetRating() {
		ScaleConsonanceRecord recordAdded,recordRetrieved;
		recordAdded =
				new ScaleConsonanceRecord(
						ChordSignature._10,
						ScaleSignature.AEOLIAN,
						ConsonanceRating.GOOD);
		
		//Make sure getRating returns null on nonexistent
		//records
		assertNull(scModel.getRating(recordAdded));
		
		scModel.addRating(recordAdded);
		
		recordRetrieved = scModel.getRating(recordAdded);
		
		assertEquals(recordAdded,recordRetrieved);
	}
	
	
	/**
	 * Make sure that the getNextRecordToBeRated function
	 * works on an empty data model
	 */
	@Test
	void testGetNextRecordToBeRatedEmpty() {
		final ScaleConsonanceRecord record = 
				scModel.getNextRecordToBeRated();

		assertEquals(ChordSignature.firstSignature(), record.chordSignature());
		assertEquals(ScaleSignature.firstSignature(),record.scaleSignature());
	}

	/**
	 * Fill a ScaleConsonanceModel and make sure that
	 * the getNextNoteRecordToBeRated returns null.
	 */
	@Test
	void testGetNextRecordToBeRatedFull() {
		populateTestModel(true,scModel);

		final ScaleConsonanceRecord record = 
				scModel.getNextRecordToBeRated();

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
		final ScaleSignature addedScaleSig = ScaleSignature.firstSignature();
		scRecord = new ScaleConsonanceRecord(addedChordSig, addedScaleSig, ConsonanceRating.MEDIOCRE);
		scModel.addRating(scRecord);

		final ScaleConsonanceRecord nextNoteRecord = 
				scModel.getNextRecordToBeRated();

		assertEquals(addedChordSig,nextNoteRecord.chordSignature());
		assertEquals(addedScaleSig.getNextScale(),nextNoteRecord.scaleSignature());
	}

	/**
	 * Fill all ratings for a single chord and make sure that
	 * the getNextRecordToBeRated method returns the next
	 * chord signature and a unison interval.
	 */
	@Test
	void testGetNextRecordToBeRatedRolloverOfChord() {
		final ChordSignature addedChordSig = ChordSignature.firstSignature();
		final ConsonanceRating addedRating = ConsonanceRating.GOOD;
		
		for(ScaleSignature scaleSig : ScaleSignature.values()) {
			scRecord = new ScaleConsonanceRecord(addedChordSig, scaleSig, addedRating); 

			scModel.addRating(scRecord);
		}

		final ScaleConsonanceRecord nextToBeRatedRecord =
				scModel.getNextRecordToBeRated();

		assertEquals(
				addedChordSig.getNextChordSignature(),
				nextToBeRatedRecord.chordSignature());
		assertEquals(
				ScaleSignature.firstSignature(),
				nextToBeRatedRecord.scaleSignature());
	}

	/**
	 * Get the last note consonance record of an empty collection
	 * and make sure that it is null.
	 */
	@Test
	void testGetLastRatedRecordEmpty() {
		final ScaleConsonanceRecord lastRatedScaleConsonanceRecord= 
				scModel.getLastRecordRated();

		assertNull(lastRatedScaleConsonanceRecord);
	}

	/**
	 * Fill a NoteConsonanceModel and make sure that
	 * the last rating added contains the last ChordSignature
	 * and the last ScaleSignature
	 */
	@Test
	void testGetLastRatedRecordFull() {
		final ChordSignature lastChordSignature = 
				ChordSignature.lastSignature();
		final ScaleSignature lastScaleSignature =
				ScaleSignature.lastSignature();

		populateTestModel(true, scModel);

		final ScaleConsonanceRecord lastRatingRecord =
				scModel.getLastRecordRated();

		assertEquals(
				lastChordSignature,
				lastRatingRecord.chordSignature());
		assertEquals(
				lastScaleSignature,
				lastRatingRecord.scaleSignature());
	}

	/**
	 * Add a rating and make sure that it is returned from 
	 * the getLastRecordRated function.
	 */
	@Test
	void testGetLastRatedRecordNormalEntry() {
		final ChordSignature addedChordSig = ChordSignature.firstSignature();
		final ScaleSignature addedScaleSig = ScaleSignature.firstSignature();
		final ConsonanceRating addedRating = ConsonanceRating.BAD;

		scRecord = new ScaleConsonanceRecord(addedChordSig, addedScaleSig, addedRating);
		scModel.addRating(scRecord);

		ScaleConsonanceRecord addedRecord = 
				scModel.getLastRecordRated();

		assertEquals(addedChordSig,addedRecord.chordSignature());
		assertEquals(addedScaleSig,addedRecord.scaleSignature());
		assertEquals(addedRating,addedRecord.rating());

		//Make sure that the model returns null after
		//values have been removed.
		scModel.removeRating(addedChordSig, addedScaleSig);
		ScaleConsonanceRecord lastRecordAfterRemoval =
				scModel.getLastRecordRated();

		assertNull(lastRecordAfterRemoval);
	}

	/**
	 * Fill all ratings for a single chord and make sure
	 * that the getLastRatingAdded method returns chordSig/lastScalSignature
	 */
	@Test
	void testGetRecordOfLastNoteConsonanceRatedRollover() {
		final ChordSignature addedChordSig = 
				ChordSignature.firstSignature();
		final ConsonanceRating addedRating =
				ConsonanceRating.GOOD;

		//fill all ratings for first chord signature
		//and all scale Signatures
		for(ScaleSignature scaleSig : ScaleSignature.values()) {
			scModel.addRating(new ScaleConsonanceRecord(addedChordSig,scaleSig,addedRating));
		}

		final ScaleConsonanceRecord lastScaleRatedRecord = 
				scModel.getLastRecordRated();

		assertEquals(
				addedChordSig,
				lastScaleRatedRecord.chordSignature());
		assertEquals(
				ScaleSignature.lastSignature(),
				lastScaleRatedRecord.scaleSignature());
	}

	/**
	 * Make sure that the isEmpty method works
	 * with initially empty, a single entry,
	 * and after all records removed.
	 */
	@Test
	void testIsEmpty() {
		final ScaleConsonanceRecord recordToAdd = 
				new ScaleConsonanceRecord(
						ChordSignature._10,
						ScaleSignature.firstSignature(),
						ConsonanceRating.BAD);
		
		//initial empty list
		assertTrue(scModel.isEmpty());
		
		//one record added
		scModel.addRating(recordToAdd);
		assertFalse(scModel.isEmpty());
		
		//test after added rating removed
		scModel.removeRating(recordToAdd);
		assertTrue(scModel.isEmpty());
	}
	
	@Test
	void testIsFull() {
		//empty model
		assertFalse(scModel.isFull());
		
		//partially filled model
		populateTestModel(false, scModel);
		assertFalse(scModel.isFull());
		
		//full model
		populateTestModel(true,otherModel);
		assertTrue(otherModel.isFull());
		
		//one rating removed model
		otherModel.removeRating(otherModel.getLastRecordRated());
		assertFalse(otherModel.isFull());
		
		//Rating removed out of order
		ScaleConsonanceRecord recordToRemoveOutOfOrder = 
				new ScaleConsonanceRecord(
						ChordSignature.MAJ_ADD11,
						ScaleSignature.AEOLIAN,
						null);
		
		ScaleConsonanceModel modelWithRecordRemovedOutOfOrder = 
				new ScaleConsonanceModel();
				
		populateTestModel(true,modelWithRecordRemovedOutOfOrder);
		
		modelWithRecordRemovedOutOfOrder.removeRating(recordToRemoveOutOfOrder);
		assertFalse(modelWithRecordRemovedOutOfOrder.isFull());
	}
	
	@Test
	void testEquals() {
		populateTestModel(false,scModel);
		populateTestModel(false,otherModel);
		//test a half filled model, both same
		assertEquals(scModel,otherModel);
		
		ScaleConsonanceRecord recordRemoved = 
				new ScaleConsonanceRecord(
						ChordSignature.firstSignature(), 
						ScaleSignature.firstSignature(), 
						null);
		//test two partially filled models,
		//not equal
		scModel.removeRating(recordRemoved);
		assertNotEquals(scModel, otherModel);
		
		//test two partially filled models after
		//removal of same record
		otherModel.removeRating(recordRemoved);
		assertEquals(scModel,otherModel);
		
		//test two completely filled models.
		scModel = new ScaleConsonanceModel();
		otherModel = new ScaleConsonanceModel();

		populateTestModel(true,scModel);
		populateTestModel(true,otherModel);
		assertEquals(scModel,otherModel);
	}
	
	
	@Test
	void testRequestMethod() {
		populateTestModel(true,scModel);
		
		ScaleConsonanceRecordRequest request = 
				ScaleConsonanceRecordRequest.allPossibleRecords();
		
		//We are loading the records into another model to
		//make sure that all of the records have been retrieved.
		Set<ScaleConsonanceRecord> recordsRetrieved = scModel.getRecords(request);
		
		for(ScaleConsonanceRecord record : recordsRetrieved) {
			otherModel.addRating(record);
		}
		
		//if everything was retrieved properly
		//the two models should be equal
		assertEquals(scModel,otherModel);
	}
}
