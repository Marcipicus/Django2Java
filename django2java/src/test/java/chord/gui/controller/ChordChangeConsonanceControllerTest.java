package chord.gui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.ChordChangeConsonanceModel;
import chord.relations.record.ChordChangeConsonanceRecord;

/**
 * Test for the ChordChangeConsonanceController.
 * 
 * The ChordChangeConsonanceController exists so that we
 * separate the 
 * @author DAD
 *
 */
public class ChordChangeConsonanceControllerTest {


	ChordChangeConsonanceRecord currentRecordBeingRated;

	ChordChangeConsonanceModel cccModel;
	ChordChangeConsonanceController cccController;
	StateChangeListener<ChordChangeConsonanceRecord> cccListener;

	/**
	 * Create a new ChordChangeConsonanceController, ChordChangeConsonanceModel, and
	 * a listener to update the chord that is currently being rated.
	 */
	@BeforeEach
	void init() {
		cccModel = new ChordChangeConsonanceModel();
		cccListener = new StateChangeListener<ChordChangeConsonanceRecord>() {

			@Override
			public void stateChanged(ChordChangeConsonanceRecord newState) {
				currentRecordBeingRated = newState;
			}
		};
		cccController = new ChordChangeConsonanceController(cccModel, cccListener);
	}

	/**
	 * Make sure that the controller is initialized properly and the
	 * current record being rated is the first chord signature and 
	 * the interval is UNISON
	 */
	@Test
	void testInitialSetup() {
		assertEquals(ChordSignature.firstSignature(),currentRecordBeingRated.startChordSignature());
		assertEquals(ChordSignature.firstSignature(),currentRecordBeingRated.endChordSignature());
		//start and end chord are the same so we use MINOR2
		assertEquals(Interval.MINOR2,currentRecordBeingRated.intervalBetweenRoots());
	}

	/**
	 * Iterate through all of the possible combinations and make
	 * sure that the ratings are added in order.
	 * NOTE:THIS TEST TAKES A LONG TIME TO RUN...
	 * APPROXIMATELY 5 MINUTES OR SO. JUST LET IT RUN, IT WILL FINISH
	 */
	@Test
	void testSaveRating() {
		for(ChordSignature startChordBeingRated : ChordSignature.valuesAsList()) {
			for(ChordSignature endChordBeingRated : ChordSignature.valuesAsList()) {
				for(Interval intervalBeingRated : Interval.valuesInFirstOctave()){
					//skip any ratings that represent the same start and end chord
					//if the end and start chords are the same type, and the
					//interval between roots is unison then the two chords
					//represent the same chord.
					if(startChordBeingRated.equals(endChordBeingRated) && 
							intervalBeingRated.equals(Interval.UNISON)) {
						continue;
					}

					//make sure that the current record being rated is
					//the same as that desscribed by the nested loops
					assertEquals(startChordBeingRated,currentRecordBeingRated.startChordSignature());
					assertEquals(endChordBeingRated,currentRecordBeingRated.endChordSignature());
					assertEquals(intervalBeingRated,currentRecordBeingRated.intervalBetweenRoots());
					
					ConsonanceRating ratingAdded = ConsonanceRating.BAD;
					cccController.saveRating(ratingAdded);

					ChordChangeConsonanceRecord lastRecordAdded = cccModel.getLastRecordRated();
					
					assertEquals(startChordBeingRated,lastRecordAdded.startChordSignature());
					assertEquals(endChordBeingRated,lastRecordAdded.endChordSignature());
					assertEquals(intervalBeingRated,lastRecordAdded.intervalBetweenRoots());
					assertEquals(ratingAdded,lastRecordAdded.rating());
				}
			}
		}

		//make sure currentRecord is null to signify that
		//all ratings have been entered.
		assertNull(currentRecordBeingRated);
	}

	@Test
	void testPreviousRating() {
		ChordChangeConsonanceRecord previousRecord, previousRecordAfterSavingAndRemoving;
		
		//iterate through all possible chord combinations.
		for(ChordSignature startChordSig : ChordSignature.valuesAsList()) {
			for(ChordSignature endChordSig : ChordSignature.valuesAsList()) {
				for(Interval intervalBetweenRoots: Interval.valuesInFirstOctave()) {
					if(startChordSig.equals(endChordSig) && 
							intervalBetweenRoots.equals(Interval.UNISON)) {
						continue;
					}
					
					previousRecord = cccController.getCurrentRecord();
					
					cccController.saveRating(ConsonanceRating.VERY_GOOD);
					cccController.previousRating();
					
					//get the previous record after saving and removing
					//it should be the same record as the first call to getCurrentRecord()
					previousRecordAfterSavingAndRemoving = cccController.getCurrentRecord();
					

					assertEquals(previousRecord,previousRecordAfterSavingAndRemoving);
					
					
					//Advance the controller to the next record to be rated.
					cccController.saveRating(ConsonanceRating.VERY_GOOD);
				}
			}
			
		}
	}
}
