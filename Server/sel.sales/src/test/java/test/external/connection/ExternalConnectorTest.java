package test.external.connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.IController;
import external.connection.DisconnectionListener;
import external.connection.IConnectionManager;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.device.IDevice;
import external.device.IDeviceManager;
import external.message.IMessage;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import model.connectivity.DeviceData;
import model.connectivity.IDeviceData;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyDevice;
import test.external.dummy.DummyDeviceDiscoveryStrategy;
import test.external.dummy.DummyDeviceManager;
import test.external.dummy.DummyExternalConnector;
import test.external.dummy.DummyServerController;
import test.external.dummy.DummyService;
import test.external.dummy.DummyServiceConnectionManager;

class ExternalConnectorTest {
	private long waitTime = 100;
	
	private DummyService service;
	private String serviceID;
	private String serviceName;
	
	private DummyExternalConnector connector;
	
	private IDeviceManager manager;
	private DummyDevice Device1;
	private String Device1Name;
	private String Device1Address;
	private DummyDevice Device2;
	private String Device2Name;
	private String Device2Address;
	private DummyDevice Device3;
	private String Device3Name;
	private String Device3Address;
	
	private IController controller;
	private boolean isOrderReceivedByController;
	
	private ExecutorService es;
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	@BeforeEach
	void prep() {
		serviceID = "serviceID";
		serviceName = "serviceName";
		es = Executors.newCachedThreadPool();
		manager = new DummyDeviceManager(es);
		Device1Name = "Device1Name";
		Device1Address = "Device1Address";
		Device2Name = "Device2Name";
		Device2Address = "Device2Address";
		Device3Name = "Device3Name";
		Device3Address = "Device3Address";
		Device1 = new DummyDevice(Device1Name, Device1Address);
		Device2 = new DummyDevice(Device2Name, Device2Address);
		Device3 = new DummyDevice(Device3Name, Device3Address);
		this.discoverDevices();
		manager.addDevice(Device1Address);
		manager.addDevice(Device2Address);
		manager.addDevice(Device3Address);
		manager.allowDevice(Device1Address);
		manager.allowDevice(Device2Address);
		manager.allowDevice(Device3Address);
		controller = initController();
		service = new DummyService(serviceID, serviceName, manager, controller, es);
		connector = new DummyExternalConnector(service, controller, es);
		isOrderReceivedByController = false;
	}
	
