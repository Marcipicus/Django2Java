package chord.gui.components;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import chord.relations.record.NoteConsonanceRecord;

public class NoteConsonanceRecordPanel extends RecordPanel<NoteConsonanceRecord> {
	
	private JLabel referenceChordLabel;
	private JLabel noteIntervalLabel;
	
	public NoteConsonanceRecordPanel() {
		super();
	}

	@Override
	protected void initializePanel() {		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		referenceChordLabel = new JLabel();
		add(referenceChordLabel);
		
		noteIntervalLabel = new JLabel();
		add(noteIntervalLabel);
		
		//padding labels
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
	}

	@Override
	public void updatePanel(NoteConsonanceRecord record) {
		if(record == null) {
			referenceChordLabel.setText("All Ratings Complete");
			noteIntervalLabel.setText("");
		}else {
			referenceChordLabel.setText("ChordSig:" + record.chordSignature().displayText());
			noteIntervalLabel.setText(  "Interval:" + record.interval().displayText());
		}
	}

}
