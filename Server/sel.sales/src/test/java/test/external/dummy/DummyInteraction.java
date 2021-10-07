package test.external.dummy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import controller.IController;
import external.connection.DisconnectionListener;
import external.connection.IConnectionManager;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;

public class DummyInteraction {
	protected IController controllerServer;
	protected IController controllerClient;
	
	protected ExecutorService esServer = Executors.newCachedThreadPool();
	protected ExecutorService esClient = Executors.newCachedThreadPool();
	
	protected long minimalPingPongDelay;
	protected long pingPongTimeout;
	protected long sendTimeout;
	protected int resendLimit;
	
	protected String clientAddress;
	protected String clientName;
	
	protected String serverName = "serverName";
	protected String serverAddress = "serverAddress";
	
	protected DummyClient client;
	protected DummyClient server;
	
	protected DummyConnection serverClientConn;
	protected DummyConnection clientServerConn;
	
	protected IConnectionManager serverClientConnManager;
	protected IConnectionManager clientServerConnManager;
	
	protected DisconnectionListener dlServer;
	protected DisconnectionListener dlClient;
	
	private ISendBuffer serverSB;
	private ISendBuffer clientSB;
	
	private IPingPong serverPP;
	private IPingPong clientPP;
	
	public DummyInteraction(String clientName, String clientAddress, long pingPongTimeout,
			long sendTimeout, int resendLimit) {
		this.pingPongTimeout = pingPongTimeout;
		this.sendTimeout = sendTimeout;
		this.resendLimit = resendLimit;
		this.clientAddress = clientAddress;
		this.clientName = clientName;
		this.controllerClient = this.initClientController();
		this.controllerServer = this.initServerController();
		this.client = this.initClient();
		this.server = this.initServer();
		this.serverClientConn = this.initServerClientConnection();
		this.clientServerConn = this.initClientServerConnection();
		this.bindConnectionStreams();
		this.serverClientConnManager = this.initServerClientConnectionManager();
		this.clientServerConnManager = this.initClientServerConnectionManager();
		this.dlServer = this.initServerDisconListener();
		this.serverClientConnManager.setDisconnectionListener(this.dlServer);
		this.dlClient = this.initClientDisconListener();
		this.clientServerConnManager.setDisconnectionListener(this.dlClient);
	}
	
	protected DummyClient initClient() {
		return new DummyClient(this.clientName, this.clientAddress);
	}
	
	protected DummyClient initServer() {
		return new DummyClient(this.serverName, this.serverAddress);
	}
	
	protected DummyConnection initServerClientConnection() {
		return new DummyConnection(this.server.getClientAddress());
	}
	protected DummyConnection initClientServerConnection() {
		return new DummyConnection(this.client.getClientAddress());
	}
	
	protected void bindConnectionStreams() {
		this.clientServerConn.setInputTarget(this.serverClientConn.getInputStream());
		this.serverClientConn.setInputTarget(this.clientServerConn.getInputStream());
	}
	
	public void resetServerInputBuffer() {
		this.serverClientConn.resetInputStream();
		this.bindConnectionStreams();
	}
	public void resetClientInputBuffer() {
		this.clientServerConn.resetInputStream();
		this.bindConnectionStreams();
	}
	public void resetServerOutputBuffer() {
		this.serverClientConn.resetOutputStream();
		this.bindConnectionStreams();
	}
	public void resetClientOutputBuffer() {
		this.clientServerConn.resetOutputStream();
		this.bindConnectionStreams();
	}
	
	protected IController initServerController() {
		return new DummyController();
	}
	
	protected IController initClientController() {
		return new DummyController();
	}
	
	protected DisconnectionListener initServerDisconListener() {
		return new DisconnectionListener(this.controllerServer);
	}
	
	protected DisconnectionListener initClientDisconListener() {
		return new DisconnectionListener(this.controllerClient);
	}
	
	protected DummyConnectionManager initServerClientConnectionManager() {
		DummyConnectionManager cm = new DummyConnectionManager(this.controllerServer, this.serverClientConn, this.esServer, this.pingPongTimeout, this.sendTimeout, this.resendLimit, this.minimalPingPongDelay);
		this.serverSB = cm.getSendBuffer();
		this.serverPP = cm.getPingPong();
		return cm;
	}
	protected DummyConnectionManager initClientServerConnectionManager() {
		DummyConnectionManager cm = new DummyConnectionManager(this.controllerClient, this.clientServerConn, this.esClient, this.pingPongTimeout, this.sendTimeout, this.resendLimit, this.minimalPingPongDelay);
		this.clientSB = cm.getSendBuffer();
		this.clientPP = cm.getPingPong();
		return cm;
	}
	
	public void close() {
		this.serverClientConnManager.close();
		this.clientServerConnManager.close();
		try {
			this.esServer.awaitTermination(1000, TimeUnit.MILLISECONDS);
			this.esClient.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
