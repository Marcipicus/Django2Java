package chord.gui.controller;

import java.io.File;
import java.io.FileNotFoundException;

import chord.ConsonanceRating;
import chord.relations.ChordChangeConsonanceModel;
import chord.relations.ChordChangeConsonanceRecord;
import chord.relations.ChordChangeConsonanceRecordRequest;

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
	public void saveFile(File destinationFile) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ChordChangeConsonanceRecord createRecordToSave(ConsonanceRating rating) {
		// TODO Auto-generated method stub
		return null;
	}

}
