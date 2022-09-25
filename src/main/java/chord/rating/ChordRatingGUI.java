package chord.rating;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;

public class ChordRatingGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ChordSignature startSig, endSig;
	private Interval currentIntervalBetweenRoots;
	
	private JLabel startChordLabel, endChordLabel, intervalBetweenRootsLabel;
	
	private JButton playChordChangeButton, acceptChordChangeRatingButton;
	
	private JRadioButton changeRatingRadioButton;
	
	public ChordRatingGUI(ChordSignature startSig) {
		super("Chord Changes");
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300,300);
		
		add(createChordChangeStatePanel(),BorderLayout.WEST);
		
		add(createRatingPanel(),BorderLayout.EAST);

		updateStartChord(startSig);
		updateEndChord(endSig);
		
		if(startSig.equals(endSig)) {
			updateIntervalBetweenRoots(Interval.MINOR2);
		}else {
			updateIntervalBetweenRoots(Interval.UNISON);
		}
		
		
		setVisible(true);
	}

	private void updateStartChord(ChordSignature newStartSig){
		if(newStartSig == null) {
			throw new IllegalArgumentException("Start Signature may not be null.");
		}
		this.startSig = newStartSig;
		startChordLabel.setText("Start:       " + startSig.displayText());
	}
	private void updateEndChord(ChordSignature newEndSig){
		if(newEndSig == null) {
			throw new IllegalArgumentException("End signature may not be null.");
		}
		this.endSig = newEndSig;
		endChordLabel.setText(  "End:         "+ endSig.displayText());
	}
	private void updateIntervalBetweenRoots(Interval newInterval){
		if(newInterval == null) {
			throw new IllegalArgumentException("Interval between roots may not be null.");
		}
		this.currentIntervalBetweenRoots = newInterval;
		intervalBetweenRootsLabel.setText("Interval:  "+currentIntervalBetweenRoots.displayText());
	}

	
	private JPanel createChordChangeStatePanel() {
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BoxLayout(westPanel,BoxLayout.Y_AXIS));
		startChordLabel = new JLabel();
		endChordLabel = new JLabel();
		intervalBetweenRootsLabel = new JLabel();
		
		playChordChangeButton = new JButton();
		playChordChangeButton.setText("Play Chord Change");
		//TODO:Add action to the play button
		playChordChangeButton.setAction(null);
		
		westPanel.add(startChordLabel);
		westPanel.add(endChordLabel);
		westPanel.add(intervalBetweenRootsLabel);
		westPanel.add(playChordChangeButton);
		return westPanel;
	}
	
	
	private Component createRatingPanel() {
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel,BoxLayout.Y_AXIS));
		
		acceptChordChangeRatingButton = new JButton();
		acceptChordChangeRatingButton.setText("Accept Rating");
		//TODO:add action to the acceptButton
		acceptChordChangeRatingButton.setAction(null);
		
		eastPanel.add(createRatingRadioPanel());
		eastPanel.add(acceptChordChangeRatingButton);
		return eastPanel;
	}
	
	private JPanel createRatingRadioPanel(){
		JRadioButton veryGood = new JRadioButton(ConsonanceRating.VERY_GOOD.toString());
		JRadioButton good = new JRadioButton(ConsonanceRating.GOOD.toString());
		JRadioButton mediocre = new JRadioButton(ConsonanceRating.MEDIOCRE.toString());
		JRadioButton bad = new JRadioButton(ConsonanceRating.BAD.toString());
		JRadioButton veryBad = new JRadioButton(ConsonanceRating.VERY_BAD.toString());
		
		veryGood.setSelected(true);
		
		ButtonGroup ratingButtonGroup = new ButtonGroup();
		ratingButtonGroup.add(veryGood);
		ratingButtonGroup.add(good);
		ratingButtonGroup.add(mediocre);
		ratingButtonGroup.add(bad);
		ratingButtonGroup.add(veryBad);
		
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new BoxLayout(radioPanel,BoxLayout.Y_AXIS));
		
		radioPanel.add(veryGood);
		radioPanel.add(good);
		radioPanel.add(mediocre);
		radioPanel.add(bad);
		radioPanel.add(veryBad);
		
		return radioPanel;
	}
}
