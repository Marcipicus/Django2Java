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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import chord.gui.components.CustomGridBagConstraints;
import chord.ident.ChordSignature;
import chord.relations.persist.P_ChordChangeConsonance;
import chord.relations.persist.P_NoteConsonance;
import chord.relations.persist.P_ScaleConsonance;

public class MainChordRatingsPopulationFrame extends JFrame implements ActionListener{

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
			new ChordConsonanceDialog(this, chordSig);
			break;
		case CHORD_NOTE_CONSONANCE:
			new NoteConsonanceDialog(this, chordSig);
			break;
		case CHORD_SCALE_CONSONANCE:
			new ScaleConsonanceDialog(this, chordSig);
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
			try {
				if(openFileSource.getName().contains("Note")) {
					//TODO: Create Note ConsonanceDialog
					//TODO: THIS MIGHT NOT BE NECESSARY SINCE IT TAKES ABOUT 1/2 A MINUTE TO
					//		POPULATE THE NOTE RATINGS FILE
					P_NoteConsonance noteConsonanceModel = (P_NoteConsonance)openConsonanceFile(P_NoteConsonance.class, openFileSource);
				}else if(openFileSource.getName().contains("Scale")){
					P_ScaleConsonance scaleConsonanceModel = (P_ScaleConsonance)openConsonanceFile(P_ScaleConsonance.class, openFileSource);
					new ScaleConsonanceDialog(this, scaleConsonanceModel);
				}else if(openFileSource.getName().contains("Chord")) {
					//TODO: Create Chord Consonance dialog
					P_ChordChangeConsonance chordConsonanceModel = (P_ChordChangeConsonance)openConsonanceFile(P_ChordChangeConsonance.class, openFileSource);
					ChordConsonanceDialog chordRatingDialog = new ChordConsonanceDialog(this,chordConsonanceModel);
				}else {
					JOptionPane.showMessageDialog(this,"Invalid File","ERROR", JOptionPane.ERROR);
					return;
				}
			} catch (JAXBException e1) {
				JOptionPane.showMessageDialog(this,"Error saving file\n"+e1.toString(),"ERROR", JOptionPane.ERROR);
			}
		} else if( returnVal == JFileChooser.CANCEL_OPTION){
			return;
		} else if (returnVal == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this,"Error saving file\n","ERROR", JOptionPane.ERROR);
		}
	}
	
	public Object openConsonanceFile(Class openFileType, File openFileSource) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(openFileType);
		
		Unmarshaller unmarshaller = context.createUnmarshaller();

		Object unmarshalled = unmarshaller.unmarshal(openFileSource);
		return unmarshalled;
	}
}
