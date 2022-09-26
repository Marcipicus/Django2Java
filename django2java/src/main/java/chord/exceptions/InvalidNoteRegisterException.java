package chord.exceptions;

import chord.NoteName;

public class InvalidNoteRegisterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int givenRegisterValue;
	
	private final NoteName note;
	
	public InvalidNoteRegisterException(String errorMessage, int givenRegisterValue, NoteName note) {
		super(errorMessage);
		
		this.givenRegisterValue = givenRegisterValue;
		this.note = note;
	}
	
	public int getGivenRegisterValue() {
		return this.givenRegisterValue;
	}
	
	public NoteName getNoteName() {
		return note;
	}
}
