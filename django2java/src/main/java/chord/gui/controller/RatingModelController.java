package chord.gui.controller;

import java.util.LinkedList;
import java.util.List;

import chord.ConsonanceRating;
import chord.relations.RatingModel;

/**
 * This class is used as a mediator between the gui and 
 * the various data models.
 * @author DAD
 *
 * @param <RECORD> the type of record we are using
 * @param <MODEL> the model we are using
 */
public abstract class RatingModelController<RECORD, MODEL extends RatingModel<RECORD>> {

	/**
	 * Data model for controller
	 */
	protected MODEL model;

	/**
	 * Current record being rated.
	 */
	protected RECORD currentRecord;

	/**
	 * List of listeners for changes to the currentRecord
	 */
	private List<StateChangeListener<RECORD>> stateChangeListeners;

	/**
	 * Create the controller using the given initialized model and 
	 * state change listeners.
	 * @param model initialized model
	 * @param listeners state change listeners 
	 */
	@SafeVarargs
	public RatingModelController(MODEL model, StateChangeListener<RECORD>... listeners) {
		if(model == null) {
			throw new NullPointerException("model may not be null");
		}
		if(listeners==null) {
			throw new NullPointerException("listeners may not be null");
		}
		this.model = model;
		this.stateChangeListeners = new LinkedList<>();

		for(StateChangeListener<RECORD> listener : listeners) {
			addStateChangeListener(listener);
		}

		RECORD recordOfNextUnratedValue = 
				model.getNextRecordToBeRated();

		updateCurrentRecordBeingRated(recordOfNextUnratedValue);
	}

	/**
	 * Add a listener that watches for the current chordSignature/interval to be changed.
	 * @param listener new listener
	 */
	public void addStateChangeListener(StateChangeListener<RECORD> listener) {
		if(listener == null) {
			throw new NullPointerException("listener may not be null");
		}

		stateChangeListeners.add(listener);
	}

	/**
	 * Update the current ChordSignature/Interval being rated and notify
	 * all listeners.
	 * @param record new Record.
	 */
	private void updateCurrentRecordBeingRated(RECORD record) {
		//all records have been added
		this.currentRecord = record;

		for(StateChangeListener<RECORD> listener : stateChangeListeners) {
			listener.stateChanged(record);
		}
	}

	/**
	 * Save a rating for the Chord interval pair currently being rated.
	 * 
	 * @param rating consonance rating for the current chord note interaction.
	 */
	public void saveRating(ConsonanceRating rating) {
		if(rating == null) {
			throw new NullPointerException("rating may not be null");
		}
		if(currentRecord == null) {
			return;
		}
		RECORD recordToSave = createRecordToSave(rating);
		model.addRating(recordToSave);

		if(model.isFull()) {
			updateCurrentRecordBeingRated(null);
		}else {
			RECORD nextNoteConsonanceRecord = 
					model.getNextRecordToBeRated();
			updateCurrentRecordBeingRated(nextNoteConsonanceRecord);
		}

	}

	/**
	 * Erase the most recent record that was added and
	 * update the current record to modify it.
	 */
	public void previousRating() {
		RECORD lastRecordRated,nextRecordToBeRated;
		lastRecordRated = model.getLastRecordRated();

		if(model.isEmpty()) {
			return;
		}

		model.removeRating(lastRecordRated);

		nextRecordToBeRated = model.getNextRecordToBeRated();

		updateCurrentRecordBeingRated(nextRecordToBeRated);
	}

	/**
	 * Take the model and save it to file.
	 */
	public void saveFile() {

	}

	/**
	 * Take the current record being rated and save it to the model
	 * with the given rating.
	 * @param rating consonance rating of record
	 * @return record filled with the given rating.
	 */
	protected abstract RECORD createRecordToSave(ConsonanceRating rating);

}
