package chord.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import chord.MIDINote;
import chord.NoteName;
import chord.exceptions.InvalidMIDIValueException;
import chord.maps.MIDINoteLibrary;

public class MIDINoteLibraryTest {

	private MIDINoteLibrary n_library;
	private MIDINote testNote;
	
	public MIDINoteLibraryTest() {
	}
	
	@Test
	public void testConstructor() {
		n_library = MIDINoteLibrary.getInstance();
	}
	
	@Test
	public void testNoteRetrievalByInteger() throws InvalidMIDIValueException {
		n_library = MIDINoteLibrary.getInstance();
		
		for(int i=21;i<128;i++) {
			testNote = n_library.getNote(i);
			assertEquals((byte) i,testNote.getMidiNoteNumber());
		}
	}
	
	@Test
	public void testNoteRetrievalInvalidNoteNumber() {
		n_library = MIDINoteLibrary.getInstance();
		
		for(int i = 0 ; i<21 ; i++){
			final int testMidiValue = i;
			IllegalArgumentException thrownException = 
					assertThrows(
							IllegalArgumentException.class,
							() -> n_library.getNote(testMidiValue),
							"Library does not throw exception when illegal value requested.");
		}
		
		for(int i = 128 ; i<200 ; i++){
			final int testMidiValue = i;
			IllegalArgumentException thrownException = 
					assertThrows(
							IllegalArgumentException.class,
							() -> n_library.getNote(testMidiValue),
							"Library does not throw exception when illegal value requested.");
		}
		
	}
	
	@Test
	public void testNOteRetrievalByNoteNameAndRegister() {
		//TODO:the library is working fine for now but I should
		//write thte test at some point.
	}

}
