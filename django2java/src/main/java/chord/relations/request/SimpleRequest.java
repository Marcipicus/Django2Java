package chord.relations.request;

/**
 * SimpleRequest exists to provide a consistent 
 * interface to the base request classes(
 * @author DAD
 *
 * @param <DATA_TYPE>
 */
public interface SimpleRequest<DATA_TYPE> {

	/**
	 * Add all of the values to the request.
	 * @param requestedValues values to look for
	 * @throws ReqeustInitializationException if the data
	 * given is invalid(contains null values or duplicates or
	 * if there is some other subclass specific error in the data.)
	 */
	void add(DATA_TYPE... requestedValues) throws ReqeustInitializationException;
	
	/**
	 * Add all of the possible values for the data type
	 * to the request.
	 */
	void addAll();
	
	/**
	 * Test to see if the request is looking for
	 * the value.
	 * @param value value to test
	 * @return true if the request is looking for the
	 * given value, false otherwise
	 */
	boolean contains(DATA_TYPE value);
	
	/**
	 * Test to see if the request is properly initialized.
	 * Specifically we test to see if the request contains
	 * at least one value to look for.
	 * @return true if the request is ready to be used,
	 * false otherwise
	 */
	boolean isInitialized();
}
