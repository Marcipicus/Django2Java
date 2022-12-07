package chord.gui.controller;

import java.io.File;
import java.io.FileNotFoundException;

import chord.ConsonanceRating;
import chord.relations.NoteConsonanceModel;
import chord.relations.NoteConsonanceRecord;
import chord.relations.NoteConsonanceRecordRequest;

/**
 * Concrete implementation of the RatngModelController to
 * mediate between the NoteConsonanceModel data structure
 * and the gui.
 * 
 * The controller class was created so that the event model
 * of the gui could be tested in isolation and programatically.
 * @author DAD
 *
 */
public class NoteConsonanceController extends 
RatingModelController<
NoteConsonanceRecord,
NoteConsonanceRecordRequest, 
NoteConsonanceModel> {

	@SafeVarargs
	public NoteConsonanceController(NoteConsonanceModel model, StateChangeListener<NoteConsonanceRecord>... listeners) {
		super(model, listeners);
	}

	@Override
	protected NoteConsonanceRecord createRecordToSave(ConsonanceRating rating) {
		if(currentRecord == null) {
			throw new IllegalStateException(
					"This should not happen. currentRecord is checked by RatingModelController");
		}
		NoteConsonanceRecord recordToSave = 
				new NoteConsonanceRecord(
						currentRecord.chordSignature(),
						currentRecord.interval(),
						rating);
		
		return recordToSave;
	}

	@Override
	public void saveFile(File destinationFile) throws FileNotFoundException {
		NoteConsonanceModel.saveToFile(model, destinationFile);
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		
	}
}
