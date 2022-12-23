package chord.gui.controller;

import java.io.File;

import chord.ConsonanceRating;
import chord.relations.ChordChangeConsonanceModel;
import chord.relations.persist.PersistenceException;
import chord.relations.record.ChordChangeConsonanceRecord;
import chord.relations.request.ChordChangeConsonanceRecordRequest;

public class ChordChangeConsonanceController extends 
RatingModelController<
ChordChangeConsonanceRecord, 
ChordChangeConsonanceRecordRequest , 
ChordChangeConsonanceModel>{

	public ChordChangeConsonanceController(ChordChangeConsonanceModel model,
			StateChangeListener<ChordChangeConsonanceRecord>[] listeners) {
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
	protected ChordChangeConsonanceRecord createRecordToSave(ConsonanceRating rating) {
		// TODO Auto-generated method stub
		return null;
	}

}
