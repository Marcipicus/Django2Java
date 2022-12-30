package chord.gui;

import javax.swing.JFrame;

import chord.gui.components.ChordChangeConsonanceRecordPanel;
import chord.gui.controller.ChordChangeConsonanceController;
import chord.relations.ChordChangeConsonanceModel;
import chord.relations.record.ChordChangeConsonanceRecord;
import chord.relations.request.ChordChangeConsonanceRecordRequest;

public class ChordChangeConsonanceDialog extends 
	RatingDialog<ChordChangeConsonanceRecord,
	ChordChangeConsonanceRecordRequest,
	ChordChangeConsonanceModel,
	ChordChangeConsonanceController,
	ChordChangeConsonanceRecordPanel> {

	public ChordChangeConsonanceDialog(JFrame parentFrame, ChordChangeConsonanceController controller) {
		super(parentFrame, controller);
	}

	@Override
	protected String getDialogTitle() {
		return "Chord Change Rating Dialog";
	}

	@Override
	protected ChordChangeConsonanceRecordPanel createRecordPanel() {
		return new ChordChangeConsonanceRecordPanel();
	}

}
