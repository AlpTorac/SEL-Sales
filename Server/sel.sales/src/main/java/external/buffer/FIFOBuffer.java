package external.buffer;

import java.util.LinkedList;
import java.util.Queue;

import external.message.IMessage;

public class FIFOBuffer implements ISendBufferDataContainer {

	private Queue<IMessage> queue;
	
	public FIFOBuffer() {
		this.queue = new LinkedList<IMessage>();
	}
	
	@Override
	public int size() {
		return this.queue.size();
	}
	
	@Override
	public boolean add(IMessage message) {
		return this.queue.add(message);
	}

	@Override
	public boolean remove(int sequenceNumber) {
		return this.queue.removeIf(m -> m.getSequenceNumber() == sequenceNumber);
	}

	@Override
	public IMessage getMessageInLine() {
		return this.queue.peek();
	}

	@Override
	public IMessage removeMessageInLine() {
		return this.queue.remove();
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public void clear() {
		this.queue.clear();
	}

	@Override
	public boolean remove(IMessage message) {
		return this.queue.remove(message);
	}

}
