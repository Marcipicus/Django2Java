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
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.record.ChordChangeConsonanceRecord;
import chord.relations.request.ChordChangeConsonanceRecordRequest;

public class ChordChangeConsonanceModelExternalTest {
	/**
	 * Take the given ChordChangeConsonanceModel and fill it with a consistent
	 * rating every time
	 * @param full fill the data structure if true, half fill if false
	 * @param model to be filled....it is assumed that the model is empty.
	 */
	static void populateTestModel(boolean full, ChordChangeConsonanceModel model) {	
		final int halfChordSigIndex = ChordSignature.values().length / 2;
		final int fullChordSigIndex = ChordSignature.values().length;

		final int endIndex = full?fullChordSigIndex:halfChordSigIndex;
		for(int i=0; i<endIndex; i++) {
			ChordSignature startChordSig = ChordSignature.values()[i];

			for(ChordSignature endChordSig : ChordSignature.values()) {
				for(Interval interval : Interval.values()) {
					if( !interval.inFirstOctave()) {
						break;
					}
					if(startChordSig.equals(endChordSig) && interval.equals(Interval.UNISON)) {
						continue;
					}

					ChordChangeConsonanceRecord recordToAdd = 
							new ChordChangeConsonanceRecord(
									startChordSig, 
									endChordSig, 
									interval,
									ConsonanceRating.GOOD);

					model.addRating(recordToAdd);

				}
			}
		}
	}

	/**
	 * Main data model used for tests.
	 * Initialized before every test.
	 */
	ChordChangeConsonanceModel cccModel,otherModel;
	ChordChangeConsonanceRecord cccRecord;

	/**
	 * Initialization code for main data model.
	 * Data model initialized and unmodified.
	 */
	@BeforeEach
	void init() {
		cccModel = new ChordChangeConsonanceModel();
		otherModel = new ChordChangeConsonanceModel();
	}

	/**
	 * Add a ChordChangeConsonanceRecord with a null rating and
	 * make sure that an exception is thrown.
	 */
	@Test
	void testAddRatingNullRating() {
		ChordChangeConsonanceRecord record = 
				new ChordChangeConsonanceRecord(
						ChordSignature._10,
						ChordSignature.MAJOR,
						Interval.MAJOR3,
						null);

		Exception exception = 
				assertThrows(NullPointerException.class, 
						() -> cccModel.addRating(record));
	}

	/**
	 * Make sure that the addRating method follows the interface
	 * defined in RatingModel<T>
	 */
	@Test
	void testAddRating() {
		ChordChangeConsonanceRecord recordAdded,recordReturned,recordRetrieved,recordUsedToOverwrite;
		recordAdded =
				new ChordChangeConsonanceRecord(
						ChordSignature._10,
						ChordSignature.MAJOR,
						Interval.MAJOR3,
						ConsonanceRating.GOOD);


		//add a new rating and make sure that it returns null
		//in accordance with the RatingModel<T> interface.
		recordReturned = cccModel.addRating(recordAdded);
		assertNull(recordReturned);

		//Make sure that the rating was added and is equal to the
		//record added
		recordRetrieved = cccModel.getRating(recordAdded);
		assertEquals(recordAdded,recordRetrieved);

		//add a new rating overtop of an existing rating and
		//make sure it returns the old rating
		//MAKE SURE THAT THIS CONSTRUCTOR TAKES A DIFFERENT
		//CONSONANCERATING THAN THE recordAdded variable
		recordUsedToOverwrite = 
				new ChordChangeConsonanceRecord(
						recordAdded.startChordSignature(),
						recordAdded.endChordSignature(),
						recordAdded.intervalBetweenRoots(),
						ConsonanceRating.BAD);
		recordReturned = cccModel.addRating(recordUsedToOverwrite);

		assertEquals(recordAdded,recordReturned);

		//Once again make sure that the rating was added successfully
		recordRetrieved = cccModel.getRating(recordUsedToOverwrite);
		assertEquals(recordUsedToOverwrite,recordRetrieved);
	}

