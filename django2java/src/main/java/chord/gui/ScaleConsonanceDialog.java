package chord.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import chord.Chord;
import chord.ConsonanceRating;
import chord.MIDIPlayer;
import chord.NoteName;
import chord.exceptions.ChordToneBuildingException;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;
import chord.gui.components.ChordLabel;
import chord.gui.components.CustomGridBagConstraints;
import chord.gui.components.RatingRadioPanel;
import chord.gui.components.ScaleLabel;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;
import chord.maps.ChordLibrary;
import chord.relations.persist.ScaleConsonance;
import chord.relations.persist.ScaleRating;

public class ScaleConsonanceDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static String FRAME_TITLE = "Scale Consonance Rating";

	private List<ScaleRating> scaleRatings;
	private ChordLabel chordLabel;
	private ScaleLabel scaleLabel;
	private RatingRadioPanel ratingPanel;
	private boolean lastRatingHasBeenSet = false;

	private JButton playButton, previousScaleButton, saveRatingButton,saveToFileButton;
	
	public ScaleConsonanceDialog(JFrame parentFrame, ScaleConsonance scaleConsonanceModel) {
		super(parentFrame, FRAME_TITLE);
		initializeDialog(parentFrame,scaleConsonanceModel.getChordSig());
		
		scaleRatings = scaleConsonanceModel.getScaleRatings();
		int indexOfLastRating = scaleRatings.size()-1;
		
		ScaleRating lastRating = scaleRatings.get(indexOfLastRating);
		ScaleSignature scaleSig= lastRating.getScaleSig();
		
		//if it is the last signature then we want to make sure that we don't rollover
		if(scaleSig.isLastSignature()) {
			scaleLabel.setScaleSignature(scaleSig);
		}else {
			scaleLabel.setScaleSignature(lastRating.getScaleSig().getNextScale());
		}
		
		setVisible(true);
	}

	public ScaleConsonanceDialog(JFrame parentFrame, ChordSignature chordSig) {
		super(parentFrame,FRAME_TITLE);

		initializeDialog(parentFrame,chordSig);
		setVisible(true);
	}
	
	/**
	 * Had to move the initialization to another method so that I can alter the
	 * scaleRatings and such.
	 * @param parentJframe
	 * @param chordSig
	 */
	private void initializeDialog(JFrame parentJframe, ChordSignature chordSig) {
		setLayout(new GridBagLayout());
		setSize(300,250);
		//setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		JLabel emptyLabel1,emptyLabel2,emptyLabel3;

		GridBagConstraints gbc; // temporary holder for GridBagConstraints..reused

		this.scaleRatings = new LinkedList<ScaleRating>();

		chordLabel = new ChordLabel(chordSig);
		gbc = new CustomGridBagConstraints(
				0,0,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(chordLabel,gbc);

		scaleLabel = new ScaleLabel(ScaleSignature.CHROMATIC);
		gbc = new CustomGridBagConstraints(
				0,1,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(scaleLabel,gbc);

		//Adding 3 empty labels so gridbag layout looks nicer with the radio buttons.
		emptyLabel1 = new JLabel(" ");
		gbc = new CustomGridBagConstraints(
				0,2,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(emptyLabel1,gbc);

		emptyLabel2 = new JLabel(" ");
		gbc = new CustomGridBagConstraints(
				0,3,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(emptyLabel2,gbc);

		emptyLabel3 = new JLabel(" ");
		gbc = new CustomGridBagConstraints(
				0,4,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(emptyLabel3,gbc);

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

		previousScaleButton = new JButton("Previous Scale");
		previousScaleButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				0,6,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(previousScaleButton,gbc);

		saveToFileButton = new JButton("Save To File");
		saveToFileButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				1,6,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(saveToFileButton,gbc);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == playButton ) {
			play();
		}else if (e.getSource() == previousScaleButton ) {
			previousScale();
		}else if ( e.getSource() == saveRatingButton) {
			saveRating();
		}else if ( e.getSource() == saveToFileButton) {
			saveToFile();
		}else {
			throw new IllegalArgumentException("Unhandled Event source");
		}
	}

	private void play() {
		try {
			MIDIPlayer player;
			player = MIDIPlayer.getInstance();

			Chord chord = ChordLibrary.getInstance().get(NoteName.A, chordLabel.getChordSignature());
			NoteName rootNote = chord.getRoot();

			player.playChord(chord);
		} catch (InvalidNoteRegisterException e1) {
			JOptionPane.showMessageDialog(this,"Invalid Note Register\n"+e1.toString(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (MidiUnavailableException e1) {
			JOptionPane.showMessageDialog(this,"MIDI Unavailable\n"+e1.toString(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (InvalidMIDIValueException e1) {
			JOptionPane.showMessageDialog(this,"Invalid MIDI Value\n"+e1.toString(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (ChordToneBuildingException e1) {
			JOptionPane.showMessageDialog(this,"Chord Building Error\n"+e1.toString(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	private void previousScale() {
		if(scaleRatings.size() == 0) {
			JOptionPane.showMessageDialog(this,"Already at first scale","INFO", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		int indexOfLastScaleRating = scaleRatings.size()-1;
		scaleRatings.remove(indexOfLastScaleRating);
		
		scaleLabel.setScaleSignature(scaleLabel.getScaleSignature().getPreviousScale());
		
		//We are rewinding here so we want to make sure that the save file
		//actionlistener knows that it is saviong a partial file
		if(lastRatingHasBeenSet) {
			lastRatingHasBeenSet = false;
		}
	}


	private void saveRating() {
		if(lastRatingHasBeenSet) {
			JOptionPane.showMessageDialog(this,"Scale Rating Population Complete.\n Click Save File","INFO", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		ScaleSignature scaleSig = scaleLabel.getScaleSignature();
		ConsonanceRating rating = ratingPanel.selectedRating();

		ScaleRating scaleRating = new ScaleRating();
		scaleRating.setScaleSig(scaleSig);
		scaleRating.setRating(rating);
		scaleRatings.add(scaleRating);

		if(scaleSig.isLastSignature()) {
			lastRatingHasBeenSet = true;
			return;
		}

		scaleLabel.setScaleSignature(scaleSig.getNextScale());
	}	

	private void saveToFile() {
		ScaleConsonance scaleConsonanceModel = new ScaleConsonance();
		scaleConsonanceModel.setChordSig(chordLabel.getChordSignature());
		scaleConsonanceModel.setScaleRatings(scaleRatings);
		
		JFileChooser jfc = new JFileChooser();
		
		String fileName,partialFileString;
		if(lastRatingHasBeenSet) {
			partialFileString = "";
		}else {
			partialFileString = "-part";
		}
		fileName = "Scale-"+scaleConsonanceModel.getChordSig().toString()+partialFileString+".xml";
		
		File fileSaveDestination = new File(fileName);
		jfc.setSelectedFile(fileSaveDestination);//Create a default name for the file
		int returnVal = jfc.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileSaveDestination = jfc.getSelectedFile();
			try {
				saveScaleConsonanceModelToFile(fileSaveDestination,scaleConsonanceModel);
			} catch (JAXBException e1) {
				JOptionPane.showMessageDialog(this,"Error saving file\n"+e1.toString(),"ERROR", JOptionPane.ERROR);
			}
		} else if( returnVal == JFileChooser.CANCEL_OPTION){
			return;
		} else if (returnVal == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this,"Error saving file\n","ERROR", JOptionPane.ERROR);
		}
	}
	
	private void saveScaleConsonanceModelToFile(File destinationFile, ScaleConsonance model) throws JAXBException {
		//This is where a real application would save the file.
		JAXBContext context = JAXBContext.newInstance(ScaleConsonance.class);

		Marshaller marshaller = context.createMarshaller();

		marshaller.marshal(model,destinationFile);
	}
}
