package chord.exceptions;

/**
 * The InvalidMIDIException is used if the 
 * @author DAD
 *
 */
public class InvalidMIDIValueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int givenMIDIValue;
	public InvalidMIDIValueException(String errorMessage, int givenMIDIValue) {
		super(errorMessage);
		
		this.givenMIDIValue = givenMIDIValue;
	}
	
	public int getGivenMIDIValue() {
		return this.givenMIDIValue;
	}
	

}
