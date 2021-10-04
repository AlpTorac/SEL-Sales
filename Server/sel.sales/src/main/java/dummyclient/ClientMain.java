package dummyclient;

import external.message.Message;
import external.message.MessageContext;

public class ClientMain {
	private static ClientModel model;
	private static ClientController controller;
	private static ClientView view;
	private static ClientExternal external;
	
	public void run() {
		model = new ClientModel();
		controller = new ClientController(model);
		view = new ClientView(controller, model);
		view.startUp();
		external = new ClientExternal(controller, model);
		external.connectToService();
		while (!external.isConnected()) {
			
		}
		external.sendMessage(new Message(MessageContext.ORDER, null, "order10-20210809000000111-1-1:item3,5;"));
	}
}
