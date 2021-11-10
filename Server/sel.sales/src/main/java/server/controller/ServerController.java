package server.controller;

import controller.Controller;
import controller.manager.IApplicationEventManager;
import server.controller.handler.AddDishHandler;
import server.controller.handler.ConfirmAllOrdersHandler;
import server.controller.handler.ConfirmOrderHandler;
import server.controller.handler.EditDishHandler;
import server.controller.handler.LoadDishMenuHandler;
import server.controller.handler.OrderConfirmModelChangedHandler;
import server.controller.handler.RemoveDishHandler;
import server.controller.handler.WriteDishMenuHandler;
import server.controller.handler.WriteOrdersHandler;
import server.model.IServerModel;

public abstract class ServerController extends Controller implements IServerController {
	public ServerController(IServerModel model) {
		super(model);
	}
	
	@Override
	public IServerModel getModel() {
		return (IServerModel) super.getModel();
	}
	
	@Override
	protected IApplicationEventManager initEventManager() {
		IApplicationEventManager bem = super.initEventManager();
		bem.addApplicationEventToHandlerMapping(ServerSpecificEvent.ADD_DISH, new AddDishHandler(this));
		bem.addApplicationEventToHandlerMapping(ServerSpecificEvent.REMOVE_DISH, new RemoveDishHandler(this));
		bem.addApplicationEventToHandlerMapping(ServerSpecificEvent.EDIT_DISH, new EditDishHandler(this));
		bem.addApplicationEventToHandlerMapping(ServerSpecificEvent.WRITE_DISH_MENU, new WriteDishMenuHandler(this));
		bem.addApplicationEventToHandlerMapping(ServerSpecificEvent.LOAD_DISH_MENU, new LoadDishMenuHandler(this));
		bem.addApplicationEventToHandlerMapping(ServerSpecificEvent.CONFIRM_ORDER, new ConfirmOrderHandler(this));
		bem.addApplicationEventToHandlerMapping(ServerSpecificEvent.CONFIRM_ALL_ORDERS, new ConfirmAllOrdersHandler(this));
		bem.addApplicationEventToHandlerMapping(ServerSpecificEvent.WRITE_ORDERS, new WriteOrdersHandler(this));
		bem.addApplicationEventToHandlerMapping(ServerSpecificEvent.ORDER_CONFIRM_MODEL_CHANGED, new OrderConfirmModelChangedHandler(this));
		return bem;
	}
}
