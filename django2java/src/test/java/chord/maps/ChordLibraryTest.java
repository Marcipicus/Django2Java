package chord.maps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import chord.Chord;
import chord.NoteName;
import chord.ident.ChordSignature;
import chord.relations.ChordChangeConsonanceModel;
import chord.relations.NoteConsonanceModel;
import chord.relations.ScaleConsonanceModel;

public class ChordLibraryTest {


	@Test
	void testChordRetrieval() {
		ChordLibrary.initializeChordLibraryInstance(
				new ChordChangeConsonanceModel(),
				new ScaleConsonanceModel(), 
				new NoteConsonanceModel());
		
		ChordLibrary chordLibrary = ChordLibrary.getInstance();
		
		for(ChordSignature chordSig : ChordSignature.values()) {
			for(NoteName rootNote : NoteName.values()) {
				Chord chord = chordLibrary.getChord(rootNote, chordSig);
				
				assertEquals(chordSig,chord.getSignature());
				assertEquals(rootNote,chord.getRoot());
			}
		}
	}

}
