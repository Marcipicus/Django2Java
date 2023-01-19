package chord.maps;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import chord.Chord;
import chord.Interval;
import chord.MIDINote;
import chord.NoteName;
import chord.Scale;
import chord.exceptions.ChordToneBuildingException;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;
import chord.ident.ChordSignature;
import chord.relations.request.AbstractRecordRequest;
import chord.relations.request.ChordChangeConsonanceRecordRequest;
import chord.relations.request.NoteConsonanceRecordRequest;
import chord.relations.request.ScaleConsonanceRecordRequest;

class ConcreteChord implements Chord{
	public static final String MSG_ROOT_NOTE_MAY_NOT_BE_NULL = "The root note may not be null";
	public static final String MSG_SIGNATURE_MAY_NOT_BE_NULL = "The signature may not be null";

	private final ChordSignature sig;
	private final NoteName root;
	
	/**
	 * Create a chord using the given root note and signature.
	 * @param root root note of the chord
	 * @param sig signature of the chord
	 */
	public ConcreteChord(NoteName root, ChordSignature sig) {
		if(root == null) {
			throw new IllegalArgumentException(MSG_ROOT_NOTE_MAY_NOT_BE_NULL);
		}
		if(sig == null) {
			throw new IllegalArgumentException(MSG_SIGNATURE_MAY_NOT_BE_NULL);
		}
		
		this.root = root;
		this.sig = sig;
	}
	
	@Override
	public NoteName getRoot() {
		return root;
	}

	@Override
	public ChordSignature getSignature() {
		return sig;
	}

	@Override
	public Set<Chord> getRelatedChords(ChordChangeConsonanceRecordRequest relatedChordsRequest){
		validateAbstractRecordRequest(relatedChordsRequest);
		ChordLibrary chordLibrary = ChordLibrary.getInstance();
		
		Set<Chord> relatedChords  = 
				chordLibrary.getRelatedChords(getRoot(), relatedChordsRequest);
		
		return relatedChords;
	}
	
	@Override
	public Set<Scale> getRelatedScales(ScaleConsonanceRecordRequest relatedScalesRequest){
		validateAbstractRecordRequest(relatedScalesRequest);
		
		ChordLibrary chordLibrary = ChordLibrary.getInstance();
		
		Set<Scale> relatedScales =
				chordLibrary.getRelatedScales(root, relatedScalesRequest);
		
		return relatedScales;
	}
	
	@Override
	public Set<NoteName> getRelatedNotes(NoteConsonanceRecordRequest relatedNotesRequest){
		validateAbstractRecordRequest(relatedNotesRequest);
		
		ChordLibrary chordLibrary = ChordLibrary.getInstance();
		
		Set<NoteName> relatedNotes = 
				chordLibrary.getRelatedNotes(root, relatedNotesRequest);
		
		return relatedNotes;
	}
	
	@Override
	public Set<Interval> getRelatedIntervals(NoteConsonanceRecordRequest relatedNotesRequest){
		validateAbstractRecordRequest(relatedNotesRequest);
		
		ChordLibrary chordLibrary = ChordLibrary.getInstance();
		
		Set<Interval> relatedIntervals = 
				chordLibrary.getRelatedIntervals(relatedNotesRequest);
		
		return relatedIntervals;
	}
	
	private void validateAbstractRecordRequest(AbstractRecordRequest request) {
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		if( !request.isInitialized()) {
			throw new IllegalArgumentException("request must be initialized.");
		}
		if( !request.containsReferenceChord(sig)) {
			throw new IllegalArgumentException("request must contain reference chord.");
		}
		if( request.numReferenceChordsRequested() != 1) {
			throw new IllegalArgumentException("request must contain only one reference chord signature.");
		}
	}
	
	@Override
	public List<MIDINote> getTones(int register) 
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
	
	@Override
	public byte[] getTonesInBytes(int register) throws InvalidMIDIValueException, InvalidNoteRegisterException, ChordToneBuildingException {
		List<MIDINote> chordTones = getTones(register);
		
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
		if(!(o instanceof ConcreteChord))
			return false;
		
		ConcreteChord other = (ConcreteChord) o;
		
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
