package chord.rating;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JButton;
import javax.swing.JFrame;

import chord.Chord;
import chord.MIDINote;
import chord.MIDIPlayer;
import chord.NoteName;
import chord.exceptions.ChordToneBuildingException;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;
import chord.ident.ChordSignature;

public class PracticeGUI {

	private static class PlayNoteButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				MIDIPlayer.getInstance().playNote(new MIDINote(40));
			} catch (MidiUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidMIDIValueException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	private static class PlayChordButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Chord aMinor = new Chord(NoteName.A,ChordSignature.MINOR);
			try {
				MIDIPlayer.getInstance().playChord(aMinor);
			} catch (InvalidMIDIValueException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidNoteRegisterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ChordToneBuildingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (MidiUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private JFrame frame;
	public PracticeGUI() {
		frame = new JFrame("Chat Frame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300,300);
		frame.setLayout(new FlowLayout());

		JButton playNoteButton = new JButton("Play Note");		
		playNoteButton.addActionListener(new PlayNoteButtonListener());
		frame.add(playNoteButton);

		JButton playChordButton = new JButton("Play Chord");
		playChordButton.addActionListener(new PlayChordButtonListener());
		frame.add(playChordButton);

		frame.setVisible(true);
	}

}
