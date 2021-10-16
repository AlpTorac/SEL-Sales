package test.external.broadcaster;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import controller.IController;
import controller.MainController;
import external.broadcaster.DishMenuBroadcaster;
import external.broadcaster.IBroadcaster;
import external.client.IClient;
import external.client.IClientManager;
import external.connection.IServiceConnectionManager;
import external.message.IMessage;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import model.IModel;
import model.Model;
import model.dish.serialise.ExternalDishMenuSerialiser;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.dish.serialise.IDishMenuSerialiser;
import test.GeneralTestUtilityClass;
import test.external.buffer.BufferUtilityClass;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyClientDiscoveryStrategy;
import test.external.dummy.DummyClientManager;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyController;
import test.external.dummy.DummyServiceConnectionManager;
import test.external.message.MessageTestUtilityClass;
@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuBroadcasterTest {
	private IBroadcaster broadcaster;
	
	private IClientManager clientManager;
	private IModel model;
	private IDishMenuItemSerialiser itemSerialiser;
	
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private BigDecimal i1Disc = BigDecimal.valueOf(0);
	private String i1id = "item1";
	
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	private String i2id = "item2";
	
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private BigDecimal i3Disc = BigDecimal.valueOf(1);
	private String i3id = "item3";
	
	private String awaitedMessage;
	
	private long waitTime = 100;
	private DummyServiceConnectionManager serviceConnectionManager;
	
	private IClientManager manager;
	private DummyClient client1;
	private String client1Name;
	private String client1Address;
	private DummyClient client2;
	private String client2Name;
	private String client2Address;
	
	private IController controller;
	private boolean isOrderReceivedByController;
	
	private ExecutorService es;
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	@BeforeEach
	void prep() {
		es = Executors.newCachedThreadPool();
		manager = new DummyClientManager(es);
		client1Name = "client1Name";
		client1Address = "client1Address";
		client2Name = "client2Name";
		client2Address = "client2Address";
		client1 = new DummyClient(client1Name, client1Address);
		client2 = new DummyClient(client2Name, client2Address);
		this.discoverClients();
		manager.addClient(client1Address);
		manager.addClient(client2Address);
		manager.allowClient(client1Address);
		manager.allowClient(client2Address);
		initModel();
		controller = initController();
		serviceConnectionManager = new DummyServiceConnectionManager(manager, controller, es);
		isOrderReceivedByController = false;
	}
	
	private void discoverClients() {
		DummyClientDiscoveryStrategy cds = new DummyClientDiscoveryStrategy();
		Collection<IClient> cs = new ArrayList<IClient>();
		cs.add(client1);
		cs.add(client2);
		cds.setDiscoveredClients(cs);
		this.manager.setDiscoveryStrategy(cds);
		this.manager.discoverClients();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	private IController initController() {
		DummyController controller = new DummyController() {
//			@Override
//			public void addOrder(String serialisedOrder) {isOrderReceivedByController = true;}
		};
		return controller;
	}
	
	@AfterEach
	void cleanUp() {
		serviceConnectionManager.close();
		isOrderReceivedByController = false;
		
		try {
			es.awaitTermination(waitTime/2, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initModel() {
		model = new Model();
		itemSerialiser = model.getDishMenuItemSerialiser();
		model.addMenuItem(itemSerialiser.serialise(i1Name, i1id, i1PorSize, i1ProCost, i1Price, i1Disc));
		model.addMenuItem(itemSerialiser.serialise(i2Name, i2id, i2PorSize, i2ProCost, i2Price, i2Disc));
		model.addMenuItem(itemSerialiser.serialise(i3Name, i3id, i3PorSize, i3ProCost, i3Price, i3Disc));
	}
	
	@Test
	void broadcastTest() {
//		serviceConnectionManager.setCurrentConnectionObject(client1);
//		GeneralTestUtilityClass.performWait(200);
//		serviceConnectionManager.setCurrentConnectionObject(client2);
//		GeneralTestUtilityClass.performWait(200);
//
//		DummyConnection conn1 = (DummyConnection) serviceConnectionManager.getConnection(client1.getClientAddress());
//		DummyConnection conn2 = (DummyConnection) serviceConnectionManager.getConnection(client2.getClientAddress());
//		
//		Assertions.assertNotNull(conn1);
//		Assertions.assertNotNull(conn2);
//		broadcaster.broadcast();
		awaitedMessage = model.getExternalDishMenuSerialiser().serialise(model.getMenuData());
		broadcaster = new DishMenuBroadcaster(serviceConnectionManager, model);
		IMessage m = broadcaster.createMessage();
		MessageTestUtilityClass.assertMessageEquals(new Message(MessageContext.MENU, null, awaitedMessage), m);
	}
}
