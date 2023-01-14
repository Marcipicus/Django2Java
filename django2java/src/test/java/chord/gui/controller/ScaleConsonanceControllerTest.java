package chord.gui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;
import chord.relations.ScaleConsonanceModel;
import chord.relations.record.ScaleConsonanceRecord;

public class ScaleConsonanceControllerTest {
	ScaleConsonanceRecord currentRecordBeingRated;
	
	ScaleConsonanceModel scModel;
	ScaleConsonanceController scController;
	StateChangeListener<ScaleConsonanceRecord> scListener;
	
	/**
	 * Create a new ScaleConsonanceController, ScaleConsonanceModel, and
	 * a listener to update the chord that is currently being rated.
	 */
	@BeforeEach
	void init() {
		scModel = new ScaleConsonanceModel();
		scListener = new StateChangeListener<ScaleConsonanceRecord>() {
			@Override
			public void stateChanged(ScaleConsonanceRecord newState) {
				currentRecordBeingRated = newState;
			}
		};
		
		scController = new ScaleConsonanceController(scModel, scListener);
	}
	
	/**
	 * Make sure that the controller is initialized properly and the
	 * current record being rated is the first chord signature and 
	 * the interval is UNISON
	 */
	@Test
	void testInitialSetup() {
		assertEquals(ChordSignature.firstSignature(),currentRecordBeingRated.chordSignature());
		assertEquals(ScaleSignature.firstSignature(),currentRecordBeingRated.scaleSignature());
	}
	
	/**
	 * Iterate through all of the possible combinations and make
	 * sure that the ratings are added in order.
	 */
	@Test
	void testSaveRating() {
		for(ChordSignature chordSigBeingRated : ChordSignature.valuesAsList()) {
			for(ScaleSignature scaleSigBeingRated : ScaleSignature.valuesAsList()) {
				assertEquals(chordSigBeingRated,currentRecordBeingRated.chordSignature());
				assertEquals(scaleSigBeingRated,currentRecordBeingRated.scaleSignature());
				
				
				ConsonanceRating ratingAdded = ConsonanceRating.BAD;
				scController.saveRating(ratingAdded);
				

				ScaleConsonanceRecord lastRecordAdded = scModel.getLastRecordRated();
				
				assertEquals(chordSigBeingRated,lastRecordAdded.chordSignature());
				assertEquals(scaleSigBeingRated,lastRecordAdded.scaleSignature());
				assertEquals(ratingAdded,lastRecordAdded.rating());
			}
		}
		
		//make sure currentRecord is null to signify that
		//all ratings have been entered.
		assertNull(currentRecordBeingRated);
	}
	
	@Test
	void testPreviousRating() {
		ScaleConsonanceRecord previousRecord, previousRecordAfterSavingAndRemoving;
		
		//Iterate through all ChordSignature and ScaleSignature
		//combinations
		for(ChordSignature referenceChordSid : ChordSignature.valuesAsList()) {
			for(ScaleSignature scaleSig : ScaleSignature.valuesAsList()) {
				previousRecord = scController.getCurrentRecord();
				
				//save the rating then turn the controller back one
				//record by calling(pressing) previous Rating.
				scController.saveRating(ConsonanceRating.VERY_GOOD);
				scController.previousRating();
				
				//get the previous record after saving and removing
				//it should be the same record as the first call to getCurrentRecord()
				previousRecordAfterSavingAndRemoving = scController.getCurrentRecord();
				
				assertEquals(previousRecord,previousRecordAfterSavingAndRemoving);
				
				//Advance the controller to the next record to be rated.
				scController.saveRating(ConsonanceRating.VERY_GOOD);
			}
		}
	}

}
