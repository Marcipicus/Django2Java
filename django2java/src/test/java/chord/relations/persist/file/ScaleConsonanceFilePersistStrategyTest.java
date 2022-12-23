package chord.relations.persist.file;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;
import chord.relations.ScaleConsonanceModel;
import chord.relations.persist.PersistenceException;
import chord.relations.persist.file.FileStrategyConfig;
import chord.relations.persist.file.ScaleConsonanceFilePersistStrategy;
import chord.relations.record.ScaleConsonanceRecord;
import chord.relations.request.ScaleConsonanceRecordRequest;

public class ScaleConsonanceFilePersistStrategyTest {

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
	
	File testFile;

	ScaleConsonanceModel model,loadedModel;
	ScaleConsonanceFilePersistStrategy fileStrategy;

	ScaleConsonanceRecordRequest request;
	FileStrategyConfig config;
	
	@BeforeEach
	void init() {
		model = new ScaleConsonanceModel();
		
		populateTestModel(true, model);
		
		testFile = new File("testFile.tmp");
	}
	
	@AfterEach
	void cleanup() {
		testFile.delete();
	}
	
	@Test
	void testSaveAndLoad() throws PersistenceException {
		request = ScaleConsonanceRecordRequest.allPossibleRecords();
		config = new FileStrategyConfig(testFile);
		
		fileStrategy = new ScaleConsonanceFilePersistStrategy(config, request);
		
		fileStrategy.save(model);
		
		loadedModel = fileStrategy.load();
		
		assertEquals(model,loadedModel);
	}
	
}
