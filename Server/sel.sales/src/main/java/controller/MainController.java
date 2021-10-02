package controller;

import controller.handler.AddDishHandler;
import controller.handler.AddKnownClientHandler;
import controller.handler.AddOrderHandler;
import controller.handler.AllowKnownClientHandler;
import controller.handler.BlockKnownClientHandler;
import controller.handler.ClientConnectedHandler;
import controller.handler.ClientDisconnectedHandler;
import controller.handler.ConfirmOrderHandler;
import controller.handler.DiscoverClientsHandler;
import controller.handler.AddDiscoveredClientHandler;
import controller.handler.EditDishHandler;
import controller.handler.RemoveDishHandler;
import controller.handler.RemoveKnownClientHandler;
import controller.handler.RemoveOrderHandler;
import controller.manager.ApplicationEventManager;
import controller.manager.IApplicationEventManager;
import model.IModel;

public class MainController extends Controller {

	public MainController(IModel model) {
		super(model);
	}

	@Override
	protected IApplicationEventManager initEventManager() {
		ApplicationEventManager bem = new ApplicationEventManager();
		bem.addApplicationEventToHandlerMapping(BusinessEvent.ADD_DISH, new AddDishHandler(this));
		bem.addApplicationEventToHandlerMapping(BusinessEvent.REMOVE_DISH, new RemoveDishHandler(this));
		bem.addApplicationEventToHandlerMapping(BusinessEvent.EDIT_DISH, new EditDishHandler(this));
		bem.addApplicationEventToHandlerMapping(BusinessEvent.ADD_ORDER, new AddOrderHandler(this));
		bem.addApplicationEventToHandlerMapping(BusinessEvent.CONFIRM_ORDER, new ConfirmOrderHandler(this));
		bem.addApplicationEventToHandlerMapping(BusinessEvent.REMOVE_ORDER, new RemoveOrderHandler(this));
		
		bem.addApplicationEventToHandlerMapping(BusinessEvent.DISCOVER_CLIENTS, new DiscoverClientsHandler(this));
		
		bem.addApplicationEventToHandlerMapping(StatusEvent.KNOWN_CLIENT_ADDED, new AddKnownClientHandler(this));
		bem.addApplicationEventToHandlerMapping(StatusEvent.KNOWN_CLIENT_REMOVED, new RemoveKnownClientHandler(this));
		bem.addApplicationEventToHandlerMapping(StatusEvent.KNOWN_CLIENT_ALLOWED, new AllowKnownClientHandler(this));
		bem.addApplicationEventToHandlerMapping(StatusEvent.KNOWN_CLIENT_BLOCKED, new BlockKnownClientHandler(this));
		
		bem.addApplicationEventToHandlerMapping(StatusEvent.CLIENT_DISCOVERED, new AddDiscoveredClientHandler(this));
		bem.addApplicationEventToHandlerMapping(StatusEvent.CLIENT_CONNECTED, new ClientConnectedHandler(this));
		bem.addApplicationEventToHandlerMapping(StatusEvent.CLIENT_DISCONNECTED, new ClientDisconnectedHandler(this));
		return bem;
	}
}
