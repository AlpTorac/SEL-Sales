package external.message;

import model.util.IParser;

public interface IMessageParser extends IParser {
	IMessage parseMessage(String message);
}
