package chord.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import chord.ConsonanceRating;
import chord.gui.components.CustomGridBagConstraints;
import chord.gui.components.RatingRadioPanel;
import chord.gui.components.RecordPanel;
import chord.gui.controller.RatingModelController;
import chord.gui.controller.StateChangeListener;
import chord.relations.RatingModel;
import chord.relations.persist.PersistenceException;

/**
 * Abstract class used to rate "musical combinations".
 * 
 * Sub classes only need to implement the getDialogTitle() and
 * createRecordPanel() methods.
 * @author DAD
 *
 * @param <RECORD> record type that represents the musical combination
 * being rated
 * @param <MODEL> data model to store ratings represented by RECORD
 * @param <CONTROLLER> controller that mediates requests from the gui
 * to the data model.
 * @param <RECORDPANEL> panel to display the current record being rated.
 */
public abstract class RatingDialog<
	RECORD,
	REQUEST,
	MODEL extends RatingModel<RECORD,REQUEST>,
	CONTROLLER extends RatingModelController<RECORD,REQUEST,MODEL>,
	RECORDPANEL extends RecordPanel<RECORD>> extends JDialog 
	implements ActionListener,KeyListener,StateChangeListener<RECORD>{
	
	
	//button hotkeys
	private static final char PREVIOUS_RATING_CHAR = 's';
	private static final char PLAY_RECORD_CHAR = 'd';
	private static final char SAVE_RATING_CHAR = 'f';
	
	//rating hotkeys
	private static final char VERY_BAD_RATING_CHAR = '1';
	private static final char BAD_RATING_CHAR = '2';
	private static final char MEDIOCRE_RATING_CHAR = '3';
	private static final char GOOD_RATING_CHAR = '4';
	private static final char VERY_GOOD_RATING_CHAR = '5';
	
	/**
	 * Controller that mediates requests from the gui to
	 * the underlying data model.
	 */
	private CONTROLLER controller;
	
	/**
	 * Panel containing the "musical combination" being rated
	 * which is represented by the record.
	 */
	private RECORDPANEL recordPanel;
	
	/**
	 * Radio panel containing the rating for the
	 * "musical combination" represented by the record.
	 */
	private RatingRadioPanel ratingPanel;
	
	/**
	 * Button used to play the musical combination\being rated.
	 */
	private JButton playButton;
	
	/**
	 * Button used to erase previous rating and
	 * prepare the dialog to re-rate the record.
	 */
	private JButton previousRatingButton;
	
	/**
	 * Button used to save rating to data model.
	 */
	private JButton saveRatingButton;
	
	/**
	 * Button used to save ratings to file.
	 */
	private JButton saveToFileButton;

	
	public RatingDialog(JFrame parentFrame, CONTROLLER controller) {
		super(parentFrame);
		if(controller == null) {
			throw new NullPointerException("Controller may not be null");
		}
		this.controller = controller;
		setTitle(getDialogTitle());
		initializeDialog();
		
		//Required for key events to work
		setFocusable(true);
		addKeyListener(this);
		
		//the following two lines udate the RecordPanel
		//so we don't have to fill it in explicitly
		//when the panels are made
		controller.addStateChangeListener(this);
		stateChanged(controller.getCurrentRecord());
		
		setVisible(true);
	}
	
	/**
	 * Get the desired title of the dialog.
	 * 
	 * Subclasses should override this method to return
	 * the desired title for their dialog.
	 * @return Desired title for the dialog.
	 */
	protected abstract String getDialogTitle();
	
	/**
	 * Create all of the widgets for the dialog and
	 * set their layout.
	 */
	private void initializeDialog() {
		setLayout(new GridBagLayout());
		setSize(300,250);
		//setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);

		GridBagConstraints gbc; // temporary holder for GridBagConstraints..reused

		recordPanel = createRecordPanel();
		gbc = new CustomGridBagConstraints(
				0,0,
				2,5,
				GridBagConstraints.HORIZONTAL);
		add(recordPanel,gbc);
		
		ratingPanel = new RatingRadioPanel();
		gbc = new CustomGridBagConstraints(
				3,0,
				2,5,
				GridBagConstraints.HORIZONTAL);
		add(ratingPanel,gbc);
		ratingPanel.setToolTipText("Hotkey: number keys 1-5, 1 lowest 5, highest");

		playButton = new JButton("Play");
		playButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				0,6,
				2,1,
				GridBagConstraints.HORIZONTAL);
		add(playButton,gbc);
		playButton.setToolTipText("Hotkey is:"+RatingDialog.PLAY_RECORD_CHAR);

		saveRatingButton = new JButton("Save Rating");
		saveRatingButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				3,6,
				2,1,
				GridBagConstraints.HORIZONTAL);
		add(saveRatingButton,gbc);
		saveRatingButton.setToolTipText("Hotkey is:" + RatingDialog.SAVE_RATING_CHAR);
		
		previousRatingButton = new JButton("Previous Rating");
		previousRatingButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				0,7,
				2,1,
				GridBagConstraints.HORIZONTAL);
		add(previousRatingButton,gbc);
		previousRatingButton.setToolTipText("Hotkey is:"+RatingDialog.PREVIOUS_RATING_CHAR);

		saveToFileButton = new JButton("Save To File");
		saveToFileButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				3,7,
				2,1,
				GridBagConstraints.HORIZONTAL);
		add(saveToFileButton,gbc);
		saveToFileButton.setToolTipText("No hotkey for saveto file");
	}
	
	/**
	 * Create the panel that displays the record being rated.
	 * @return an initialized panel that represents the record
	 * being rated.
	 */
	protected abstract RECORDPANEL createRecordPanel();

	@Override
	public void stateChanged(RECORD newState) {
		recordPanel.updatePanel(newState);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == playButton) {
			controller.play();
		}else if(e.getSource() == saveRatingButton) {
			controller.saveRating(ratingPanel.selectedRating());
		}else if(e.getSource() == previousRatingButton) {
			controller.previousRating();
		}else if(e.getSource() == saveToFileButton) {
			
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = jfc.showSaveDialog(this);
			if(returnVal == JFileChooser.CANCEL_OPTION) {
				return;
			}else if(returnVal == JFileChooser.APPROVE_OPTION) {
				File destinationFile = jfc.getSelectedFile();
				try {
					controller.saveFile(destinationFile);
				} catch (PersistenceException e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage(),
				               "Error Saving File", JOptionPane.ERROR_MESSAGE);
				}
			}else if(returnVal == JFileChooser.ERROR_OPTION) {
				JOptionPane.showMessageDialog(this, "JFileChooser Error",
			               "Error Saving File", JOptionPane.ERROR_MESSAGE);
			}
		}else {
			throw new IllegalArgumentException("Unhandled Event source.");
		}
	}
	
	//Simple key listener for hot keys
	@Override
	public void keyTyped(KeyEvent e) {
		final char typedCharacter = e.getKeyChar();
		
		//BUTTON HOTKEYS
		if(typedCharacter == RatingDialog.PREVIOUS_RATING_CHAR) {
			controller.previousRating();
		}else if(typedCharacter == PLAY_RECORD_CHAR) {
			controller.play();
		}else if(typedCharacter == SAVE_RATING_CHAR) {
			controller.saveRating(ratingPanel.selectedRating());
			//RATING HOTKEYS
		}else if(typedCharacter == VERY_BAD_RATING_CHAR) {
			ratingPanel.setRating(ConsonanceRating.VERY_BAD);
		}else if(typedCharacter == BAD_RATING_CHAR) {
			ratingPanel.setRating(ConsonanceRating.BAD);
		}else if(typedCharacter == MEDIOCRE_RATING_CHAR) {
			ratingPanel.setRating(ConsonanceRating.MEDIOCRE);
		}else if(typedCharacter == GOOD_RATING_CHAR) {
			ratingPanel.setRating(ConsonanceRating.GOOD);
		}else if(typedCharacter == VERY_GOOD_RATING_CHAR) {
			ratingPanel.setRating(ConsonanceRating.VERY_GOOD);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
