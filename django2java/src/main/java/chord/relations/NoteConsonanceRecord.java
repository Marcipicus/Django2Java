package chord.relations;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;

/**
 * Record meant to be used to return Data from the NoteConsonanceModel.
 * @author DAD
 *
 */
public record NoteConsonanceRecord(
		ChordSignature chordSignature, 
		Interval interval, 
		ConsonanceRating rating) {
	
	/**
	 * Create a new NoteConsonanceRecord.
	 * 
	 * No parameters may be null.
	 * 
	 * @param chordSignature chordSignature for consonance
	 * @param interval interval between root of chordSignature and note
	 * (Must be between UNISON and MAJOR7
	 * @param rating rating for the note/chord consonance
	 */
	public NoteConsonanceRecord {
		if(chordSignature == null)
			throw new NullPointerException("chordSignature May not be null");
		if(interval == null)
			throw new NullPointerException("interval may not be null");
		if(!interval.inFirstOctave())
			throw new IllegalArgumentException("Only intervals within the first octave are allowed (UNISON->MAJOR7) inclusive");
		if(rating == null) 
			throw new NullPointerException("rating may not be null");
		
		//assignments created automagically
	}

}
