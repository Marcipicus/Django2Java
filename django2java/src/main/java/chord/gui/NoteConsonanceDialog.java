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
import chord.Interval;
import chord.MIDIPlayer;
import chord.NoteName;
import chord.exceptions.ChordToneBuildingException;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;
import chord.gui.components.ChordLabel;
import chord.gui.components.CustomGridBagConstraints;
import chord.gui.components.IntervalLabel;
import chord.gui.components.RatingRadioPanel;
import chord.gui.controller.NoteConsonanceController;
import chord.gui.controller.StateChangeListener;
import chord.ident.ChordSignature;
import chord.maps.ChordLibrary;
import chord.relations.NoteConsonanceRecord;
import chord.relations.persist.NoteConsonance;
import chord.relations.persist.NoteRating;

public class NoteConsonanceDialog extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private NoteConsonanceController ncController;
	private ChordLabel chordLabel;
	private IntervalLabel intervalLabel;
	private RatingRadioPanel ratingPanel;
	private boolean lastNoteHasBeenRated = false;

	private JButton playButton, saveRatingButton,saveToFileButton;

	private LinkedList<NoteRating> noteRatings;

	public NoteConsonanceDialog(JFrame parentFrame, ChordSignature chordSig) {
		super(parentFrame,"Note Consonance Rating");
		setLayout(new GridBagLayout());
		setSize(300,250);
		//setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		JLabel emptyLabel1,emptyLabel2,emptyLabel3;

		GridBagConstraints gbc; // temporary holder for GridBagConstraints..reused

		this.noteRatings = new LinkedList<NoteRating>();

		chordLabel = new ChordLabel(chordSig);
		gbc = new CustomGridBagConstraints(
				0,0,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(chordLabel,gbc);

		intervalLabel = new IntervalLabel(Interval.UNISON);
		gbc = new CustomGridBagConstraints(
				0,1,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(intervalLabel,gbc);

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

		saveToFileButton = new JButton("Save To File");
		saveToFileButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				1,6,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(saveToFileButton,gbc);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == playButton) {
			play();
		}else if(e.getSource() == saveRatingButton) {
			saveRating();
		}else if(e.getSource() == saveToFileButton) {
			saveToFile();
		}else {
			throw new IllegalArgumentException("Unhandled Event source.");
		}
	}

	private void play() {
		try {
			MIDIPlayer player;
			player = MIDIPlayer.getInstance();

			Chord chord = ChordLibrary.getInstance().get(NoteName.A, chordLabel.getChordSignature());
			NoteName rootNote = chord.getRoot();

			player.playChord(chord);
			player.playNote(rootNote);
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

	private void saveRating() {
		if(lastNoteHasBeenRated) {
			JOptionPane.showMessageDialog(this,"Note Rating Population Complete.\n Click Save File","INFO", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		Interval interval = intervalLabel.getInterval();
		ConsonanceRating rating = ratingPanel.selectedRating();

		NoteRating noteRating = new NoteRating();
		noteRating.setNoteInterval(interval);
		noteRating.setRating(rating);
		noteRatings.add(noteRating);

		if(interval.equals(Interval.MAJOR7)) {
			lastNoteHasBeenRated = true; 
			return;
		}

		intervalLabel.setInterval(interval.getNextInterval());
	}
	
	private void saveToFile() {
		NoteConsonance noteConsonanceModel = new NoteConsonance();
		noteConsonanceModel.setChordSig(chordLabel.getChordSignature());
		noteConsonanceModel.setNoteRatings(noteRatings);

		JFileChooser jfc = new JFileChooser();
		File fileSaveDestination = new File("Note-"+noteConsonanceModel.getChordSig().toString()+".xml");
		jfc.setSelectedFile(fileSaveDestination);//Create a default name for the file
		int returnVal = jfc.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileSaveDestination = jfc.getSelectedFile();
			try {
				saveNoteConsonanceModelToFile(fileSaveDestination,noteConsonanceModel);
			} catch (JAXBException e1) {
				JOptionPane.showMessageDialog(this,"Error saving file\n"+e1.toString(),"ERROR", JOptionPane.ERROR);
			}
		} else if( returnVal == JFileChooser.CANCEL_OPTION){
			return;
		} else if (returnVal == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this,"Error saving file\n","ERROR", JOptionPane.ERROR);
		}
		
	}
	
	private void saveNoteConsonanceModelToFile(File destinationFile, NoteConsonance model) throws JAXBException {
		//This is where a real application would save the file.
		JAXBContext context = JAXBContext.newInstance(NoteConsonance.class);

		Marshaller marshaller = context.createMarshaller();

		marshaller.marshal(model,destinationFile);
	}

}
