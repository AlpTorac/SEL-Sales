package test.model.order;

import model.order.OrderData;
import server.model.IServerModel;

public class ClientSimulant implements Runnable {
	private OrderData order;
	private String serialisedOrder;
	private IServerModel model;
	
	public ClientSimulant(String serialisedOrder, IServerModel model) {
		this.serialisedOrder = serialisedOrder;
		this.model = model;
	}
	
	public ClientSimulant(OrderData order, IServerModel model) {
		this.order = order;
		this.model = model;
	}
	
	@Override
	public void run() {
		if (serialisedOrder != null) {
			model.addOrder(serialisedOrder);
		} else {
			model.addOrder(order);
		}
	}
	
}