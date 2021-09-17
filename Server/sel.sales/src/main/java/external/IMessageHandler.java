package external;

public interface IMessageHandler {
	default boolean handleMessage(String message) {
		if (!this.verify(message)) {
			return false;
		} else {
			this.acknowledge(message);
			this.performNeededAction(message);
			return true;
		}
	}
	boolean verify(String message);
	void acknowledge(String message);
	void performNeededAction(String message);
}
