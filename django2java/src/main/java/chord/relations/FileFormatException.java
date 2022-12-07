package chord.relations;

/**
 * Exception used to indicate to user that the program was handed
 * a file with invalid data for the code used to parse it.
 * 
 * Used mainly for the ConsonanceModels' load from file methods.
 * @author DAD
 *
 */
public class FileFormatException extends RuntimeException {

	//TODO:change to checked exception later
	public FileFormatException() {
		super();
	}

	public FileFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FileFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileFormatException(String message) {
		super(message);
	}

	public FileFormatException(Throwable cause) {
		super(cause);
	}

	
}
