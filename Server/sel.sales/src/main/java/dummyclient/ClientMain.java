package dummyclient;

import external.message.IMessage;
import external.message.Message;
import external.message.MessageContext;

public class ClientMain {
	private static ClientModel model;
	private static ClientController controller;
	private static ClientView view;
	private static ClientExternal external;
	
	public static void main(String[] args) {
		model = new ClientModel();
		controller = new ClientController(model);
		view = new ClientView(controller, model);
		view.startUp();
		external = new ClientExternal(controller, model);
		external.connectToService();
		final Object lock = new Object();
		while (true) {
			synchronized (lock) {
				IMessage order = new Message(MessageContext.ORDER, null, "order10-20210809000000111-1-1:item3,5;");
				external.sendMessage(order);
				System.out.println("Message Sent");
				external.getConnManager().getSendBuffer().receiveAcknowledgement(order.getMinimalAcknowledgementMessage());
				try {
					lock.wait(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		external.close(order.getMinimalAcknowledgementMessage());
	}
}
