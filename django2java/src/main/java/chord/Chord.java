package chord;

import java.util.List;
import java.util.Set;

import chord.ident.ChordSignature;
import chord.progression.CircularLinkedList;
import chord.relations.request.ChordChangeConsonanceRecordRequest;
import chord.relations.request.NoteConsonanceRecordRequest;
import chord.relations.request.ScaleConsonanceRecordRequest;

public interface Chord extends ToneCollection{
	
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
	 * Get a list of chords between the current chord and the destination chord
	 * using only chord types in the request and limit the number of chords between to
	 * changeDepth or lower
	 * @param destinationChord the chord that we are targeting
	 * @param request request to filter the chordTypes and ratings we desire
	 * @param depth the depth of the recursive algorithm to limit the number of results
	 * @return a list of chord sequences fitting the request
	 */
	List<List<Chord>> getPathToChord(Chord destinationChord,ChordChangeConsonanceRecordRequest request, int depth);
}
