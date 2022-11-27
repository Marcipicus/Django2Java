package chord.gui;

import javax.swing.JFrame;

import chord.gui.components.ChordChangeConsonanceRecordPanel;
import chord.gui.controller.ChordChangeConsonanceController;
import chord.relations.ChordChangeConsonanceModel;
import chord.relations.ChordChangeConsonanceRecord;

public class ChordChangeConsonanceDialog extends 
	RatingDialog<ChordChangeConsonanceRecord,
	ChordChangeConsonanceModel,
	ChordChangeConsonanceController,
	ChordChangeConsonanceRecordPanel> {

	public ChordChangeConsonanceDialog(JFrame parentFrame, ChordChangeConsonanceController controller) {
		super(parentFrame, controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getDialogTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ChordChangeConsonanceRecordPanel createRecordPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