	/**
	 * Make sure that the removeRating method follows the interface
	 * defined in RatingModel<T>
	 */
	@Test
	void testRemoveRating() {
		ChordChangeConsonanceRecord recordAdded,recordRemoved;
		recordAdded =
				new ChordChangeConsonanceRecord(
						ChordSignature._10,
						ChordSignature.MAJOR,
						Interval.MAJOR3,
						ConsonanceRating.GOOD);

		//Make sure the model returns null if the rating doesn't exist
		recordRemoved = cccModel.removeRating(recordAdded);
		assertNull(recordRemoved);

		cccModel.addRating(recordAdded);
		recordRemoved = cccModel.removeRating(recordAdded);

		assertEquals(recordAdded,recordRemoved);

		//Make sure that the rating was removed successfully
		assertNull(cccModel.getRating(recordAdded));
	}

	@Test
	void testGetRating() {
		ChordChangeConsonanceRecord recordAdded,recordRetrieved;
		recordAdded =
				new ChordChangeConsonanceRecord(
						ChordSignature._10,
						ChordSignature.MAJOR,
						Interval.MAJOR3,
						ConsonanceRating.GOOD);

		//Make sure getRating returns null on nonexistent
		//records
		assertNull(cccModel.getRating(recordAdded));

		cccModel.addRating(recordAdded);

		recordRetrieved = cccModel.getRating(recordAdded);

		assertEquals(recordAdded,recordRetrieved);
	}


	/**
	 * Make sure that the getNextRecordToBeRated function
	 * works on an empty data model
	 */
	@Test
	void testGetNextRecordToBeRatedEmpty() {
		final ChordChangeConsonanceRecord record = 
				cccModel.getNextRecordToBeRated();

		assertEquals(ChordSignature.firstSignature(), record.startChordSignature());
		assertEquals(ChordSignature.firstSignature(),record.endChordSignature());
		assertEquals(Interval.MINOR2,record.intervalBetweenRoots());
	}

	/**
	 * Fill a ChordChangeConsonanceModel and make sure that
	 * the getNextNoteRecordToBeRated returns null.
	 */
	@Test
	void testGetNextRecordToBeRatedFull() {
		populateTestModel(true,cccModel);

		final ChordChangeConsonanceRecord record = 
				cccModel.getNextRecordToBeRated();

		assertNull(record);
	}

	/**
	 * Add a single rating and make sure that the nextNoteRecord to be rated
	 * has the correct interval. The ChordSignature doesn't change hence the
	 * no rollover of chord.
	 */
	@Test
	void testGetNextRecordToBeRatedNoRolloverOfChord() {
		final ChordSignature startChordSig = ChordSignature.firstSignature();
		final ChordSignature endChordSig = ChordSignature.firstSignature();
		//we are starting with the same start and end chord signatures so
		//initial interval is MINOR2
		final Interval initialInterval = Interval.MINOR2;
		cccRecord = 
				new ChordChangeConsonanceRecord(
						startChordSig,
						endChordSig,
						initialInterval,
						ConsonanceRating.MEDIOCRE);
		cccModel.addRating(cccRecord);

		final ChordChangeConsonanceRecord nextRecord = 
				cccModel.getNextRecordToBeRated();

		assertEquals(startChordSig,nextRecord.startChordSignature());
		assertEquals(endChordSig,nextRecord.endChordSignature());
		assertEquals(initialInterval.getNextInterval(),nextRecord.intervalBetweenRoots());
	}

	/**
	 * Fill all ratings for a single chord and make sure that
	 * the getNextRecordToBeRated method returns the next
	 * chord signature and a unison interval.
	 */
	@Test
	void testGetNextRecordToBeRatedRolloverOfEndChord() {
		final ChordSignature startChordSig = ChordSignature.firstSignature();
		final ChordSignature endChordSig = ChordSignature.firstSignature();
		final ConsonanceRating addedRating = ConsonanceRating.GOOD;
		
		//start and end chord are the same so we skip the first
		//interval since the start and end chord represented are
		//the same
		//we are filling the model with the first start/end chord combination
		//and testing to make sure that the end chord rollover is working
		for(int i = 1 ; ; i++) {
			Interval interval = Interval.values()[i];
			if( !interval.inFirstOctave() ) {
				break;
			}
			cccRecord = 
					new ChordChangeConsonanceRecord(
							startChordSig, 
							endChordSig, 
							interval,
							addedRating); 

			cccModel.addRating(cccRecord);
		}

		final ChordChangeConsonanceRecord nextToBeRatedRecord =
				cccModel.getNextRecordToBeRated();

		assertEquals(startChordSig,nextToBeRatedRecord.startChordSignature());
		assertEquals(endChordSig.getNextChordSignature(),nextToBeRatedRecord.endChordSignature());
		assertEquals(Interval.UNISON,nextToBeRatedRecord.intervalBetweenRoots());
	}

