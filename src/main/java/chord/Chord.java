package chord;

import java.util.LinkedList;
import java.util.List;

import chord.exceptions.ChordToneBuildingException;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;
import chord.ident.ChordSignature;
import chord.maps.MIDINoteLibrary;

public class Chord {
	public static final String MSG_ROOT_NOTE_MAY_NOT_BE_NULL = "The root note may not be null";
	public static final String MSG_SIGNATURE_MAY_NOT_BE_NULL = "The signature may not be null";

	private final ChordSignature sig;
	private final NoteName root;
	
	//TODO:implement the chord changes
	//private final ChordChangeContainer cc_container;
	
	/**
	 * Create a chord using the given root note and signature.
	 * @param root root note of the chord
	 * @param sig signature of the chord
	 */
	public Chord(NoteName root, ChordSignature sig) {
		if(root == null) {
			throw new IllegalArgumentException(MSG_ROOT_NOTE_MAY_NOT_BE_NULL);
		}
		if(sig == null) {
			throw new IllegalArgumentException(MSG_SIGNATURE_MAY_NOT_BE_NULL);
		}
		
		this.root = root;
		this.sig = sig;
	}
	
	/**
	 * Get the rootNote
	 * @return root note
	 */
	public NoteName getRoot() {
		return root;
	}
	
	/**
	 * Get the signature of the chord
	 * @return
	 */
	public ChordSignature getSignature() {
		return sig;
	}
	
	/**
	 * Get a list of the notes for the chord in MIDI form.
	 * @param register
	 * @return
	 * @throws InvalidMIDIValueException
	 * @throws InvalidNoteRegisterException
	 * @throws ChordToneBuildingException
	 */
	public List<MIDINote> getChordTones(int register) 
			throws InvalidMIDIValueException, 
			InvalidNoteRegisterException, 
			ChordToneBuildingException {
		List<MIDINote> chordTones = new LinkedList<MIDINote>();
		
		MIDINoteLibrary noteLibrary = MIDINoteLibrary.getInstance();

		MIDINote m_rootNote;
		
		m_rootNote = noteLibrary.getNote(getRoot(), register);

		chordTones.add(m_rootNote);
		for(Interval interval : getSignature().getIntervals()) {
			chordTones.add(m_rootNote.getRelatedNote(interval));
		}
			
		return chordTones;
	}
	
	public byte[] getChordTonesInBytes(int register) throws InvalidMIDIValueException, InvalidNoteRegisterException, ChordToneBuildingException {
		List<MIDINote> chordTones = getChordTones(register);
		
		byte[] chordBytes = new byte[chordTones.size()];
		
		int i = 0;
		for(MIDINote note : chordTones) {
			chordBytes[i] = note.getMidiNoteNumber();
			i++;
		}
		
		return chordBytes;
	}
	
	/**
	 * Get a string representing the chord..generally Root note and chord name e.g.Cm or Cmadd9
	 */
	@Override
	public String toString() {
		return root.displayText() + sig.displayText();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Chord))
			return false;
		
		Chord other = (Chord) o;
		
		boolean sameRoot, sameSig;
		
		sameRoot = this.getRoot().equals(other.getRoot());
		sameSig = this.getSignature().equals(other.getSignature());
		
		return sameRoot && sameSig;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 17;
		
		int result = 1;
		
		result = prime*result + getRoot().hashCode();
		result = prime*result + getSignature().hashCode();
		
		return result;
	}
}
