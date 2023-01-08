package chord.relations.persist.file;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.NoteConsonanceModel;
import chord.relations.persist.PersistenceException;
import chord.relations.persist.file.FileStrategyConfig;
import chord.relations.persist.file.NoteConsonanceFilePersister;
import chord.relations.record.NoteConsonanceRecord;
import chord.relations.request.NoteConsonanceRecordRequest;

public class NoteConsonanceFilePersistStrategyTest {
	
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
	
	File testFile;

	NoteConsonanceModel model,loadedModel;
	NoteConsonanceFilePersister fileStrategy;

	NoteConsonanceRecordRequest request;
	FileStrategyConfig config;
	
	@BeforeEach
	void init() {
		model = new NoteConsonanceModel();
		
		populateTestModel(true, model);
		
		testFile = new File("testFile.tmp");
	}
	
	@AfterEach
	void cleanup() {
		testFile.delete();
	}
	
	@Test
	void testSaveAndLoad() throws PersistenceException {
		request = NoteConsonanceRecordRequest.allPossibleRecords();
		config = new FileStrategyConfig(testFile);
		
		fileStrategy = new NoteConsonanceFilePersister(config, request);
		
		fileStrategy.save(model);
		
		loadedModel = fileStrategy.load();
		
		assertEquals(model,loadedModel);
	}
}
