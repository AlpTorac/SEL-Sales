package test.model.order;

import model.IModel;

public class ClientSimulant implements Runnable {
	private String serialisedOrder;
	private IModel model;
	
	public ClientSimulant(String serialisedOrder, IModel model) {
		this.serialisedOrder = serialisedOrder;
		this.model = model;
	}
	
	@Override
	public void run() {
		model.addOrder(serialisedOrder);
	}
	
}