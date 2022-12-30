package chord.gui;

import javax.swing.JFrame;

import chord.gui.components.ScaleConsonanceRecordPanel;
import chord.gui.controller.ScaleConsonanceController;
import chord.relations.ScaleConsonanceModel;
import chord.relations.record.ScaleConsonanceRecord;
import chord.relations.request.ScaleConsonanceRecordRequest;

public class ScaleConsonanceDialog extends 
	RatingDialog<ScaleConsonanceRecord,
	ScaleConsonanceRecordRequest,
	ScaleConsonanceModel,
	ScaleConsonanceController,
	ScaleConsonanceRecordPanel> {

	public ScaleConsonanceDialog(JFrame parentFrame, ScaleConsonanceController controller) {
		super(parentFrame, controller);
	}

	@Override
	protected String getDialogTitle() {
		return "Scale Rating Dialog";
	}

	@Override
	protected ScaleConsonanceRecordPanel createRecordPanel() {
		return new ScaleConsonanceRecordPanel();
	}

}
