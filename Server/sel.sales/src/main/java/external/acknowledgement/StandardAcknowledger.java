package external.acknowledgement;

import external.connection.BasicMessageSender;
import external.connection.IConnection;

public class StandardAcknowledger extends Acknowledger {
	public StandardAcknowledger(IConnection conn) {
		super(conn, new MinimalAcknowledgementStrategy(), new BasicMessageSender());
	}
}
