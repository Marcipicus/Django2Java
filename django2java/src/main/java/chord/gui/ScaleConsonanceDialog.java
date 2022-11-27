package chord.gui;

import javax.swing.JFrame;

import chord.gui.components.ScaleConsonanceRecordPanel;
import chord.gui.controller.ScaleConsonanceController;
import chord.relations.ScaleConsonanceModel;
import chord.relations.ScaleConsonanceRecord;

public class ScaleConsonanceDialog extends 
	RatingDialog<ScaleConsonanceRecord,
	ScaleConsonanceModel,
	ScaleConsonanceController,
	ScaleConsonanceRecordPanel> {

	public ScaleConsonanceDialog(JFrame parentFrame, ScaleConsonanceController controller) {
		super(parentFrame, controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getDialogTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ScaleConsonanceRecordPanel createRecordPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
