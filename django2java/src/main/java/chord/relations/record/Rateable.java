package chord.relations.record;

/**
 * This interface exists to add an common method to 
 * test if a ConsonanceRecord has a rating in it.
 * @author DAD
 *
 */
public interface Rateable {
	
	/**
	 * Check to see if the record is rated.
	 * @return true if the record has a rating,
	 * false otherwise
	 */
	boolean isRated();
}
