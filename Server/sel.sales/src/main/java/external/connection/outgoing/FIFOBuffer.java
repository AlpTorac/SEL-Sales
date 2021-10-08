package external.connection.outgoing;

import java.util.LinkedList;
import external.message.IMessage;

public class FIFOBuffer implements ISendBufferDataContainer {

	private LinkedList<IMessage> list;
	
	public FIFOBuffer() {
		this.list = new LinkedList<IMessage>();
	}
	
	@Override
	public int size() {
		return this.list.size();
	}
	
	@Override
	public boolean add(IMessage message) {
		return this.list.add(message);
	}

	@Override
	public boolean remove(int sequenceNumber) {
		return this.list.removeIf(m -> m.getSequenceNumber() == sequenceNumber);
	}

	@Override
	public IMessage getMessageInLine() {
		return this.list.getFirst();
	}

	@Override
	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	@Override
	public void clear() {
		this.list.clear();
	}

	@Override
	public boolean remove(IMessage message) {
		return this.list.remove(message);
	}

}