	/**
	 * Fill all ratings for a single chord and make sure that
	 * the getNextRecordToBeRated method returns the next
	 * chord signature and a unison interval.
	 */
	@Test
	void testGetNextRecordToBeRatedRolloverOfStartChord() {
		final ChordSignature startChordSig = ChordSignature.firstSignature();
		final ConsonanceRating addedRating = ConsonanceRating.GOOD;
		
		//add ratings for every end chord/interval combination 
		//so that the next record to rate method will rollover the
		//start chord signature
		for(ChordSignature endChordSig : ChordSignature.values()) {
			for(Interval interval : Interval.values()) {
				if( !interval.inFirstOctave()) {
					break;
				}
				if(startChordSig.equals(endChordSig) && interval.equals(Interval.UNISON)) {
					continue;
				}

				cccRecord = 
						new ChordChangeConsonanceRecord(
								startChordSig, 
								endChordSig, 
								interval,
								ConsonanceRating.GOOD);

				cccModel.addRating(cccRecord);

			}
		}

		final ChordChangeConsonanceRecord nextToBeRatedRecord =
				cccModel.getNextRecordToBeRated();

		assertEquals(
				startChordSig.getNextChordSignature(),
				nextToBeRatedRecord.startChordSignature());
		assertEquals(
				ChordSignature.firstSignature(),
				nextToBeRatedRecord.endChordSignature());
		assertEquals(
				Interval.UNISON,
				nextToBeRatedRecord.intervalBetweenRoots());
	}
	
	/**
	 * Get the last note consonance record of an empty collection
	 * and make sure that it is null.
	 */
	@Test
	void testGetLastRatedRecordEmpty() {
		final ChordChangeConsonanceRecord lastRatedChordChangeConsonanceRecord= 
				cccModel.getLastRecordRated();

		assertNull(lastRatedChordChangeConsonanceRecord);
	}

	/**
	 * Fill a ChordChangeConsonanceModel and make sure that
	 * the last rating added contains the last ChordSignature
	 * and the last ChordSignature
	 */
	@Test
	void testGetLastRatedRecordFull() {
		final ChordSignature lastStartChordSig = 
				ChordSignature.lastSignature();
		final ChordSignature lastEndChordSig =
				ChordSignature.lastSignature();
		final Interval lastInterval = 
				Interval.MAJOR7;
		
		populateTestModel(true, cccModel);

		final ChordChangeConsonanceRecord lastRatingRecord =
				cccModel.getLastRecordRated();

		assertEquals(
				lastStartChordSig,
				lastRatingRecord.startChordSignature());
		assertEquals(
				lastEndChordSig,
				lastRatingRecord.endChordSignature());
		assertEquals(
				lastInterval,lastRatingRecord.intervalBetweenRoots());
	}

	/**
	 * Add a rating and make sure that it is returned from 
	 * the getLastRecordRated function.
	 */
	@Test
	void testGetLastRatedRecordNormalEntry() {
		final ChordSignature addedChordSig = ChordSignature.firstSignature();
		final ChordSignature addedScaleSig = ChordSignature.firstSignature();
		final Interval addedInterval = Interval.MINOR2;
		final ConsonanceRating addedRating = ConsonanceRating.BAD;

		cccRecord = 
				new ChordChangeConsonanceRecord(
						addedChordSig, 
						addedScaleSig, 
						addedInterval, 
						addedRating);
		cccModel.addRating(cccRecord);

		ChordChangeConsonanceRecord addedRecord = 
				cccModel.getLastRecordRated();

		assertEquals(addedChordSig,addedRecord.startChordSignature());
		assertEquals(addedScaleSig,addedRecord.endChordSignature());
		assertEquals(addedInterval, addedRecord.intervalBetweenRoots());
		assertEquals(addedRating,addedRecord.rating());

		//Make sure that the model returns null after
		//values have been removed.
		cccModel.removeRating(addedRecord);
		ChordChangeConsonanceRecord lastRecordAfterRemoval =
				cccModel.getLastRecordRated();

		assertNull(lastRecordAfterRemoval);
	}

