package external.message;

public class Message implements IMessage {
	private volatile int sequenceNumber;
	private MessageContext context;
	private MessageFlag[] flags;
	private String serialisedData;
	
	public Message(MessageContext context, MessageFlag[] flags, String serialisedData) {
		this.context = context;
		this.flags = flags;
		this.serialisedData = serialisedData;
	}
	
	public Message(int sequenceNumber, MessageContext context, MessageFlag[] flags, String serialisedData) {
		this.sequenceNumber = sequenceNumber;
		this.context = context;
		this.flags = flags;
		this.serialisedData = serialisedData;
	}
	
	@Override
	public int getSequenceNumber() {
		return this.sequenceNumber;
	}
	
	@Override
	public MessageContext getMessageContext() {
		return this.context;
	}
	
	@Override
	public String getSerialisedData() {
		return this.serialisedData;
	}

	@Override
	public MessageFlag[] getMessageFlags() {
		return this.flags;
	}

	@Override
	public IMessage getMinimalAcknowledgementMessage() {
		if (this.isAcknowledgementMessage()) {
			return null;
		}
		IMessage ackMessage = new Message(this.sequenceNumber, this.context, new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT}, this.serialisedData);
		return ackMessage;
	}

	@Override
	public boolean isAcknowledgementMessage() {
		return this.hasFlag(MessageFlag.ACKNOWLEDGEMENT);
	}

	@Override
	public boolean hasFlag(MessageFlag f) {
		if (this.flags == null) {
			return false;
		}
		for (MessageFlag flag : this.flags) {
			if (flag == f) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasContext(MessageContext c) {
		if (this.context == c) {
			return true;
		}
		return false;
	}

	@Override
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public boolean isPingPongMessage() {
		return this.hasContext(MessageContext.PINGPONG);
	}
	
	@Override
	public Message clone() {
		MessageContext c = null;
		if (this.context != null) {
			c = MessageContext.stringToMessageContext(this.context.toString());
		}
		MessageFlag[] fs = null;
		if (this.flags != null) {
			fs = new MessageFlag[this.flags.length];
			for (int i = 0; i < fs.length; i++) {
				fs[i] = MessageFlag.stringToMessageFlag(this.flags[i].toString());
			}
		}
		return new Message(this.sequenceNumber, c, fs, this.serialisedData);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IMessage)) {
			return false;
		}
		IMessage castedO = (IMessage) o;
		MessageFlag[] flags = castedO.getMessageFlags();
		if (this.getMessageFlags() == null ^ flags == null) {
			return false;
		}
		if (flags != null) {
			for (MessageFlag f : flags) {
				if (f != null && !this.hasFlag(f)) {
					return false;
				}
			}
		}
		if (this.getSerialisedData() == null ^ castedO.getSerialisedData() == null) {
			return false;
		}
		if (!(this.getSerialisedData() == castedO.getSerialisedData() ||
				this.getSerialisedData().equals(castedO.getSerialisedData()))) {
			return false;
		}
		return this.getSequenceNumber() == castedO.getSequenceNumber() &&
				this.hasContext(castedO.getMessageContext());
	}
}
