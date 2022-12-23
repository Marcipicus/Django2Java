package chord.relations.persist.file;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.ChordChangeConsonanceModel;
import chord.relations.NoteConsonanceModel;
import chord.relations.persist.PersistenceException;
import chord.relations.persist.file.ChordChangeConsonanceFilePersistStrategy;
import chord.relations.persist.file.FileStrategyConfig;
import chord.relations.persist.file.NoteConsonanceFilePersistStrategy;
import chord.relations.record.ChordChangeConsonanceRecord;
import chord.relations.request.ChordChangeConsonanceRecordRequest;
import chord.relations.request.NoteConsonanceRecordRequest;

public class ChordChangeConsonanceFilePersistStrategyTest {
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
	
	File testFile;

	ChordChangeConsonanceModel model,loadedModel;
	ChordChangeConsonanceFilePersistStrategy fileStrategy;

	ChordChangeConsonanceRecordRequest request;
	FileStrategyConfig config;
	
	@BeforeEach
	void init() {
		model = new ChordChangeConsonanceModel();
		
		populateTestModel(true, model);
		
		testFile = new File("testFile.tmp");
	}
	
	@AfterEach
	void cleanup() {
		testFile.delete();
	}
	
	@Test
	void testSaveAndLoad() throws PersistenceException {
		request = ChordChangeConsonanceRecordRequest.allPossibleRecords();
		config = new FileStrategyConfig(testFile);
		
		fileStrategy = new ChordChangeConsonanceFilePersistStrategy(config, request);
		
		fileStrategy.save(model);
		
		loadedModel = fileStrategy.load();
		
		assertEquals(model,loadedModel);
	}
}
