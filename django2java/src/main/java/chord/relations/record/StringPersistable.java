package chord.relations.record;

/**
 * Interface to enforce consistent method names
 * through the compiler.
 * @author DAD
 *
 */
public interface StringPersistable {
	
	/**
	 * Character used to separate fields when persisting
	 */
	public static final String FIELD_SEPARATOR = ":";
	
	/**
	 * String used to signify that the field is null.
	 */
	public static final String NULL_SIGNATURE = "null";

	/**
	 * Create a string that will be used to store the 
	 * record.
	 * @return string that represents the record
	 */
	String createPersistenceString();
	
}
