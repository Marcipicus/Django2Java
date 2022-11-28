package chord.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import chord.gui.components.CustomGridBagConstraints;
import chord.gui.components.RatingRadioPanel;
import chord.gui.components.RecordPanel;
import chord.gui.controller.RatingModelController;
import chord.gui.controller.StateChangeListener;
import chord.relations.RatingModel;

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
	MODEL extends RatingModel<RECORD>,
	CONTROLLER extends RatingModelController<RECORD,MODEL>,
	RECORDPANEL extends RecordPanel<RECORD>> extends JDialog 
	implements ActionListener,StateChangeListener<RECORD>{
	
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
		setTitle(getDialogTitle());
		initializeDialog();
		
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
				0,4,
				GridBagConstraints.HORIZONTAL);
		add(recordPanel,gbc);
		
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

		saveToFileButton = new JButton("Save To File");
		saveToFileButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				1,6,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(saveToFileButton,gbc);
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
				} catch (FileNotFoundException e1) {
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

}
