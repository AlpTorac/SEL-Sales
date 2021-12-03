package test.external.buffer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import external.connection.outgoing.FIFOBuffer;
import external.connection.outgoing.ISendBufferDataContainer;
import external.message.IMessage;
import external.message.Message;
//@Execution(value = ExecutionMode.SAME_THREAD)
class FIFOBufferTest {
	private ISendBufferDataContainer buffer;
	
	@BeforeEach
	void prep() {
		buffer = new FIFOBuffer();
	}
	
	@AfterEach
	void cleanUp() {
		buffer.clear();
	}
	
	@Test
	void isEmptyTest() {
		Assertions.assertTrue(buffer.isEmpty());
		buffer.add(this.generateNullMessage());
		Assertions.assertFalse(buffer.isEmpty());
	}
	
	@Test
	void addTest() {
		IMessage m1 = this.generateNullMessage();
		buffer.add(m1);
		Assertions.assertEquals(m1, buffer.getMessageInLine());
		IMessage m2 = this.generateNullMessage();
		buffer.add(m2);
		Assertions.assertEquals(m1, buffer.getMessageInLine());
		IMessage m3 = this.generateNullMessage();
		buffer.add(m3);
		Assertions.assertEquals(m1, buffer.getMessageInLine());
	}
	
	@Test
	void removeTest() {
		IMessage m1 = this.generateNullMessage();
		int seqNum1 = 1;
		m1.setSequenceNumber(seqNum1);
		buffer.add(m1);
		Assertions.assertEquals(buffer.size(), 1);
		
		IMessage m2 = this.generateNullMessage();
		int seqNum2 = 2;
		m2.setSequenceNumber(seqNum2);
		buffer.add(m2);
		Assertions.assertEquals(buffer.size(), 2);
		
		IMessage m3 = this.generateNullMessage();
		int seqNum3 = 3;
		m3.setSequenceNumber(seqNum3);
		buffer.add(m3);
		Assertions.assertEquals(buffer.size(), 3);
		
		IMessage m4 = this.generateNullMessage();
		int seqNum4 = 4;
		m4.setSequenceNumber(seqNum4);
		buffer.add(m4);
		Assertions.assertEquals(buffer.size(), 4);
		
		int nonExistentSeqNum = 999;
		
		Assertions.assertTrue(buffer.remove(seqNum2));
		Assertions.assertEquals(buffer.size(), 3);
		Assertions.assertTrue(buffer.remove(m3));
		Assertions.assertEquals(buffer.size(), 2);
		Assertions.assertFalse(buffer.remove(nonExistentSeqNum));
		Assertions.assertEquals(buffer.size(), 2);
		Assertions.assertFalse(buffer.remove(this.generateNullMessage()));
		Assertions.assertEquals(buffer.size(), 2);
		Assertions.assertTrue(buffer.remove(seqNum1));
		Assertions.assertEquals(buffer.size(), 1);
		Assertions.assertTrue(buffer.remove(seqNum4));
		Assertions.assertEquals(buffer.size(), 0);
		Assertions.assertFalse(buffer.remove(seqNum1));
		Assertions.assertTrue(buffer.isEmpty());
	}
	
	@Test
	void getMessageInLineTest() {
		IMessage m1 = this.generateNullMessage();
		m1.setSequenceNumber(0);
		buffer.add(m1);
		Assertions.assertEquals(buffer.size(), 1);
		IMessage m2 = this.generateNullMessage();
		m2.setSequenceNumber(1);
		buffer.add(m2);
		Assertions.assertEquals(buffer.size(), 2);
		IMessage m3 = this.generateNullMessage();
		m3.setSequenceNumber(2);
		buffer.add(m3);
		Assertions.assertEquals(buffer.size(), 3);
		IMessage m4 = this.generateNullMessage();
		m4.setSequenceNumber(3);
		buffer.add(m4);
		Assertions.assertEquals(buffer.size(), 4);
		
		Assertions.assertEquals(m1, buffer.getMessageInLine());
		Assertions.assertEquals(true, buffer.remove(m1));
		Assertions.assertEquals(buffer.size(), 3);
		
		Assertions.assertEquals(m2, buffer.getMessageInLine());
		Assertions.assertEquals(true, buffer.remove(m2));
		Assertions.assertEquals(buffer.size(), 2);
		
		Assertions.assertEquals(m3, buffer.getMessageInLine());
		Assertions.assertEquals(true, buffer.remove(m3));
		Assertions.assertEquals(buffer.size(), 1);
		
		Assertions.assertEquals(m4, buffer.getMessageInLine());
		Assertions.assertEquals(true, buffer.remove(m4));
		Assertions.assertEquals(buffer.size(), 0);
		Assertions.assertTrue(buffer.isEmpty());
	}
	
	@Test
	void sizeTest() {
		Assertions.assertEquals(buffer.size(), 0);
		buffer.add(this.generateNullMessage());
		Assertions.assertEquals(buffer.size(), 1);
		buffer.add(this.generateNullMessage());
		Assertions.assertEquals(buffer.size(), 2);
		buffer.add(this.generateNullMessage());
		Assertions.assertEquals(buffer.size(), 3);
	}
	
	@Test
	void clearTest() {
		buffer.add(this.generateNullMessage());
		buffer.add(this.generateNullMessage());
		buffer.add(this.generateNullMessage());
		buffer.add(this.generateNullMessage());
		buffer.add(this.generateNullMessage());
		Assertions.assertEquals(buffer.size(), 5);
		buffer.clear();
		Assertions.assertTrue(buffer.isEmpty());
	}
	
	private IMessage generateNullMessage() {
		return new Message(null, null, null);
	}
}
