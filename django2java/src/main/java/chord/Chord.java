package chord;

import java.util.List;
import java.util.Set;

import chord.exceptions.ChordToneBuildingException;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;
import chord.ident.ChordSignature;
import chord.relations.request.ChordChangeConsonanceRecordRequest;
import chord.relations.request.NoteConsonanceRecordRequest;
import chord.relations.request.ScaleConsonanceRecordRequest;

public interface Chord {
	
	/**
	 * Get the rootNote
	 * @return root note
	 */
	NoteName getRoot();
	
	/**
	 * Get the signature of the chord
	 * @return
	 */
	ChordSignature getSignature();

	/**
	 * Get a set of related chords requested by the request.
	 * @param relatedChordsRequest request to filter results.
	 * Request must have only one reference chord. that matches the
	 * chordSignature of the chord calling the function.
	 * @return set of chords requested.
	 */
	Set<Chord> getRelatedChords(ChordChangeConsonanceRecordRequest relatedChordsRequest);
	
	/**
	 * Get a set of related scales filtered by the request passed in
	 * @param relatedScalesRequest request for scales, must contain only one 
	 * reference chord signature matching the chord signature of the calling 
	 * instance
	 * @return set of related scales requested.
	 */
	Set<Scale> getRelatedScales(ScaleConsonanceRecordRequest relatedScalesRequest);
	
	/**
	 * Get a set of notes related to the chord filtered by the request.
	 * @param relatedNotesRequest request defining the notes the caller is
	 * interested in. Must have only one reference chord signature matching the 
	 * chord signature of the calling instance.
	 * @return set of notes related to the chord
	 */
	Set<NoteName> getRelatedNotes(NoteConsonanceRecordRequest relatedNotesRequest);
	
	/**
	 * Get a set of intervals related to the chord and filtered by the request.
	 * @param relatedNotesRequest request that defines the intervals requested.
	 * Must contain only one reference chord signature that matches the chord signature
	 * of calling instance.
	 * @return a set of related intervals that match the request.
	 */
	Set<Interval> getRelatedIntervals(NoteConsonanceRecordRequest relatedNotesRequest);
	
	/**
	 * Get a list of the notes for the chord in MIDI form.
	 * @param register
	 * @return
	 * @throws InvalidMIDIValueException
	 * @throws InvalidNoteRegisterException
	 * @throws ChordToneBuildingException
	 */
	List<MIDINote> getChordTones(int register) 
			throws InvalidMIDIValueException, 
			InvalidNoteRegisterException, 
			ChordToneBuildingException;
	
	/**
	 * Get an array of bytes representing midi notes that will
	 * be consumed by the MIDIPlayer
	 * @param register register which will be used to create the notes
	 * (A register is basically a numbered octave) 
	 * @return array of bytes that can be consumed by the MIDIPlayer
	 * @throws InvalidMIDIValueException
	 * @throws InvalidNoteRegisterException
	 * @throws ChordToneBuildingException
	 */
	byte[] getChordTonesInBytes(int register) 
			throws InvalidMIDIValueException, 
			InvalidNoteRegisterException, 
			ChordToneBuildingException;
}
