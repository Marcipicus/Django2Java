package chord.maps;

import java.util.LinkedList;
import java.util.List;

import chord.Interval;
import chord.MIDINote;
import chord.NoteName;
import chord.Scale;
import chord.exceptions.ChordToneBuildingException;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;
import chord.ident.ScaleSignature;

class ConcreteScale implements Scale {
	
	private final NoteName rootNote;
	private final ScaleSignature scaleSig;

	public ConcreteScale(NoteName rootNote, ScaleSignature scaleSig) {
		if(rootNote == null) {
			throw new NullPointerException("RootNote may not be null");
		}
		if(scaleSig == null) {
			throw new NullPointerException("scaleSig may not be null");
		}
		
		this.rootNote = rootNote;
		this.scaleSig = scaleSig;
	}
	
	@Override
	public NoteName getRootNote() {
		return rootNote;
	}
	
	@Override
	public ScaleSignature getScaleSignature() {
		return scaleSig;
	}
	
	@Override
	public List<MIDINote> getTones(int register) 
			throws InvalidMIDIValueException, 
			InvalidNoteRegisterException, 
			ChordToneBuildingException {
		List<MIDINote> scaleTones = new LinkedList<MIDINote>();
		
		MIDINoteLibrary noteLibrary = MIDINoteLibrary.getInstance();

		MIDINote m_rootNote;
		
		m_rootNote = noteLibrary.getNote(getRootNote(), register);

		for(Interval interval : getScaleSignature().getIntervals()) {
			scaleTones.add(m_rootNote.getRelatedNote(interval));
		}
			
		return scaleTones;
	}
	
	@Override
	public byte[] getTonesInBytes(int register) throws InvalidMIDIValueException, InvalidNoteRegisterException, ChordToneBuildingException {
		List<MIDINote> scaleTones = getTones(register);
		
		byte[] chordBytes = new byte[scaleTones.size()];
		
		int i = 0;
		for(MIDINote note : scaleTones) {
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
		return rootNote.displayText() + scaleSig.displayText();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof ConcreteScale))
			return false;
		
		ConcreteScale other = (ConcreteScale) o;
		
		boolean sameRoot, sameSig;
		
		sameRoot = this.getRootNote().equals(other.getRootNote());
		sameSig = this.getScaleSignature().equals(other.getScaleSignature());
		
		return sameRoot && sameSig;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		
		int result = 1;
		
		result = prime*result + getRootNote().hashCode();
		result = prime*result + getScaleSignature().hashCode();
		
		return result;
	}

}
