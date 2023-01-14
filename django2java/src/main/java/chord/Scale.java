package chord;

import chord.ident.ScaleSignature;

public class Scale {
	
	private final NoteName rootNote;
	private final ScaleSignature scaleSig;

	public Scale(NoteName rootNote, ScaleSignature scaleSig) {
		if(rootNote == null) {
			throw new NullPointerException("RootNote may not be null");
		}
		if(scaleSig == null) {
			throw new NullPointerException("scaleSig may not be null");
		}
		
		this.rootNote = rootNote;
		this.scaleSig = scaleSig;
	}
	
	public NoteName getRootNote() {
		return rootNote;
	}
	
	public ScaleSignature getScaleSignature() {
		return scaleSig;
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
		if(!(o instanceof Scale))
			return false;
		
		Scale other = (Scale) o;
		
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
