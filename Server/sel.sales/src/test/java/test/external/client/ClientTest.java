package test.external.client;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import external.client.IClient;
import test.external.dummy.DummyClient;
@Execution(value = ExecutionMode.SAME_THREAD)
class ClientTest {
	private String client1Name;
	private String client1Address;
	private String client2Name;
	private String client2Address;
	private IClient client1;
	private IClient client2;
	
	@BeforeEach
	void prep() {
		this.client1Name = "client1";
		this.client1Address = "client1Address";
		this.client1Name = "client2";
		this.client1Address = "client2Address";
		this.client1 = new DummyClient(this.client1Name, this.client1Address);
		this.client2 = new DummyClient(this.client2Name, this.client2Address);
	}
	
	@Test
	void getClientNameTest() {
		Assertions.assertEquals(this.client1Name, client1.getClientName());
	}

	@Test
	void getClientAddressTest() {
		Assertions.assertEquals(this.client1Address, client1.getClientAddress());
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	void equalsTest() {
		Assertions.assertFalse(client1.equals(null));
		Assertions.assertFalse(client1.equals(Integer.valueOf(12)));
		Assertions.assertFalse(client1.equals(client2));
		
		Assertions.assertTrue(client1.equals(new DummyClient(client1Name, client1Address)));
		Assertions.assertTrue(client1.equals(client1));
		Assertions.assertTrue(client1.equals(new DummyClient(client2Name, client1Address)));
	}
}
