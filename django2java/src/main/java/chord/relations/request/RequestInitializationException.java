package chord.relations.request;

/**
 * This exception exists to notify the user that there
 * is an error in parameters passed to a Request.
 * @author DAD
 *
 */
public class RequestInitializationException extends Exception {

	public RequestInitializationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RequestInitializationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public RequestInitializationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public RequestInitializationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public RequestInitializationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
