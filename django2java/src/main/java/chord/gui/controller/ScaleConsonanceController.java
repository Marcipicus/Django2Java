package chord.gui.controller;

import java.io.File;

import chord.ConsonanceRating;
import chord.MIDIPlayer;
import chord.exceptions.GenericMIDIException;
import chord.relations.ScaleConsonanceModel;
import chord.relations.persist.PersistenceException;
import chord.relations.persist.file.FileStrategyConfig;
import chord.relations.persist.file.ScaleConsonanceFilePersister;
import chord.relations.record.ScaleConsonanceRecord;
import chord.relations.request.ScaleConsonanceRecordRequest;

public class ScaleConsonanceController extends 
RatingModelController<
ScaleConsonanceRecord, 
ScaleConsonanceRecordRequest,
ScaleConsonanceModel>{

	public ScaleConsonanceController(ScaleConsonanceModel model,
			StateChangeListener<ScaleConsonanceRecord>... listeners) {
		super(model, listeners);
	}

	@Override
	public void play() throws GenericMIDIException {
		MIDIPlayer midiPlayer = MIDIPlayer.getInstance();
		
		midiPlayer.playScaleConsonanceRecord(currentRecord);
	}

	@Override
	public void saveFile(File destinationFile) throws PersistenceException {
		ScaleConsonanceRecordRequest request = 
				ScaleConsonanceRecordRequest.allPossibleRecords();
		FileStrategyConfig fileConfig = new FileStrategyConfig(destinationFile);
		
		ScaleConsonanceFilePersister scaleConsonancePersister = 
				new ScaleConsonanceFilePersister(
						fileConfig, 
						request);

		scaleConsonancePersister.save(model);
	}

	@Override
	protected ScaleConsonanceRecord createRecordToSave(ConsonanceRating rating) {
		if(currentRecord == null) {
			throw new IllegalStateException(
					"This should not happen. currentRecord is checked by RatingModelController");
		}
		ScaleConsonanceRecord recordToSave = 
				new ScaleConsonanceRecord(
						currentRecord.chordSignature(),
						currentRecord.scaleSignature(),
						rating);
		
		return recordToSave;
	}

}
