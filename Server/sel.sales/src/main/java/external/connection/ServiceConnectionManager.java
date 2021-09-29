package external.connection;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.IController;
import external.client.IClientManager;
import external.message.IMessage;

public abstract class ServiceConnectionManager implements IServiceConnectionManager {

	protected ExecutorService pool = Executors.newCachedThreadPool();
	private Collection<IConnectionManager> connectionManagers = new CopyOnWriteArrayList<IConnectionManager>();
	private IClientManager manager;
	protected IController controller;

	protected ServiceConnectionManager(IClientManager manager, IController controller) {
		this.manager = manager;
		this.controller = controller;
	}

	private IConnectionManager getConnectionManager(String clientAddress) {
		Iterator<IConnectionManager> it = this.connectionManagers.iterator();
		while (it.hasNext()) {
			IConnectionManager current = it.next();
			if (current.getConnection().getTargetClientAddress().equals(clientAddress)) {
				return current;
			}
		}
		return null;
	}
	
	@Override
	public IConnection getConnection(String clientAddress) {
		IConnectionManager cm = this.getConnectionManager(clientAddress);
		if (cm != null) {
			return cm.getConnection();
		}
		return null;
	}
	/**
	 * @param connectionObject The object that contains the information about the connection (not the {@link IConnection} instance)
	 */
	protected abstract IConnection initConnection(Object connectionObject);
	
	protected abstract Object getConnectionObject();
	
	protected abstract IConnectionManager createConnectionManager(IConnection conn);
	
	protected boolean addConnection(IConnection conn) {
		if (this.isConnectionAllowed(conn.getTargetClientAddress())) {
			IConnectionManager connManager = this.createConnectionManager(conn);
			return this.connectionManagers.add(connManager);
		} else {
			try {
				conn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	protected Runnable initConnectionRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				IConnection conn = initConnection(getConnectionObject());
				if (!addConnection(conn)) {
					return;
				}
			}
		};
	}
	
	@Override
	public void acceptIncomingConnection() {
		this.pool.submit(this.initConnectionRunnable());
	}

	@Override
	public boolean isConnectionAllowed(String clientAddress) {
		return this.manager.isAllowedToConnect(clientAddress);
	}

	@Override
	public void close() {
		this.pool.shutdown();
		this.connectionManagers.forEach(cm -> cm.close());
		this.pool = null;
		this.manager = null;
	}
	@Override
	public void sendMessageTo(String clientAddress, IMessage message) {
		this.getConnectionManager(clientAddress).sendMessage(message);
	}
	@Override
	public void broadcastMessage(IMessage message) {
		this.connectionManagers.forEach(cm -> cm.sendMessage(message));
	}
}