package chord.gui.controller;

import java.io.File;

import chord.ConsonanceRating;
import chord.relations.ScaleConsonanceModel;
import chord.relations.persist.PersistenceException;
import chord.relations.record.ScaleConsonanceRecord;
import chord.relations.request.ScaleConsonanceRecordRequest;

public class ScaleConsonanceController extends 
RatingModelController<
ScaleConsonanceRecord, 
ScaleConsonanceRecordRequest,
ScaleConsonanceModel>{

	public ScaleConsonanceController(ScaleConsonanceModel model,
			StateChangeListener<ScaleConsonanceRecord>[] listeners) {
		super(model, listeners);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveFile(File destinationFile) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ScaleConsonanceRecord createRecordToSave(ConsonanceRating rating) {
		// TODO Auto-generated method stub
		return null;
	}

}
