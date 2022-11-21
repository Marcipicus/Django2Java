package chord.gui.controller;

/**
 * Class to store the updated state of a
 * class being watched.
 * 
 * @author DAD
 *
 * @param <T> Type of object representing state.
 */
public class StateChangeEvent<T> {
	
	/**
	 * State of the event.
	 */
	private T state;
	
	/**
	 * Create a new StateChangeEvent with the new state
	 * being watched.
	 * @param state new state of object being watched
	 */
	public StateChangeEvent(T state) {
		if(state == null) {
			throw new NullPointerException("state may not be null");
		}
		this.state = state;
	}

	/**
	 * Get the state of the object being watched
	 * @return
	 */
	public T getState() {
		return state;
	}
}
