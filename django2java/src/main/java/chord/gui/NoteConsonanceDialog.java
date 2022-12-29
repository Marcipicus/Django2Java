package chord.gui;

import javax.swing.JFrame;

import chord.gui.components.NoteConsonanceRecordPanel;
import chord.gui.controller.NoteConsonanceController;
import chord.relations.NoteConsonanceModel;
import chord.relations.record.NoteConsonanceRecord;
import chord.relations.request.NoteConsonanceRecordRequest;

public class NoteConsonanceDialog extends 
	RatingDialog<NoteConsonanceRecord,
	NoteConsonanceRecordRequest,
	NoteConsonanceModel,
	NoteConsonanceController,
	NoteConsonanceRecordPanel>{

	public NoteConsonanceDialog(JFrame parentFrame, NoteConsonanceController controller) {
		super(parentFrame, controller);
	}

	@Override
	protected String getDialogTitle() {
		return "Note Chord Consonance Rating";
	}

	@Override
	protected NoteConsonanceRecordPanel createRecordPanel() {				
		return new NoteConsonanceRecordPanel();
	}

}
