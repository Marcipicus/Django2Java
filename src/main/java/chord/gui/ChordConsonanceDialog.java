package chord.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import chord.gui.components.ChordLabel;
import chord.gui.components.CustomGridBagConstraints;
import chord.ident.ChordSignature;
import chord.relations.ChordChangeConsonance;
import chord.relations.EndChordRatingList;

public class ChordConsonanceDialog extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String CONSONANCE_DIALOG_TITLE = "Chord Consonance Rating";
	
	private List<EndChordRatingList> endChordRatingsList;
	private boolean lastChordChangeHasBeenRated = false;

	private ChordLabel startChordLabel, endChordLabel;
	private JButton rateChangesButton;
	private JButton previousEndChordButton;
	private JButton saveToFileButton;

	public ChordConsonanceDialog(JFrame parentFrame, ChordSignature chordSig) {
		super(parentFrame,CONSONANCE_DIALOG_TITLE);
		initializeDialog(chordSig,ChordSignature.U,new LinkedList<EndChordRatingList>());

		setVisible(true);
	}

	public ChordConsonanceDialog(JFrame parentFrame, ChordChangeConsonance chordConsonanceModel) {
		super(parentFrame,CONSONANCE_DIALOG_TITLE);
		
		List<EndChordRatingList> endChordListFromModel;
		ChordSignature startChordSig, endChordSig;
		
		endChordListFromModel = chordConsonanceModel.getEndChordList();
		
		int indexOfLastElement = endChordListFromModel.size()-1;
		startChordSig = chordConsonanceModel.getStartSignature();
		endChordSig = chordConsonanceModel.getEndChordList().get(indexOfLastElement).getEndsignature();
		
		if(!endChordSig.isHighestValue()) {
			endChordSig = endChordSig.getNextChordSignature();
		}else {
			lastChordChangeHasBeenRated = true;
		}
		

		initializeDialog(startChordSig,endChordSig,endChordListFromModel);

		setVisible(true);
	}
	
	private void initializeDialog(ChordSignature startChordSig, ChordSignature endChordSig, List<EndChordRatingList> endChordRatingList) {
		setLayout(new GridBagLayout());
		setSize(300,250);
		//setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		JLabel emptyLabel2,emptyLabel3;

		GridBagConstraints gbc; // temporary holder for GridBagConstraints..reused

		this.endChordRatingsList = endChordRatingList;

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

		JLabel emptyLabel1 = new JLabel(" ");
		gbc = new CustomGridBagConstraints(
				0,2,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(emptyLabel1,gbc);

		//adding empty labels for gridbag layout alignment
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

		rateChangesButton = new JButton("Rate Changes By Interval");

		rateChangesButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				1,0,
				1,4,
				GridBagConstraints.BOTH);
		add(rateChangesButton,gbc);

		previousEndChordButton = new JButton("Previous End Chord");
		previousEndChordButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				0,5,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(previousEndChordButton,gbc);

		saveToFileButton = new JButton("Save To File");
		saveToFileButton.addActionListener(this);
		gbc = new CustomGridBagConstraints(
				1,5,
				1,1,
				GridBagConstraints.HORIZONTAL);
		add(saveToFileButton,gbc);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == previousEndChordButton) {
			previousEndChord();
		}else if(e.getSource() == rateChangesButton) {
			rateChanges();
		}else if( e.getSource() == saveToFileButton) {
			saveToFile();
		}else {
			throw new IllegalArgumentException("Unhandled event source.");
		}
	}
	
	private void previousEndChord() {
		boolean noRatingsYetAdded = endChordRatingsList.size() == 0;
		
		if(noRatingsYetAdded) {
			JOptionPane.showMessageDialog(ChordConsonanceDialog.this,"Already at first Chord rating.","INFO", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		int indexOfLastElement = endChordRatingsList.size() - 1;
		
		endChordRatingsList.remove(indexOfLastElement);
		
		if(indexOfLastElement == 0) {
			endChordLabel.setChordSignature(ChordSignature.U);
		}else {
			//update the end chord label
			endChordLabel.setChordSignature((endChordRatingsList.get(indexOfLastElement - 1).getEndsignature()));
		}
		
		//if we have just gone to a previous value then it is guaranteed that the
		//last chordchange has not been rated
		lastChordChangeHasBeenRated = false;
	}
	
	private void rateChanges() {
		boolean allRatingsFilled = endChordRatingsList.size() == ChordSignature.values().length;
		
		if(allRatingsFilled) {
			JOptionPane.showMessageDialog(ChordConsonanceDialog.this,
					"All Chords Have Been Rated\nClick Save File","INFO", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		ChordConsonanceRatingDialog chordRatingDialog = 
				new ChordConsonanceRatingDialog(
						this, 
						startChordLabel.getChordSignature(), 
						endChordLabel.getChordSignature());
		
		endChordRatingsList.add(chordRatingDialog.showDialog());
		endChordLabel.setChordSignature(endChordLabel.getChordSignature().getNextChordSignature());
	}
	
	private void saveToFile() {
		ChordChangeConsonance chordChangeConsonanceModel = new ChordChangeConsonance();
		chordChangeConsonanceModel.setStartSignature(startChordLabel.getChordSignature());
		chordChangeConsonanceModel.setEndChordList(endChordRatingsList);
		
		JFileChooser jfc = new JFileChooser();
		
		String fileName,partialFileString;
		if(lastChordChangeHasBeenRated) {
			partialFileString = "";
		}else {
			partialFileString = "-part";
		}
		fileName = "Chord-"+chordChangeConsonanceModel.getStartSignature().toString()+partialFileString+".xml";
		
		File fileSaveDestination = new File(fileName);
		jfc.setSelectedFile(fileSaveDestination);//Create a default name for the file
		int returnVal = jfc.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			fileSaveDestination = jfc.getSelectedFile();
			try {
				saveScaleConsonanceModelToFile(fileSaveDestination,chordChangeConsonanceModel);
			} catch (JAXBException e1) {
				JOptionPane.showMessageDialog(this,"Error saving file\n"+e1.toString(),"ERROR", JOptionPane.ERROR);
			}
		} else if( returnVal == JFileChooser.CANCEL_OPTION){
			return;
		} else if (returnVal == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(this,"Error saving file\n","ERROR", JOptionPane.ERROR);
		}
	}
	
	private void saveScaleConsonanceModelToFile(File destinationFile, ChordChangeConsonance model) throws JAXBException {
		//This is where a real application would save the file.
		JAXBContext context = JAXBContext.newInstance(ChordChangeConsonance.class);

		Marshaller marshaller = context.createMarshaller();

		marshaller.marshal(model,destinationFile);
	}
}
