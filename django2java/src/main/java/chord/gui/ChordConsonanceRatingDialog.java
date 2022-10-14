package chord.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import chord.ConsonanceRating;
import chord.Interval;
import chord.gui.components.ChordLabel;
import chord.gui.components.CustomGridBagConstraints;
import chord.gui.components.IntervalLabel;
import chord.gui.components.RatingRadioPanel;
import chord.ident.ChordSignature;
import chord.relations.persist.P_EndChordRatingList;
import chord.relations.persist.P_IntervalRating;

/**
 * This dialog is used to rate chord changes between a start chord and
 * a target chord at all intervals between the roots of each chord 
 * in the range between UNISON and MAJOR7
 * @author DAD
 *
 */
public class ChordConsonanceRatingDialog extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<P_IntervalRating> intervalRatings;
	private boolean lastChordChangeHasBeenRated = false;
	
	private ChordLabel startChordLabel, endChordLabel;
	private IntervalLabel intervalLabel;
	private RatingRadioPanel ratingPanel;

	private JButton playButton;

	private JButton saveRatingButton;

	public ChordConsonanceRatingDialog(JDialog  parentDialog, ChordSignature startChordSig, ChordSignature endChordSig) {
		super(parentDialog,"Chord Consonance Rating");
		setLayout(new GridBagLayout());
		setSize(300,250);
		//setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		JLabel emptyLabel1,emptyLabel2;

		GridBagConstraints gbc; // temporary holder for GridBagConstraints..reused

		intervalRatings = new LinkedList<P_IntervalRating>();
		
		startChordLabel = new ChordLabel("Start Chord",startChordSig);
		gbc = new CustomGridBagConstraints(
				0,0,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(startChordLabel,gbc);
		
		endChordLabel = new ChordLabel("End Chord",endChordSig);
		gbc = new CustomGridBagConstraints(
				0,1,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(endChordLabel,gbc);
		
		//MAKE SURE THAT WE DON'T RATE CHANGES THAT START
		//AND ON THE SAME CHORD WITH THE SAME ROOT NOTE
		Interval initialIntervalValue;
		if(startChordSig.equals(endChordSig)) {
			initialIntervalValue = Interval.MINOR2;
		}else {
			initialIntervalValue = Interval.UNISON;
		}

		intervalLabel = new IntervalLabel(initialIntervalValue);
		gbc = new CustomGridBagConstraints(
				0,2,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(intervalLabel,gbc);

		//adding empty labels for gridbag layout alignment
		emptyLabel1 = new JLabel(" ");
		gbc = new CustomGridBagConstraints(
				0,3,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(emptyLabel1,gbc);

		emptyLabel2 = new JLabel(" ");
		gbc = new CustomGridBagConstraints(
				0,4,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(emptyLabel2,gbc);

		ratingPanel = new RatingRadioPanel();
		gbc = new CustomGridBagConstraints(
				1,0,
				1,4,
				GridBagConstraints.HORIZONTAL);
		add(ratingPanel,gbc);

		playButton = new JButton("Play");
		playButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				0,5,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(playButton,gbc);

		saveRatingButton = new JButton("Save Rating");
		saveRatingButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				1,5,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(saveRatingButton,gbc);
	}
	
	/**
	 * Make the dialog visible and return all ratings to the caller
	 * @return
	 */
	public P_EndChordRatingList showDialog() {
		setVisible(true);
		
		P_EndChordRatingList endChordList = new P_EndChordRatingList();
		endChordList.setEndsignature(endChordLabel.getChordSignature());
		endChordList.setIntervalRatings(intervalRatings);

		return endChordList;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == playButton) {
			play();
		}else if(e.getSource() == saveRatingButton) {
			saveRating();
		}else {
			throw new IllegalArgumentException("Unhandled event source.");
		}
	}
	
	private void play() {
		//TODO: SET UP THE PLAY BUTTON
	}
	
	private void saveRating() {
		
		Interval interval = intervalLabel.getInterval();
		ConsonanceRating rating = ratingPanel.selectedRating();

		P_IntervalRating intervalRating = new P_IntervalRating();
		intervalRating.setIntervalBetweenRoots(interval);
		intervalRating.setRating(rating);
		
		intervalRatings.add(intervalRating);

		if(interval.equals(Interval.MAJOR7)) {
			lastChordChangeHasBeenRated = true;
			//Close the dialog
			ChordConsonanceRatingDialog.this.setVisible(false);
            ChordConsonanceRatingDialog.this.dispatchEvent(new WindowEvent(
                ChordConsonanceRatingDialog.this, WindowEvent.WINDOW_CLOSING));
			return;
		}

		intervalLabel.setInterval(interval.getNextInterval());
	}
}
