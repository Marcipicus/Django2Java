package chord.gui.components;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import chord.relations.record.ScaleConsonanceRecord;

public class ScaleConsonanceRecordPanel extends RecordPanel<ScaleConsonanceRecord> {

	private JLabel referenceChordLabel;
	private JLabel scaleLabel;
	
	public ScaleConsonanceRecordPanel() {
		super();
	}

	@Override
	protected void initializePanel() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		referenceChordLabel = new JLabel();
		add(referenceChordLabel);
		
		scaleLabel = new JLabel();
		add(scaleLabel);
		
		//padding labels
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
	}

	@Override
	public void updatePanel(ScaleConsonanceRecord record) {
		if(record == null) {
			referenceChordLabel.setText("All Ratings Complete");
			scaleLabel.setText("");
		}else {
			referenceChordLabel.setText("ChordSig:" + record.chordSignature().displayText());
			scaleLabel.setText("Scale:" + record.scaleSignature().displayText());
		}
	}

}