	private void discoverDevices() {
		DummyDeviceDiscoveryStrategy cds = new DummyDeviceDiscoveryStrategy();
		Collection<IDevice> cs = new ArrayList<IDevice>();
		cs.add(Device1);
		cs.add(Device2);
		cs.add(Device3);
		cds.setDiscoveredDevices(cs);
		this.manager.setDiscoveryStrategy(cds);
		this.manager.discoverDevices(()->{});
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	@AfterEach
	void cleanUp() {
		connector.close();
		isOrderReceivedByController = false;
		
		try {
			es.awaitTermination(waitTime/2, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private IController initController() {
		DummyServerController controller = new DummyServerController() {
//			@Override
//			public void addOrder(String serialisedOrder) {isOrderReceivedByController = true;}
		};
		return controller;
	}
	@Test
	void getConnectionTest() {
		connector.setCurrentConnectionObject(Device1);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime * 2);
		Assertions.assertEquals(Device1.getDeviceAddress(), connector.getConnection(Device1Address).getTargetDeviceAddress());
	}
	@Test
	void setConnectivitySettingsTest() {
		connector.setCurrentConnectionObject(Device1);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime * 2);
		Assertions.assertEquals(Device1.getDeviceAddress(), connector.getConnection(Device1Address).getTargetDeviceAddress());
		IConnectionManager cm = connector.getConnectionManagers().stream().filter(connMan -> connMan.getConnection().getTargetDeviceAddress().equals(Device1.getDeviceAddress())).findFirst().get();
		
		long minimalPPDelay = 1000;
		long ppTimeout = 2000;
		long sendTimeout = 5000;
		int resendLimit = 20;
		
		cm.setMinimalPingPongDelay(minimalPPDelay);
		cm.setPingPongResendLimit(resendLimit);
		cm.setPingPongTimeoutInMillis(ppTimeout);
		cm.setSendTimeoutInMillis(sendTimeout);
		
		Assertions.assertEquals(minimalPPDelay, cm.getMinimalPingPongDelay());
		Assertions.assertEquals(ppTimeout, cm.getPingPongTimeoutInMillis());
		Assertions.assertEquals(sendTimeout, cm.getSendTimeoutInMillis());
		Assertions.assertEquals(resendLimit, cm.getPingPongResendLimit());
	}
	@Test
	void acceptIncomingConnectionTest() {
		connector.setCurrentConnectionObject(Device1);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		connector.setCurrentConnectionObject(Device2);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		Assertions.assertEquals(Device2.getDeviceAddress(), connector.getConnection(Device2Address).getTargetDeviceAddress());
		Assertions.assertEquals(Device1.getDeviceAddress(), connector.getConnection(Device1Address).getTargetDeviceAddress());
	}
	@Test
	void acceptIncomingBlockedConnectionTest() {
		manager.blockDevice(Device2Address);
		Assertions.assertEquals(Device2.getDeviceAddress(),manager.getDevice(Device2Address).getDeviceAddress());
		Assertions.assertFalse(manager.isAllowedToConnect(Device2Address));
		GeneralTestUtilityClass.performWait(waitTime);
		connector.setCurrentConnectionObject(Device2);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertNull(connector.getConnection(Device2Address));
	}
	
	@Test
	void sendMessageToTest() {
		connector.setCurrentConnectionObject(Device1);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		connector.setCurrentConnectionObject(Device2);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		Assertions.assertEquals(Device2.getDeviceAddress(), connector.getConnection(Device2Address).getTargetDeviceAddress());
		Assertions.assertEquals(Device1.getDeviceAddress(), connector.getConnection(Device1Address).getTargetDeviceAddress());
		
		Collection<IConnectionManager> connectionManagers = connector.getConnectionManagers();
		connectionManagers.forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertFalse(sb.isBlocked());
		});
		IMessage m = new Message(null, null, null);
		
		String targetDeviceAddress = Device1Address;
		
		connector.sendMessageTo(targetDeviceAddress, m);
		
		GeneralTestUtilityClass.performWait(waitTime*2);
		
		// Make sure the right one gets it
		connectionManagers.stream().filter(cm -> cm.getConnection().getTargetDeviceAddress().equals(targetDeviceAddress)).forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertTrue(sb.isBlocked());
			
			DummyConnection conn = (DummyConnection) cm.getConnection();
			conn.fillInputBuffer(serialiser.serialise(m.getMinimalAcknowledgementMessage()));
		});
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		// Make sure others do not receive it
		connectionManagers.stream().filter(cm -> !cm.getConnection().getTargetDeviceAddress().equals(targetDeviceAddress)).forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertFalse(sb.isBlocked());
		});
		
		connectionManagers.stream().filter(cm -> cm.getConnection().getTargetDeviceAddress().equals(targetDeviceAddress)).forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			DummyConnection conn = (DummyConnection) cm.getConnection();
			
			ConnectionManagerTestUtilityClass.assertAckReadAndSentToSendBuffer(conn, sb, serialiser.serialise(m.getMinimalAcknowledgementMessage()), waitTime, 10000);
		});
	}
	
	@Test
	void broadcastMessageTest() {
		connector.setCurrentConnectionObject(Device1);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime);
		connector.setCurrentConnectionObject(Device2);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		Assertions.assertEquals(Device2.getDeviceAddress(), connector.getConnection(Device2Address).getTargetDeviceAddress());
		Assertions.assertEquals(Device1.getDeviceAddress(), connector.getConnection(Device1Address).getTargetDeviceAddress());
		
		Collection<IConnectionManager> connectionManagers = connector.getConnectionManagers();
		
		connectionManagers.forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertFalse(sb.isBlocked());
		});
		IMessage m = new Message(null, null, null);
		connector.broadcastMessage(m);
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		connectionManagers.forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertTrue(sb.isBlocked());
		});
		
		connectionManagers.forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			DummyConnection conn = (DummyConnection) cm.getConnection();
			ConnectionManagerTestUtilityClass.assertAckReadAndSentToSendBuffer(conn, sb, serialiser.serialise(m.getMinimalAcknowledgementMessage()), waitTime, 10000);
		});
	}
	
	private volatile boolean isDisconnected = false;
	
	@Test
	void disconnectionViaWaitingTest() {
		connector.setCurrentConnectionObject(Device1);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime);
		connector.setCurrentConnectionObject(Device2);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		Assertions.assertEquals(Device2.getDeviceAddress(), connector.getConnection(Device2Address).getTargetDeviceAddress());
		Assertions.assertEquals(Device1.getDeviceAddress(), connector.getConnection(Device1Address).getTargetDeviceAddress());
		
		Collection<IConnectionManager> connectionManagers = connector.getConnectionManagers();
		
		GeneralTestUtilityClass.performWait(DummyServiceConnectionManager.ESTIMATED_PP_TIMEOUT / 2);
		IConnectionManager cm = connectionManagers.stream().findAny().get();
		IPingPong pp = cm.getPingPong();
		pp.receiveResponse(new Message(MessageContext.PINGPONG, null, null));
		IConnectionManager cm2 = connectionManagers.stream().filter(man -> man != cm).findAny().get();
		DisconnectionListener l = new DisconnectionListener(null) {
			@Override
			public void fireApplicationEvent(IController controller) {
				isDisconnected = true;
			}
		};
		cm2.setDisconnectionListener(l);
		GeneralTestUtilityClass.performWait(DummyServiceConnectionManager.ESTIMATED_PP_TIMEOUT / 2);
		Assertions.assertTrue(isDisconnected);
	}
	
	@Test
	void disconnectionViaDataTest() {
		connector.setCurrentConnectionObject(Device1);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime);
		connector.setCurrentConnectionObject(Device2);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		connector.setCurrentConnectionObject(Device3);
