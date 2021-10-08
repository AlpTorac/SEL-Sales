package model.exceptions;

public class NoSuchMessageContextException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;
	
	public NoSuchMessageContextException(String argument) {
		super("No such message context: " + argument);
	}
}
