package controller;

import controller.handler.AddDishHandler;
import controller.handler.AddOrderHandler;
import controller.handler.ConfirmOrderHandler;
import controller.handler.EditDishHandler;
import controller.handler.RemoveDishHandler;
import controller.handler.RemoveOrderHandler;
import controller.manager.BusinessEventManager;
import controller.manager.IBusinessEventManager;
import model.IModel;

public class MainController extends Controller {

	public MainController(IModel model) {
		super(model);
	}

	@Override
	protected IBusinessEventManager initEventManager() {
		BusinessEventManager bem = new BusinessEventManager();
		bem.addBusinessEventToHandlerMapping(BusinessEvent.ADD_DISH, new AddDishHandler(this));
		bem.addBusinessEventToHandlerMapping(BusinessEvent.REMOVE_DISH, new RemoveDishHandler(this));
		bem.addBusinessEventToHandlerMapping(BusinessEvent.EDIT_DISH, new EditDishHandler(this));
		bem.addBusinessEventToHandlerMapping(BusinessEvent.ADD_ORDER, new AddOrderHandler(this));
		bem.addBusinessEventToHandlerMapping(BusinessEvent.CONFIRM_ORDER, new ConfirmOrderHandler(this));
		bem.addBusinessEventToHandlerMapping(BusinessEvent.REMOVE_ORDER, new RemoveOrderHandler(this));
		return bem;
	}
}
