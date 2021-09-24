package external.message;

public class StandardMessageParser extends MessageParser {
	public StandardMessageParser() {
		super(new StandardMessageFormat());
	}
}
