package external.acknowledgement;

import java.io.OutputStream;

import external.connection.BasicMessageSender;

public class StandardAcknowledger extends Acknowledger {
	public StandardAcknowledger(OutputStream os) {
		super(os, new MinimalAcknowledgementStrategy(), new BasicMessageSender());
	}
}
