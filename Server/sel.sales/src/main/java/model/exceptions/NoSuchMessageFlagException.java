package model.exceptions;

public class NoSuchMessageFlagException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;
	
	public NoSuchMessageFlagException(String argument) {
		super("No such message flag: " + argument);
	}
}
