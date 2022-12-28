package chord.gui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.NoteConsonanceModel;
import chord.relations.record.NoteConsonanceRecord;

/**
 * Test class used to automate the testing of the
 * NoteConsonance rating gui's event model programmatically.
 * @author DAD
 *
 */
public class NoteConsonanceControllerTest {
	
	NoteConsonanceRecord currentRecordBeingRated;
	
	NoteConsonanceModel ncModel;
	NoteConsonanceController ncController;
	StateChangeListener<NoteConsonanceRecord> ncListener;
	
	/**
	 * Create a new NoteConsonanceController, NoteConsonanceModel, and
	 * a listener to update the chord that is currently being rated.
	 */
	@BeforeEach
	void init() {
		ncModel = new NoteConsonanceModel();
		ncListener = new StateChangeListener<NoteConsonanceRecord>() {
			
			@Override
			public void stateChanged(NoteConsonanceRecord newState) {
				currentRecordBeingRated = newState;
			}
		};
		ncController = new NoteConsonanceController(ncModel, ncListener);
	}
	
	/**
	 * Make sure that the controller is initialized properly and the
	 * current record being rated is the first chord signature and 
	 * the interval is UNISON
	 */
	@Test
	void testInitialSetup() {
		assertEquals(ChordSignature.firstSignature(),currentRecordBeingRated.chordSignature());
		assertEquals(Interval.UNISON,currentRecordBeingRated.interval());
	}
	
	/**
	 * Iterate through all of the possible combinations and make
	 * sure that the ratings are added in order.
	 */
	@Test
	void testSaveRating() {
		for(ChordSignature chordSigBeingRated : ChordSignature.values()) {
			for(Interval intervalBeingRated : Interval.valuesInFirstOctave()){
				
				assertEquals(chordSigBeingRated,currentRecordBeingRated.chordSignature());
				assertEquals(intervalBeingRated,currentRecordBeingRated.interval());
				
				ConsonanceRating ratingAdded = ConsonanceRating.BAD;
				ncController.saveRating(ratingAdded);
				
				NoteConsonanceRecord lastRecordAdded = ncModel.getLastRecordRated();
				
				assertEquals(chordSigBeingRated,lastRecordAdded.chordSignature());
				assertEquals(intervalBeingRated,lastRecordAdded.interval());
				assertEquals(ratingAdded,lastRecordAdded.rating());
				
			}
		}
		
		//make sure currentRecord is null to signify that
		//all ratings have been entered.
		assertNull(currentRecordBeingRated);
	}
	
	/**
	 * Iterate through all possible records and ensure that the
	 * previousRating method returns the previous rating
	 */
	@Test
	void testPreviousRating() {
		NoteConsonanceRecord previousRecord, previousRecordAfterSavingAndRemoving;
		
		for(ChordSignature referenceChordSid : ChordSignature.values()) {
			for(Interval intervalBeingRated : Interval.valuesInFirstOctave()) {
				previousRecord = ncController.getCurrentRecord();
				
				//save the rating then turn the controller back one
				//record by calling(pressing) previous Rating.
				ncController.saveRating(ConsonanceRating.VERY_GOOD);
				ncController.previousRating();
				
				//get the previous record after saving and removing
				//it should be the same record as the first call to getCurrentRecord()
				previousRecordAfterSavingAndRemoving = ncController.getCurrentRecord();
				
				assertEquals(previousRecord,previousRecordAfterSavingAndRemoving);
				
				//Advance the controller to the next record to be rated.
				ncController.saveRating(ConsonanceRating.VERY_GOOD);
			}
		}
	}

}
