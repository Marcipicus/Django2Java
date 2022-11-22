package chord.gui.controller;

import chord.ConsonanceRating;
import chord.relations.NoteConsonanceModel;
import chord.relations.NoteConsonanceRecord;

public class NoteConsonanceController extends RatingModelController<NoteConsonanceRecord, NoteConsonanceModel> {

	public NoteConsonanceController(NoteConsonanceModel model, StateChangeListener<NoteConsonanceRecord>[] listeners) {
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
		// TODO Auto-generated method stub
		return recordToSave;
	}
}
