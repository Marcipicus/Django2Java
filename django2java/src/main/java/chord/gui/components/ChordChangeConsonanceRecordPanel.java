package chord.gui.components;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import chord.relations.record.ChordChangeConsonanceRecord;

public class ChordChangeConsonanceRecordPanel extends RecordPanel<ChordChangeConsonanceRecord> {

	private JLabel referenceChordLabel;
	private JLabel targetChordLabel;
	private JLabel intervalBetweenRootsLabel;
	
	public ChordChangeConsonanceRecordPanel() {
		super();
	}

	@Override
	protected void initializePanel() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		referenceChordLabel = new JLabel();
		add(referenceChordLabel);
		
		targetChordLabel = new JLabel();
		add(targetChordLabel);
		
		intervalBetweenRootsLabel = new JLabel();
		add(intervalBetweenRootsLabel);
		
		//padding labels
		add(new JLabel());
		add(new JLabel());
	}

	@Override
	public void updatePanel(ChordChangeConsonanceRecord record) {
		if(record == null) {
			referenceChordLabel.setText("All Ratings Complete");
			targetChordLabel.setText("");
			intervalBetweenRootsLabel.setText("");
		}else {
			referenceChordLabel.setText("StartChord:" + record.startChordSignature().displayText());
			targetChordLabel.setText("EndChord:" + record.endChordSignature().displayText());
			intervalBetweenRootsLabel.setText(  "Interval:" + record.intervalBetweenRoots().displayText());
		}
	}

}
