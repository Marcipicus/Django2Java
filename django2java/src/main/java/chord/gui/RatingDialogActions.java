package chord.gui;

/**
 * Interface used to access functions of the RatingDialog through 
 * WindowAdapter ....The generics were too
 * complicated to implement a window closing event so I am
 * using this interface instead.
 * @author DAD
 *
 */
public interface RatingDialogActions {
	
	/**
	 * Play the current rating record.
	 */
	void play();
	
	/**
	 * save the current rating.
	 */
	void saveRating();
	
	/**
	 * Go back to the previous rating.
	 */
	void previousRating();
	
	/**
	 * Save the current model to file.
	 */
	void saveFile();

}
