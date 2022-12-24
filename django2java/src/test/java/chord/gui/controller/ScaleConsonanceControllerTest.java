package chord.gui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
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
		for(ChordSignature chordSigBeingRated : ChordSignature.values()) {
			for(ScaleSignature scaleSigBeingRated : ScaleSignature.values()) {
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
		//attempt to go back to the previous rating when
		//no ratings exist, make sure that nothing changes
		scController.previousRating();
		
		assertEquals(ChordSignature.firstSignature(),currentRecordBeingRated.chordSignature());
		assertEquals(ScaleSignature.firstSignature(),currentRecordBeingRated.scaleSignature());
		
		scController.saveRating(ConsonanceRating.GOOD);
	}

}
