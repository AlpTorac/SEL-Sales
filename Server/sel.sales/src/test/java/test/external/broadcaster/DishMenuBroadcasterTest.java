package test.external.broadcaster;

import java.io.IOException;
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
import external.broadcaster.DishMenuBroadcaster;
import external.broadcaster.IBroadcaster;
import external.device.IDevice;
import external.device.IDeviceManager;
import external.connection.IConnectionManager;
import external.connection.outgoing.ISendBuffer;
import external.message.IMessage;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyDevice;
import test.external.dummy.DummyDeviceDiscoveryStrategy;
import test.external.dummy.DummyDeviceManager;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyConnectionManager;
import test.external.dummy.DummyServerController;
import test.external.dummy.DummyServiceConnectionManager;
import test.external.message.MessageTestUtilityClass;
@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuBroadcasterTest {
	private IBroadcaster broadcaster;
	
	private IDeviceManager DeviceManager;
	private IServerModel model;
	
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
	
	private IDeviceManager manager;
	private DummyDevice Device1;
	private String Device1Name;
	private String Device1Address;
	private DummyDevice Device2;
	private String Device2Name;
	private String Device2Address;
	
	private IController controller;
	private boolean isOrderReceivedByController;
	
	private ExecutorService es;
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	@BeforeEach
	void prep() {
		es = Executors.newCachedThreadPool();
		manager = new DummyDeviceManager(es);
		Device1Name = "Device1Name";
		Device1Address = "Device1Address";
		Device2Name = "Device2Name";
		Device2Address = "Device2Address";
		Device1 = new DummyDevice(Device1Name, Device1Address);
		Device2 = new DummyDevice(Device2Name, Device2Address);
		this.discoverDevices();
		manager.addDevice(Device1Address);
		manager.addDevice(Device2Address);
		manager.allowDevice(Device1Address);
		manager.allowDevice(Device2Address);
		initModel();
		controller = initController();
		serviceConnectionManager = new DummyServiceConnectionManager(manager, controller, es);
		isOrderReceivedByController = false;
	}
	
	private void discoverDevices() {
		DummyDeviceDiscoveryStrategy cds = new DummyDeviceDiscoveryStrategy();
		Collection<IDevice> cs = new ArrayList<IDevice>();
		cs.add(Device1);
		cs.add(Device2);
		cds.setDiscoveredDevices(cs);
		this.manager.setDiscoveryStrategy(cds);
		this.manager.discoverDevices(()->{});
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	private IController initController() {
		DummyServerController controller = new DummyServerController() {
//			@Override
//			public void addOrder(String serialisedOrder) {isOrderReceivedByController = true;}
		};
		return controller;
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
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
		model = new ServerModel();
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
	}
	
	@Test
	void messageContentTest() {
//		serviceConnectionManager.setCurrentConnectionObject(Device1);
//		GeneralTestUtilityClass.performWait(200);
//		serviceConnectionManager.setCurrentConnectionObject(Device2);
//		GeneralTestUtilityClass.performWait(200);
//
//		DummyConnection conn1 = (DummyConnection) serviceConnectionManager.getConnection(Device1.getDeviceAddress());
//		DummyConnection conn2 = (DummyConnection) serviceConnectionManager.getConnection(Device2.getDeviceAddress());
//		
//		Assertions.assertNotNull(conn1);
//		Assertions.assertNotNull(conn2);
//		broadcaster.broadcast();
		awaitedMessage = model.getDishMenuHelper().serialiseForExternal(model.getMenuData());
		broadcaster = new DishMenuBroadcaster(serviceConnectionManager, model);
		IMessage m = broadcaster.createMessage();
		MessageTestUtilityClass.assertMessageEquals(new Message(MessageContext.MENU, null, awaitedMessage), m);
	}
	
	@Test
	void broadcastTest() {
		serviceConnectionManager.setCurrentConnectionObject(Device1);
		GeneralTestUtilityClass.performWait(waitTime);
		serviceConnectionManager.setCurrentConnectionObject(Device2);
		GeneralTestUtilityClass.performWait(waitTime);

		DummyConnection conn1Target = new DummyConnection("someDeviceAddress1");
		DummyConnection conn2Target = new DummyConnection("someDeviceAddress2");
		
		DummyConnection conn1 = (DummyConnection) serviceConnectionManager.getConnection(Device1.getDeviceAddress());
		DummyConnection conn2 = (DummyConnection) serviceConnectionManager.getConnection(Device2.getDeviceAddress());
		
		Assertions.assertNotNull(conn1);
		Assertions.assertNotNull(conn2);
		
		conn1.setInputTarget(conn1Target.getInputStream());
		conn2.setInputTarget(conn2Target.getInputStream());
		
		Collection<IConnectionManager> connectionManager = serviceConnectionManager.getConnectionManagers();
		
		connectionManager.stream().map(cm -> (DummyConnectionManager) cm)
		.forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertFalse(sb.isBlocked());
		});
		
		broadcaster = new DishMenuBroadcaster(serviceConnectionManager, model);
		broadcaster.broadcast();
		IMessage m = broadcaster.createMessage();
		String serialisedM = serialiser.serialise(m);
		GeneralTestUtilityClass.performWait(waitTime);
//		BufferUtilityClass.assertInputStoredEquals(conn1.getInputStream(), serialisedM.getBytes());
//		BufferUtilityClass.assertInputStoredEquals(conn2.getInputStream(), serialisedM.getBytes());
		connectionManager.stream().map(cm -> (DummyConnectionManager) cm)
		.forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertTrue(sb.isBlocked());
		});
		
//		BufferUtilityClass.assertInputStoredEquals(conn1Target.getInputStream(), serialisedM.getBytes());
//		BufferUtilityClass.assertInputStoredEquals(conn2Target.getInputStream(), serialisedM.getBytes());
		
		try {
			conn1Target.close();
			conn2Target.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
