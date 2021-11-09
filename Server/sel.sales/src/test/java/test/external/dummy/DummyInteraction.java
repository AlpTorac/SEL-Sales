package test.external.dummy;

import java.util.concurrent.ExecutorService;
import controller.IController;
import external.connection.DisconnectionListener;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.message.IMessage;

public class DummyInteraction {
	protected IController controllerServer;
	protected IController controllerDevice;
	
	protected ExecutorService esServer;
	protected ExecutorService esDevice;
	
	protected long minimalPingPongDelay;
	protected long pingPongTimeout;
	protected long sendTimeout;
	protected int resendLimit;
	
	protected String DeviceAddress;
	protected String DeviceName;
	
	protected String serverName = "serverName";
	protected String serverAddress = "serverAddress";
	
	protected DummyDevice device;
	protected DummyDevice server;
	
	protected DummyConnection serverDeviceConn;
	protected DummyConnection DeviceServerConn;
	
	protected DummyConnectionManager serverDeviceConnManager;
	protected DummyConnectionManager DeviceServerConnManager;
	
	protected DisconnectionListener dlServer;
	protected DisconnectionListener dlDevice;
	
	protected ISendBuffer serverSB;
	protected ISendBuffer DeviceSB;
	
	protected IPingPong serverPP;
	protected IPingPong DevicePP;
	
	public DummyInteraction(ExecutorService esServer, ExecutorService esDevice, String DeviceName, String DeviceAddress, long pingPongTimeout,
			long sendTimeout, int resendLimit, long minimalPingPongDelay) {
		this.esServer = esServer;
		this.esDevice = esDevice;
		this.minimalPingPongDelay = minimalPingPongDelay;
		this.pingPongTimeout = pingPongTimeout;
		this.sendTimeout = sendTimeout;
		this.resendLimit = resendLimit;
		this.DeviceAddress = DeviceAddress;
		this.DeviceName = DeviceName;
		this.controllerDevice = this.initDeviceController();
		this.controllerServer = this.initServerController();
		this.device = this.initDevice();
		this.server = this.initServer();
		this.serverDeviceConn = this.initServerDeviceConnection();
		this.DeviceServerConn = this.initDeviceServerConnection();
		this.bindConnectionStreams();
		this.serverDeviceConnManager = this.initServerDeviceConnectionManager();
		this.DeviceServerConnManager = this.initDeviceServerConnectionManager();
		this.dlServer = this.initServerDisconListener();
		this.serverDeviceConnManager.setDisconnectionListener(this.dlServer);
		this.dlDevice = this.initDeviceDisconListener();
		this.DeviceServerConnManager.setDisconnectionListener(this.dlDevice);
	}
	
	protected DummyDevice initDevice() {
		return new DummyDevice(this.DeviceName, this.DeviceAddress);
	}
	
	protected DummyDevice initServer() {
		return new DummyDevice(this.serverName, this.serverAddress);
	}
	
	protected DummyConnection initServerDeviceConnection() {
		return new DummyConnection(this.server.getDeviceAddress());
	}
	protected DummyConnection initDeviceServerConnection() {
		return new DummyConnection(this.device.getDeviceAddress());
	}
	
	protected void bindConnectionStreams() {
		this.DeviceServerConn.setInputTarget(this.serverDeviceConn.getInputStream());
		this.serverDeviceConn.setInputTarget(this.DeviceServerConn.getInputStream());
	}
	
	public void resetServerInputBuffer() {
		this.serverDeviceConn.resetInputStream();
		this.bindConnectionStreams();
	}
	public void resetDeviceInputBuffer() {
		this.DeviceServerConn.resetInputStream();
		this.bindConnectionStreams();
	}
	public void resetServerOutputBuffer() {
		this.serverDeviceConn.resetOutputStream();
		this.bindConnectionStreams();
	}
	public void resetDeviceOutputBuffer() {
		this.DeviceServerConn.resetOutputStream();
		this.bindConnectionStreams();
	}
	
	protected IController initServerController() {
		return new DummyServerController();
	}
	
	protected IController initDeviceController() {
		return new DummyServerController();
	}
	
	protected DisconnectionListener initServerDisconListener() {
		return new DisconnectionListener(this.controllerServer);
	}
	
	protected DisconnectionListener initDeviceDisconListener() {
		return new DisconnectionListener(this.controllerDevice);
	}
	
	protected DummyConnectionManager initServerDeviceConnectionManager() {
		DummyConnectionManager cm = new DummyConnectionManager(this.controllerServer, this.serverDeviceConn, this.esServer, this.pingPongTimeout, this.sendTimeout, this.resendLimit, this.minimalPingPongDelay);
		this.serverSB = cm.getSendBuffer();
		this.serverPP = cm.getPingPong();
		return cm;
	}
	protected DummyConnectionManager initDeviceServerConnectionManager() {
		DummyConnectionManager cm = new DummyConnectionManager(this.controllerDevice, this.DeviceServerConn, this.esDevice, this.pingPongTimeout, this.sendTimeout, this.resendLimit, this.minimalPingPongDelay);
		this.DeviceSB = cm.getSendBuffer();
		this.DevicePP = cm.getPingPong();
		return cm;
	}
	
	public void close() {
		this.serverDeviceConnManager.close();
		this.DeviceServerConnManager.close();
	}
	
	public int getPingPongSuccessfulConsecutiveCycleCount() {
		return Math.max(this.serverDeviceConnManager.getPingPongSuccessfulConsecutiveCycleCount(), this.DeviceServerConnManager.getPingPongSuccessfulConsecutiveCycleCount());
	}
	public int getPingPongSuccessfulCycleCount() {
		return Math.max(this.serverDeviceConnManager.getPingPongSuccessfulCycleCount(), this.DeviceServerConnManager.getPingPongSuccessfulCycleCount());
	}
	public int getSendBufferSuccessfulConsecutiveCycleCount() {
		return Math.max(this.serverDeviceConnManager.getSendBufferSuccessfulConsecutiveCycleCount(), this.DeviceServerConnManager.getSendBufferSuccessfulConsecutiveCycleCount());
	}
	public int getSendBufferSuccessfulCycleCount() {
		return Math.max(this.serverDeviceConnManager.getSendBufferSuccessfulCycleCount(), this.DeviceServerConnManager.getSendBufferSuccessfulCycleCount());
	}
	public int getServerSendBufferSuccessfulConsecutiveCycleCount() {
		return this.serverDeviceConnManager.getSendBufferSuccessfulConsecutiveCycleCount();
	}
	public int getServerSendBufferSuccessfulCycleCount() {
		return this.serverDeviceConnManager.getSendBufferSuccessfulCycleCount();
	}
	public int getDeviceSendBufferSuccessfulConsecutiveCycleCount() {
		return this.DeviceServerConnManager.getSendBufferSuccessfulConsecutiveCycleCount();
	}
	public int getDeviceSendBufferSuccessfulCycleCount() {
		return this.DeviceServerConnManager.getSendBufferSuccessfulCycleCount();
	}
	
	public void messageToDevice(IMessage message) {
		this.serverDeviceConnManager.sendMessage(message);
	}
	public void messageToServer(IMessage message) {
		this.DeviceServerConnManager.sendMessage(message);
	}
	
	public void serverWaitTillAcknowledgement() {
		while (this.serverDeviceConnManager.getSendBuffer().isBlocked()) {
			
		}
	}
	
	public void DeviceWaitTillAcknowledgement() {
		while (this.DeviceServerConnManager.getSendBuffer().isBlocked()) {
			
		}
	}
}
