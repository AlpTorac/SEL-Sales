package controller;

import controller.handler.AddDishHandler;
import controller.handler.AddOrderHandler;
import controller.handler.ConfirmOrderHandler;
import controller.handler.EditDishHandler;
import controller.handler.RemoveDishHandler;
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
		return bem;
	}
}
