package chord.gui.components;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;

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
		//temporary holder for layout information
		//overwritten after each component is added
		GridBagConstraints gbc;
		
		//placeholders to simplify the layout of the
		//components of the panel
		//
		//there should be space for 5 vertical components
		//to match the RatingRadioPanel's Layout
		//within the RatingDialog superclass
		JLabel placeHolderLabel1,placeHolderLabel2,placeHolderLabel3;
		
		this.setLayout(new GridLayout());

		//we don't have to set the text of the label
		//since this is done by the updatePanel method
		referenceChordLabel = new JLabel();
		gbc = new CustomGridBagConstraints(
						0, 0, 
						2, 1, 
						GridBagConstraints.HORIZONTAL);
		add(referenceChordLabel,gbc);
		
		//we don't have to set the text of the label
		//since this is done by the updatePanel method
		noteIntervalLabel = new JLabel();
		gbc = new CustomGridBagConstraints(
						0, 0, 
						2, 1, 
						GridBagConstraints.HORIZONTAL);
		add(noteIntervalLabel,gbc);
		
		
		//Adding placeHolders so the layout lines up
		//with the RatingRadioPanel created in superclass
		placeHolderLabel1 = new JLabel();
		gbc = new CustomGridBagConstraints(
						0, 0, 
						2, 1, 
						GridBagConstraints.HORIZONTAL);
		add(placeHolderLabel1,gbc);
		
		placeHolderLabel2 = new JLabel();
		gbc = new CustomGridBagConstraints(
						0, 0, 
						2, 1, 
						GridBagConstraints.HORIZONTAL);
		add(placeHolderLabel2,gbc);
		
		placeHolderLabel3 = new JLabel();
		gbc = new CustomGridBagConstraints(
						0, 0, 
						2, 1, 
						GridBagConstraints.HORIZONTAL);
		add(placeHolderLabel3,gbc);
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
