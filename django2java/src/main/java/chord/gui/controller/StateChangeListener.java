package chord.gui.controller;

/**
 * Interface used to update a class with the
 * new state given by the newState parameter.
 * @author DAD
 *
 * @param <T> class of state being updated.
 */
public interface StateChangeListener<T> {
	
	/**
	 * Function to be executed when the state
	 * being watched changes.
	 * 
	 * @param newState updated state
	 */
	void stateChanged(T newState);

}
