package chord.gui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.NoteConsonanceModel;
import chord.relations.NoteConsonanceRecord;

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
			for(Interval intervalBeingRated : Interval.values()){
				if(!intervalBeingRated.inFirstOctave()) {
					break;
				}
				
				
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
	
	@Test
	void testPreviousRating() {
		//attempt to go back to the previous rating when
		//no ratings exist, make sure that nothing changes
		ncController.previousRating();
		
		assertEquals(ChordSignature.firstSignature(),currentRecordBeingRated.chordSignature());
		assertEquals(Interval.UNISON,currentRecordBeingRated.interval());
		
		ncController.saveRating(ConsonanceRating.GOOD);
	}

}
