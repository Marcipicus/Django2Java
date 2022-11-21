package chord.gui.controller;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.NoteConsonanceModel;
import chord.relations.NoteConsonanceRecord;

public class NoteConsonanceController {
	
	/**
	 * Model that holds the rankings of note chord interactions.
	 */
	private NoteConsonanceModel noteConsonanceModel;
	/**
	 * Field that holds the chord signature currently being rated.
	 * 
	 * Null if all possible note chord interactions have been rated.
	 */
	private ChordSignature chordSigCurrentlyBeingRated;
	/**
	 * Field that holds the interval currently being rated.
	 * 
	 * Null if all possible note chord interactions have been rated
	 */
	private Interval intervalCurrentlyBeingRated;
	
	/**
	 * List of listeners that are watching for changes to the current
	 * ChordSignature/Interval being rated.
	 */
	private List<StateChangeListener<NoteConsonanceRecord>> stateChangeListeners;
	
	/**
	 * Create a NoteConsonanceController using the given noteConsonanceModel
	 * and initialize the currentChord and currentInterval fields.
	 * @param noteConsonanceModel
	 */
	public NoteConsonanceController(NoteConsonanceModel noteConsonanceModel) {
		if(noteConsonanceModel == null) {
			throw new NullPointerException("noteConsonanceModel may not be null");
		}
		this.noteConsonanceModel = noteConsonanceModel;
		NoteConsonanceRecord noteRecordOfNextUnratedValue = 
				this.noteConsonanceModel.getNextNoteRecordToBeRated();
		
		//All Chord Interval combinations have been filled
		if(noteRecordOfNextUnratedValue == null) {
			this.chordSigCurrentlyBeingRated = null;
			this.intervalCurrentlyBeingRated = null;
		}
		
		//There is at least one unrated chord interval combination
		this.chordSigCurrentlyBeingRated = 
				noteRecordOfNextUnratedValue.chordSignature();
		this.intervalCurrentlyBeingRated = 
				noteRecordOfNextUnratedValue.interval();
		
		stateChangeListeners = new LinkedList<>();
	}
	
	/**
	 * Add a listener that watches for the current chordSignature/interval to be changed.
	 * @param listener new listener
	 */
	public void addStateChangeListener(StateChangeListener<NoteConsonanceRecord> listener) {
		if(listener == null) {
			throw new NullPointerException("listener may not be null");
		}
		
		stateChangeListeners.add(listener);
	}


	/**
	 * Get the chord signature whose note interactions are currently being rated.
	 * 
	 * @return current chord signature of ratings, null if all ratings filled.
	 */
	public ChordSignature getChordSigCurrentlyBeingRated() {
		return chordSigCurrentlyBeingRated;
	}

	/**
	 * Get the interval whose chord interaction is being rated.
	 * 
	 * @return current interval being rated, null if all possible ratings
	 * have already been filled
	 */
	public Interval getIntervalCurrentlyBeingRated() {
		return intervalCurrentlyBeingRated;
	}
	
	/**
	 * Update the current ChordSignature/Interval being rated and notify
	 * all listeners.
	 * @param record new Record.
	 */
	private void updateChordSigIntervalCurrentlyBeingRated(NoteConsonanceRecord record) {
		//all records have been added
		if(record == null) {
			this.chordSigCurrentlyBeingRated = null;
			this.intervalCurrentlyBeingRated = null;
		}else {
			this.chordSigCurrentlyBeingRated = record.chordSignature();
			this.intervalCurrentlyBeingRated = record.interval();
		}
		
		for(StateChangeListener<NoteConsonanceRecord> listener : stateChangeListeners) {
			listener.stateChanged(record);
		}
	}

	/**
	 * Save a rating for the Chord interval pair currently being rated.
	 * 
	 * @param rating consonance rating for the current chord note interaction.
	 */
	public void saveRating(ConsonanceRating rating) {
		noteConsonanceModel.addRating(
				chordSigCurrentlyBeingRated, 
				intervalCurrentlyBeingRated, 
				rating);
		
		NoteConsonanceRecord nextNoteConsonanceRecord = 
				noteConsonanceModel.getNextNoteRecordToBeRated();
		
		//All interactions have been rated
		if(nextNoteConsonanceRecord == null) {
			updateChordSigIntervalCurrentlyBeingRated(null);
		}else {
			updateChordSigIntervalCurrentlyBeingRated(nextNoteConsonanceRecord);
		}
	}
	
	/**
	 * Save the underlying NoteConsonanceModel to the given filename.
	 * @param fileName name of file to save to
	 * @throws FileNotFoundException permission error or attempting to
	 * save to a directory
	 */
	public void saveFile(String fileName) throws FileNotFoundException {
		NoteConsonanceModel.saveToFile(noteConsonanceModel, fileName);
	}
	
	/**
	 * Remove the last rating added and update the current 
	 * chord signature/interval.
	 * 
	 */
	public void previousRating() {
		NoteConsonanceRecord lastRatedRecord = 
				noteConsonanceModel.getRecordOfLastNoteConsonanceRated();
		
		//do nothing if there are no records
		if(lastRatedRecord == null) {
			return;
		}
		
		noteConsonanceModel.removeRating(
				lastRatedRecord.chordSignature(), 
				lastRatedRecord.interval());

		updateChordSigIntervalCurrentlyBeingRated(lastRatedRecord);
	}
}