	/**
	 * Fill all ratings for a single chord and make sure
	 * that the getLastRatingAdded method returns chordSig/lastScaleSignature
	 */
	@Test
	void testGetRecordOfLastNoteConsonanceRatedRollover() {
		final ChordSignature startChordSig = 
				ChordSignature.firstSignature();
		final ConsonanceRating addedRating =
				ConsonanceRating.GOOD;
		
		for(ChordSignature endChordSig : ChordSignature.values()) {
			for(Interval interval : Interval.valuesInFirstOctave()) {
				if(startChordSig.equals(endChordSig) && interval.equals(Interval.UNISON)) {
					continue;
				}
				cccRecord =
						new ChordChangeConsonanceRecord(
								startChordSig, 
								endChordSig, 
								interval, 
								addedRating);
				cccModel.addRating(cccRecord);
			}
		}

		final ChordChangeConsonanceRecord lastRecordRated = 
				cccModel.getLastRecordRated();

		assertEquals(
				startChordSig,
				lastRecordRated.startChordSignature());
		assertEquals(
				ChordSignature.lastSignature(),
				lastRecordRated.endChordSignature());
		assertEquals(
				Interval.MAJOR7,
				lastRecordRated.intervalBetweenRoots());
	}

	/**
	 * Make sure that the isEmpty method works
	 * with initially empty, a single entry,
	 * and after all records removed.
	 */
	@Test
	void testIsEmpty() {
		final ChordChangeConsonanceRecord recordToAdd = 
				new ChordChangeConsonanceRecord(
						ChordSignature._10,
						ChordSignature.firstSignature(),
						Interval.MAJOR3,
						ConsonanceRating.BAD);

		//initial empty list
		assertTrue(cccModel.isEmpty());

		//one record added
		cccModel.addRating(recordToAdd);
		assertFalse(cccModel.isEmpty());

		//test after added rating removed
		cccModel.removeRating(recordToAdd);
		assertTrue(cccModel.isEmpty());
	}

	@Test
	void testIsFull() {
		//empty model
		assertFalse(cccModel.isFull());

		//partially filled model
		populateTestModel(false, cccModel);
		assertFalse(cccModel.isFull());

		//full model
		populateTestModel(true,otherModel);
		assertTrue(otherModel.isFull());

		//one rating removed model
		otherModel.removeRating(otherModel.getLastRecordRated());
		assertFalse(otherModel.isFull());

		//Rating removed out of order
		ChordChangeConsonanceRecord recordToRemoveOutOfOrder = 
				new ChordChangeConsonanceRecord(
						ChordSignature.MAJ_ADD11,
						ChordSignature.MAJOR,
						Interval.MAJOR3,
						null);

		ChordChangeConsonanceModel modelWithRecordRemovedOutOfOrder = 
				new ChordChangeConsonanceModel();

		populateTestModel(true,modelWithRecordRemovedOutOfOrder);

		modelWithRecordRemovedOutOfOrder.removeRating(recordToRemoveOutOfOrder);
		assertFalse(modelWithRecordRemovedOutOfOrder.isFull());
	}

	@Test
	void testEquals() {
		populateTestModel(false,cccModel);
		populateTestModel(false,otherModel);
		//test a half filled model, both same
		assertEquals(cccModel,otherModel);

		ChordChangeConsonanceRecord recordRemoved = 
				new ChordChangeConsonanceRecord(
						ChordSignature.firstSignature(), 
						ChordSignature.firstSignature().getNextChordSignature(),
						Interval.MAJOR3,
						null);
		
		//test two partially filled models,
		//not equal
		cccModel.removeRating(recordRemoved);
		assertNotEquals(cccModel, otherModel);

		//test two partially filled models after
		//removal of same record
		otherModel.removeRating(recordRemoved);
		assertEquals(cccModel,otherModel);

		//test two completely filled models.
		cccModel = new ChordChangeConsonanceModel();
		otherModel = new ChordChangeConsonanceModel();

		populateTestModel(true,cccModel);
		populateTestModel(true,otherModel);
		assertEquals(cccModel,otherModel);
	}
	
	@Test
	void testRequestMethod() {
		populateTestModel(true,cccModel);
		
		ChordChangeConsonanceRecordRequest request = 
				ChordChangeConsonanceRecordRequest.allPossibleRecords();
		
		//We are loading the records into another model to
		//make sure that all of the records have been retrieved.
		Set<ChordChangeConsonanceRecord> recordsRetrieved = cccModel.getRecords(request);
		
		for(ChordChangeConsonanceRecord record : recordsRetrieved) {
			otherModel.addRating(record);
		}
		
		//if everything was retrieved properly
		//the two models should be equal
		assertEquals(cccModel,otherModel);
	}
}
