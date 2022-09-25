package testing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import chord.Chord;
import chord.ConsonanceRating;
import chord.Interval;
import chord.MIDINote;
import chord.NoteName;
import chord.exceptions.ChordToneBuildingException;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;
import chord.gui.MainChordRatingsPopulationFrame;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;
import chord.maps.ChordLibrary;
import chord.rating.ChordRatingGUI;
import chord.rating.PracticeGUI;
import chord.relations.ChordChangeConsonance;
import chord.relations.EndChordRatingList;
import chord.relations.IntervalRating;

public class Main {

	public static void main(String[] args) throws JAXBException, InvalidMidiDataException, MidiUnavailableException, InvalidMIDIValueException, InvalidNoteRegisterException, ChordToneBuildingException, InterruptedException {
		//tryPracticeGui();
		//tryMarshallingAndUnMarshalling();
		//tryChordChangeRatingGUI();
		//displayConsonanceCombinations();
		tryMainConsonanceFileBuildingGUI();
		//tryJFileChooser();
		
		//createAndPlayMidiSequence();
	}

	private static void tryJFileChooser() {
		JFrame parentFrame = new JFrame("TestingFrame");
		parentFrame.setLayout(new FlowLayout());
		parentFrame.setSize(300,250);

		JButton openButton, saveButton;
		openButton = new JButton("Open");
		openButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				int returnVal = jfc.showOpenDialog(parentFrame);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					//This is where a real application would open the file.
				} else if( returnVal == JFileChooser.CANCEL_OPTION){
					return;
				} else if (returnVal == JFileChooser.ERROR_OPTION) {
					
				}
			}
		});
		parentFrame.add(openButton);


		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				File fileSaveDestination = new File("Note-"+ChordSignature.U.toString()+".xml");
				jfc.setSelectedFile(fileSaveDestination);
				int returnVal = jfc.showSaveDialog(parentFrame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileSaveDestination = jfc.getSelectedFile();
					//This is where a real application would save the file.
				} else if( returnVal == JFileChooser.CANCEL_OPTION){
					return;
				} else if (returnVal == JFileChooser.ERROR_OPTION) {
					
				}
			}
		});
		parentFrame.add(saveButton);

		parentFrame.setVisible(true);
	}

	private static void tryMainConsonanceFileBuildingGUI() {
		new MainChordRatingsPopulationFrame();
	}
	
	private static void displayConsonanceCombinations() {

		System.out.println("Number of Chord Signatures                   :" + ChordSignature.values().length);
		System.out.println("Number of Scales                             :" + ScaleSignature.values().length);		
		System.out.println("Number of intervals                          :" + 12);

		System.out.println("Number of Combinations for Chord Change File :" + (ChordSignature.values().length * 12));
		System.out.println("Total number of chord changes                :" + (ChordSignature.values().length * ChordSignature.values().length * 12));
		System.out.println("Number of Combinations for Scales            :" + (ScaleSignature.values().length * ChordSignature.values().length));
		System.out.println("Number of Combinations for Note Consonance   :" + ChordSignature.values().length * 12);
	}
	private static void tryChordChangeRatingGUI() {
		ChordRatingGUI ratingGui = new ChordRatingGUI(ChordSignature.MAJOR);

	}

	private static void tryMarshallingAndUnMarshalling() throws JAXBException {
		IntervalRating rating = new IntervalRating(Interval.DIMINISHED12, ConsonanceRating.VERY_GOOD);

		EndChordRatingList endList = new EndChordRatingList();
		endList.setEndsignature(ChordSignature.AUGSUS2);
		List<IntervalRating> intervalRatings = new LinkedList<IntervalRating>();
		intervalRatings.add(rating);
		endList.setIntervalRatings(intervalRatings);

		ChordChangeConsonance mainChordChange = new ChordChangeConsonance();
		mainChordChange.setStartSignature(ChordSignature.MAJOR);

		List<EndChordRatingList> endChordList = new LinkedList<EndChordRatingList>();
		endChordList.add(endList);

		mainChordChange.setEndChordList(endChordList);

		JAXBContext context = JAXBContext.newInstance(ChordChangeConsonance.class);

		Marshaller marshaller = context.createMarshaller();

		marshaller.marshal(mainChordChange,new File("text.xml"));

		Unmarshaller unmarshaller = context.createUnmarshaller();

		Object unmarshalled = unmarshaller.unmarshal(new File("text.xml"));
		ChordChangeConsonance unmChordChange = (ChordChangeConsonance)unmarshalled;

		System.out.println(unmChordChange.getStartSignature());
		for(EndChordRatingList umEndList : unmChordChange.getEndChordList()) {
			System.out.println(umEndList.getEndsignature());
			for(IntervalRating umIntervalRating : umEndList.getIntervalRatings()) {
				System.out.println(umIntervalRating.getIntervalBetweenRoots());
				System.out.println(umIntervalRating.getRating());
			}
		}
	}
	
	//TODO: DOES NOT WORK YET. FOR SOME REASON EVERYTHING SOUNDS 
	//LIKE A MINOR CHORD AT A LOW REGISTER
	private static void createAndPlayMidiSequence() throws InvalidMidiDataException, MidiUnavailableException, InvalidMIDIValueException, InvalidNoteRegisterException, ChordToneBuildingException, InterruptedException {
		final int PPQN = 8;//use eigth notes
		Sequence sequence;
		final Track harmonyTrack,melodyTrack;
		
		sequence = new Sequence(Sequence.PPQ,PPQN);
		harmonyTrack = sequence.createTrack();
		melodyTrack = sequence.createTrack();
		
		//Add messages and add to tracks
		final long quarterTicks = PPQN;
		long tick = 0;
		
		Chord cMajor = ChordLibrary.getInstance().get(NoteName.C, ChordSignature.P5);
		for(MIDINote midiNote : cMajor.getChordTones(4)) {
			final int channel = 0;
			final byte noteValue = midiNote.getMidiNoteNumber();
			
			final ShortMessage noteOn = 
					new ShortMessage(
							ShortMessage.NOTE_ON,
							channel, 
							noteValue);
			harmonyTrack.add(new MidiEvent(noteOn,tick));
			
			//turn the chord off
			long oneWholeNote = tick + 4*PPQN;
			final ShortMessage noteOff = new 
					ShortMessage(
							ShortMessage.NOTE_OFF,
							channel,
							noteValue);
			
			harmonyTrack.add(new MidiEvent(noteOff, oneWholeNote));
			
		}
		
		
		final Sequencer sequencer = MidiSystem.getSequencer();
		sequencer.setSequence(sequence);
		final CountDownLatch waitForEnd = new CountDownLatch(1);
		
		sequencer.addMetaEventListener(e->{
			if(e.getType() == 47) {
				waitForEnd.countDown();
			}
		});
		
		sequencer.open();
		sequencer.start();
		waitForEnd.await();
		
		sequencer.stop();
		sequencer.close();
	}

	private static void tryPracticeGui() {
		PracticeGUI prGUI = new PracticeGUI();
	}

	private static void tryMIDIPlayer() {
		/*
		MIDIPlayer plyr;
		plyr = MIDIPlayer.getInstance();


		MIDINoteLibrary lib = MIDINoteLibrary.getInstance();
		MIDINote note; 
		note = lib.getNote(NoteName.C, 4);


		plyr.playNote(note);
		 */
	}

	private static void buildChordLibrary() {

		/*
		ChordLibrary lib = ChordLibrary.getInstance();

		Charset utf8Charset = Charset.forName("UTF-16");

		PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

		List<Signature> allChordSignatures = Signature.StandardChordSignatures.getAllChordSignatures();

		for(NoteName note : NoteName.values()) {
			for(Signature sig : allChordSignatures) {
				Chord nChord = lib.get(note, sig);
				out.println(nChord);
			}

		}
		 */
	}

}
