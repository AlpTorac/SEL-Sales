package external.acknowledgement;

import external.connection.IConnection;
import external.connection.outgoing.BasicMessageSender;

public class StandardAcknowledger extends Acknowledger {
	public StandardAcknowledger(IConnection conn) {
		super(conn, new MinimalAcknowledgementStrategy(), new BasicMessageSender());
	}
}
