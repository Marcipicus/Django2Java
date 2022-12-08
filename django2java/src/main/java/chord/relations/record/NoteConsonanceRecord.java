package chord.relations.record;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.Rateable;

/**
 * Record meant to be used to return Data from the NoteConsonanceModel.
 * @author DAD
 *
 */
public record NoteConsonanceRecord(
		ChordSignature chordSignature, 
		Interval interval, 
		ConsonanceRating rating) implements Rateable{
	
	/**
	 * Create a new NoteConsonanceRecord.
	 * 
	 * Only the rating may be null to signify that there
	 * is no rating for the record.
	 * 
	 * @param chordSignature chordSignature for consonance record.
	 * @param interval interval between root of chordSignature and note
	 * (Must be between UNISON and MAJOR7
	 * @param rating rating for the note/chord consonance, null
	 * if no rating exists.
	 */
	public NoteConsonanceRecord {
		if(chordSignature == null)
			throw new NullPointerException("chordSignature May not be null");
		if(interval == null)
			throw new NullPointerException("interval may not be null");
		if(!interval.inFirstOctave())
			throw new IllegalArgumentException("Only intervals within the first octave are allowed (UNISON->MAJOR7) inclusive");
		//assignments created automagically
	}

	@Override
	public boolean isRated() {
		return rating!=null;
	}
}
