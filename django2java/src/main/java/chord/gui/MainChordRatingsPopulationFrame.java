package chord.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import chord.gui.components.CustomGridBagConstraints;
import chord.gui.controller.ChordChangeConsonanceController;
import chord.gui.controller.NoteConsonanceController;
import chord.gui.controller.ScaleConsonanceController;
import chord.relations.ChordChangeConsonanceModel;
import chord.relations.NoteConsonanceModel;
import chord.relations.ScaleConsonanceModel;
import chord.relations.persist.PersistenceException;
import chord.relations.persist.file.ChordChangeConsonanceFilePersistStrategy;
import chord.relations.persist.file.FileStrategyConfig;
import chord.relations.persist.file.NoteConsonanceFilePersistStrategy;
import chord.relations.persist.file.ScaleConsonanceFilePersistStrategy;
import chord.relations.request.ChordChangeConsonanceRecordRequest;
import chord.relations.request.NoteConsonanceRecordRequest;
import chord.relations.request.ScaleConsonanceRecordRequest;

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

	private JComboBox<RatingType> ratingTypeComboBox;
	private JCheckBox useExistingFileCheckBox;
	private JButton launchButton;

	public MainChordRatingsPopulationFrame() {
		super("Chord Consonance File Building Dialog");
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400,150);
		setResizable(false);
		setLocationRelativeTo(null); //make the frame display in the center of the screen

		//temporary holder for GridBagConstraints
		//used repeatedly and reset
		GridBagConstraints gbc;

		ratingTypeComboBox  = new JComboBox<RatingType>(RatingType.values());
		gbc = new CustomGridBagConstraints(
				0,0,
				3,1,
				GridBagConstraints.HORIZONTAL);
		add(ratingTypeComboBox,gbc);

		useExistingFileCheckBox  = new JCheckBox("Use Existing File",false);
		gbc = new CustomGridBagConstraints(
				0,1,
				3,1,
				GridBagConstraints.HORIZONTAL);
		add(useExistingFileCheckBox,gbc);

		//Launch Components(Just a JButton)
		launchButton = new JButton("Launch");	
		launchButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				0,2,
				3,1,
				GridBagConstraints.HORIZONTAL);
		add(launchButton,gbc);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == launchButton) {
			launch();
		}
	}

	/**
	 * Call up JFileChooser and get the user to select an
	 * existing file.
	 * @return file used to initialize rating dialogs, null
	 * is returned if the user cancels the selection or there
	 * is some other error.
	 */
	private File getExistingFile() {
		File openFileSource;
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnVal = jfc.showOpenDialog(this.getContentPane());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			openFileSource = jfc.getSelectedFile();
			return openFileSource;
		} else if( returnVal == JFileChooser.CANCEL_OPTION){
			JOptionPane.showMessageDialog(this,"User Canceled File Select Dialog\n","INFO", JOptionPane.INFORMATION_MESSAGE);
		} else if (returnVal == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this,"Error selecting file\n","ERROR", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	private void launch() {
		boolean useExistingFile = useExistingFileCheckBox.isSelected();
		RatingType ratingType = (RatingType) ratingTypeComboBox.getSelectedItem();
		FileStrategyConfig persisterConfig = null;
		File sourceFile = null;

		if(useExistingFile) {
			//calls up JFileChooser and displays messages
			//if there is an error
			//null is returned
			sourceFile = getExistingFile();
			if(sourceFile == null) {
				return;
			}
			persisterConfig = new FileStrategyConfig(sourceFile);
		}

		try {
			switch(ratingType) {
			case CHORD_CHANGE_CONSONANCE:
				launchChordChangeConsonanceRatingDialog(persisterConfig);
				break;
			case CHORD_NOTE_CONSONANCE:
				launchNoteConsonanceRatingDialog(persisterConfig);
				break;
			case CHORD_SCALE_CONSONANCE:
				launchScaleConsonanceRatingDialog(persisterConfig);
				break;
			default:
				throw new IllegalArgumentException("Unhandled Rating Type");
			}
		}catch(PersistenceException e) {
			JOptionPane.showMessageDialog(this,"Error loading from ratings from file\n"+e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Launch the chord change consonance rating dialog using the given config.
	 * If the config is null then a new Model will be created.
	 * 
	 * @param fileStrategyConfig configuration used to load previously saved data from
	 * file, PASS NULL IF YOU DO NOT WANT TO LOAD FROM FILE
	 * @throws PersistenceException 
	 */
	private void launchChordChangeConsonanceRatingDialog(FileStrategyConfig fileStrategyConfig) throws PersistenceException {
		ChordChangeConsonanceModel cccModel;
		ChordChangeConsonanceController cccController;
		if(fileStrategyConfig == null) {
			cccModel = new ChordChangeConsonanceModel();
		}else {
			ChordChangeConsonanceFilePersistStrategy chordChangePersistStrategy = 
					new ChordChangeConsonanceFilePersistStrategy(
							fileStrategyConfig, 
							ChordChangeConsonanceRecordRequest.allPossibleRecords());
			cccModel = chordChangePersistStrategy.load();
		}

		cccController = new ChordChangeConsonanceController(cccModel);
		ChordChangeConsonanceDialog cccDialog = new ChordChangeConsonanceDialog(this, cccController);
	}

	/**
	 * Launch the chord change consonance rating dialog using the given config.
	 * If the config is null then a new Model will be created.
	 * 
	 * @param fileStrategyConfig configuration used to load previously saved data from
	 * file, PASS NULL IF YOU DO NOT WANT TO LOAD FROM FILE
	 * @throws PersistenceException 
	 */
	private void launchScaleConsonanceRatingDialog(FileStrategyConfig fileStrategyConfig) throws PersistenceException {
		ScaleConsonanceModel scModel;
		ScaleConsonanceController scController;
		if(fileStrategyConfig == null) {
			scModel = new ScaleConsonanceModel();
		}else {
			ScaleConsonanceFilePersistStrategy scaleConsonancePersistStrategy = 
					new ScaleConsonanceFilePersistStrategy(
							fileStrategyConfig, 
							ScaleConsonanceRecordRequest.allPossibleRecords());
			scModel = scaleConsonancePersistStrategy.load();
		}

		scController = new ScaleConsonanceController(scModel);
		ScaleConsonanceDialog scDialog = new ScaleConsonanceDialog(this, scController);
	}

	/**
	 * Launch the chord change consonance rating dialog using the given config.
	 * If the config is null then a new Model will be created.
	 * 
	 * @param fileStrategyConfig configuration used to load previously saved data from
	 * file, PASS NULL IF YOU DO NOT WANT TO LOAD FROM FILE
	 * @throws PersistenceException 
	 */
	private void launchNoteConsonanceRatingDialog(FileStrategyConfig fileStrategyConfig) throws PersistenceException {
		NoteConsonanceModel ncModel;
		NoteConsonanceController ncController;
		if(fileStrategyConfig == null) {
			ncModel = new NoteConsonanceModel();
		}else {
			NoteConsonanceFilePersistStrategy noteConsonancePersistStrategy = 
					new NoteConsonanceFilePersistStrategy(
							fileStrategyConfig, 
							NoteConsonanceRecordRequest.allPossibleRecords());
			ncModel = noteConsonancePersistStrategy.load();
		}

		ncController = new NoteConsonanceController(ncModel);
		NoteConsonanceDialog ncDialog = new NoteConsonanceDialog(this, ncController);
	}
}
