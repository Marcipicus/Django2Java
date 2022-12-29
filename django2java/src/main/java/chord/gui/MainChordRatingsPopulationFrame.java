package chord.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import chord.gui.components.CustomGridBagConstraints;
import chord.ident.ChordSignature;

public class MainChordRatingsPopulationFrame extends JFrame implements ActionListener{
	//TODO: This class needs to be rewritten to use the new rating guis
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static enum RatingType{
		CHORD_CHANGE_CONSONANCE("Chord Change"),
		CHORD_SCALE_CONSONANCE("Chord Scale"),
		CHORD_NOTE_CONSONANCE("Chord Note");

		private final String displayText; 
		
		private RatingType(String displayText) {
			this.displayText = displayText;
		}
		
		public String getDisplayText() {
			return displayText;
		}
	}


	private JComboBox<ChordSignature> chordComboBox;
	private JComboBox<RatingType> ratingTypeComboBox;
	private JButton launchButton;
	private JButton openExistingFileButton;

	public MainChordRatingsPopulationFrame() {
		super("Chord Consonance File Building Dialog");
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,150);
		setResizable(false);
		setLocationRelativeTo(null); //make the frame displays in the center of the screen
		
		//temporary holder for GridBagConstraints
		//used repeatedly and reset
		GridBagConstraints gbc;

		//Chord Type Selection components
		JLabel chordTypeLabel = new JLabel("Chord Type:");
		gbc = new CustomGridBagConstraints(
						0,0,
						1,1,
						GridBagConstraints.HORIZONTAL);
		add(chordTypeLabel,gbc);
		
		chordComboBox = new JComboBox<ChordSignature>(ChordSignature.values());
		gbc = new CustomGridBagConstraints(
						1,0,
						1,1,
						GridBagConstraints.HORIZONTAL);
		add(chordComboBox,gbc);
		
		//Consonance Type selection components
		JLabel consonanceTypeLabel = new JLabel("Consonance Type:");
		gbc = new CustomGridBagConstraints(
				0,1,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(consonanceTypeLabel, gbc);

		ratingTypeComboBox  = new JComboBox<RatingType>(RatingType.values());
		gbc = new CustomGridBagConstraints(
				1,1,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(ratingTypeComboBox,gbc);

		//Launch Components(Just a JButton)
		launchButton = new JButton("Launch");	
		launchButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				1,2,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(launchButton,gbc);
		
		openExistingFileButton = new JButton("Open Existing File");
		openExistingFileButton.addActionListener(this);
		
		gbc = new CustomGridBagConstraints(
				0,3,
				2,1,
				GridBagConstraints.HORIZONTAL);
		add(openExistingFileButton,gbc);
		
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == launchButton) {
			launch();
		}else if(e.getSource() == openExistingFileButton) {
			openExistingFile();
		}else {
			throw new IllegalArgumentException("There is an unhandled event source");
		}
		
	}
	
	private void launch() {
		ChordSignature chordSig = (ChordSignature)chordComboBox.getSelectedItem();
		RatingType ratingType = (RatingType) ratingTypeComboBox.getSelectedItem();

		switch(ratingType) {
		case CHORD_CHANGE_CONSONANCE:
			//TODO:Add new chord change rating dialog
			break;
		case CHORD_NOTE_CONSONANCE:
			//TODO:Add new chord note rating dialog
			break;
		case CHORD_SCALE_CONSONANCE:
			//TODO:Add new chord scale rating dialog
			break;
		default:
			throw new IllegalArgumentException("Unhandled Rating Type");
		}
	}
	
	private void openExistingFile() {
		File openFileSource;
		JFileChooser jfc = new JFileChooser();
		
		int returnVal = jfc.showOpenDialog(this.getContentPane());
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			openFileSource = jfc.getSelectedFile();
			return;
		} else if( returnVal == JFileChooser.CANCEL_OPTION){
			return;
		} else if (returnVal == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this,"Error saving file\n","ERROR", JOptionPane.ERROR);
		}
	}
	
	public Object openConsonanceFile(Class openFileType, File openFileSource) {
		return null;
	}
}
