package test.model.order;

import server.model.IServerModel;

public class ClientSimulant implements Runnable {
	private String serialisedOrder;
	private IServerModel model;
	
	public ClientSimulant(String serialisedOrder, IServerModel model) {
		this.serialisedOrder = serialisedOrder;
		this.model = model;
	}
	
	@Override
	public void run() {
		model.addUnconfirmedOrder(serialisedOrder);
	}
	
}