package chord.exceptions;

/**
 * Generci Exception used to wrap a list of Exceptions caused
 * by trying to process invalid MIDI.
 * @author DAD
 *
 */
public class GenericMIDIException extends Exception {

	public GenericMIDIException(Throwable cause) {
		super(cause);
	}

	public GenericMIDIException(String message, Throwable cause) {
		super(message, cause);
	}

}
