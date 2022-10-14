package chord;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import chord.MIDINote;
import chord.NoteName;
import chord.exceptions.InvalidMIDIValueException;

public class MIDINoteTest {

	private MIDINote testNote;


	public MIDINoteTest() {
	}

	//This method only exists to avoid the type cast
	private void runConstructorWithValue(int midiValue) throws InvalidMIDIValueException {
		testNote = new MIDINote( midiValue);
	}
	
	@Test
	public void testMIDINoteConstructorInvalidValues() {
		//Test values too low
		for(int i = 0 ; i<21 ; i++){
			final int testMidiValue = i;
			InvalidMIDIValueException thrownException = 
					assertThrows(
							InvalidMIDIValueException.class,
							() -> runConstructorWithValue(testMidiValue),
							"Constructor does not throw an exception when given an invalid value");
		}

		for(int i = 128 ; i<200 ; i++){
			final int testMidiValue = i;
			InvalidMIDIValueException thrownException = 
					assertThrows(
							InvalidMIDIValueException.class,
							() -> runConstructorWithValue(testMidiValue),
							"Constructor does not throw an exception when given an invalid value");
		}
	}

	@Test
	public void testMIDINoteConstructorValidValues() throws InvalidMIDIValueException {
		for(int i =21 ; i<128 ; i++){
			testNote = new MIDINote((byte) i);
		}
	}

	/**
	 * This test is used to make sure that the note names are retrieved properly.
	 * @throws InvalidMIDIValueException 
	 */
	@Test
	public void testGetNoteName() throws InvalidMIDIValueException {
		for (byte midiNumber = 21 ; midiNumber<128 ;) {

			testNote = new MIDINote(midiNumber);
			assertEquals(NoteName.A, testNote.getNoteName());

			midiNumber++;
			testNote = new MIDINote(midiNumber);
			assertEquals(NoteName.A_SHARP, testNote.getNoteName());

			midiNumber++;
			testNote = new MIDINote(midiNumber);
			assertEquals(NoteName.B , testNote.getNoteName());

			midiNumber++;
			testNote = new MIDINote(midiNumber);
			assertEquals(NoteName.C, testNote.getNoteName());

			midiNumber++;
			testNote = new MIDINote(midiNumber);
			assertEquals(NoteName.C_SHARP, testNote.getNoteName());

			midiNumber++;
			testNote = new MIDINote(midiNumber);
			assertEquals(NoteName.D, testNote.getNoteName());

			midiNumber++;
			testNote = new MIDINote(midiNumber);
			assertEquals(NoteName.D_SHARP, testNote.getNoteName());

			midiNumber++;
			testNote = new MIDINote(midiNumber);
			assertEquals(NoteName.E, testNote.getNoteName());

			midiNumber++;
			testNote = new MIDINote(midiNumber);
			assertEquals(NoteName.F, testNote.getNoteName());

			midiNumber++;
			testNote = new MIDINote(midiNumber);
			assertEquals(NoteName.F_SHARP, testNote.getNoteName());

			midiNumber++;
			testNote = new MIDINote(midiNumber);
			assertEquals(NoteName.G, testNote.getNoteName());

			//This break is here so that the byte doesn't roll over
			if(midiNumber == 127) {
				break;
			}

			midiNumber++;
			testNote = new MIDINote(midiNumber);
			assertEquals(NoteName.G_SHARP, testNote.getNoteName());

			midiNumber++;
		}	
	}

	@Test
	public void testGetRegisterValue() throws InvalidMIDIValueException {
		byte expectedRegister = 0;
		
		testNote = new MIDINote((byte)21);
		assertEquals(expectedRegister , testNote.getNoteRegister());
		
		testNote = new MIDINote((byte) 22);
		assertEquals(expectedRegister, testNote.getNoteRegister());

		testNote = new MIDINote((byte) 23);
		assertEquals(expectedRegister, testNote.getNoteRegister());

		//Check all of the values by octave to make sure that their 
		//register values are correct
		for(byte midiValue = 24 ; midiValue<127 ;) {
			expectedRegister++;
			for(int i = 0 ; i<12; i++) {
				testNote = new MIDINote((byte) midiValue);
				assertEquals(expectedRegister, testNote.getNoteRegister());
				
				midiValue++;
				if(midiValue == 127) {
					break;
				}
			}
		}
		

		testNote = new MIDINote((byte) 127);
		assertEquals((byte)9, testNote.getNoteRegister());
	}
	
	@Test
	public void testRetrieveMIDINoteNumber() throws InvalidMIDIValueException {
		//I have verified that the constructors are creating the MIDINote properly
		//therefore I only need to make sure that the MIDI number is retrieved properly.
		for(int midiNumber = 21; midiNumber <= 127 ; midiNumber++) {
			testNote = new MIDINote((byte)midiNumber);
			
			
			assertEquals(midiNumber, testNote.getMidiNoteNumber());
		}
	}
	
}