//		connector.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		Assertions.assertEquals(Device3.getDeviceAddress(), connector.getConnection(Device3Address).getTargetDeviceAddress());
		Assertions.assertEquals(Device2.getDeviceAddress(), connector.getConnection(Device2Address).getTargetDeviceAddress());
		Assertions.assertEquals(Device1.getDeviceAddress(), connector.getConnection(Device1Address).getTargetDeviceAddress());
		
		Collection<IConnectionManager> connectionManagers = connector.getConnectionManagers();
		IConnectionManager cm = connectionManagers.stream().filter(man -> man.getConnection().getTargetDeviceAddress().equals(Device1Address)).findFirst().get();
		IConnectionManager cm2 = connectionManagers.stream().filter(man -> man.getConnection().getTargetDeviceAddress().equals(Device2Address)).findFirst().get();
		IConnectionManager cm3 = connectionManagers.stream().filter(man -> man.getConnection().getTargetDeviceAddress().equals(Device3Address)).findFirst().get();
		
		DisconnectionListener l = new DisconnectionListener(null) {
			@Override
			public void fireApplicationEvent(IController controller) {
				isDisconnected = true;
			}
		};
		
		connector.setDisconnectionListener(l);
		
		connector.receiveKnownDeviceData(new IDeviceData[] {
				new DeviceData(Device1Name, Device1Address, false, true),
				new DeviceData(Device2Name, Device2Address, true, false),
				new DeviceData(Device3Name, Device3Address, false, false)
		});
		
		Assertions.assertTrue(isDisconnected);
		Assertions.assertTrue(cm.isClosed());
		Assertions.assertTrue(cm2.isClosed());
		Assertions.assertTrue(cm3.isClosed());
	}
}
