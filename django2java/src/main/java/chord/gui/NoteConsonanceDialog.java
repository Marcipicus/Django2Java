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
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getDialogTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected NoteConsonanceRecordPanel createRecordPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
