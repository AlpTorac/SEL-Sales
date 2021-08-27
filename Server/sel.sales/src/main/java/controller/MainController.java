package controller;

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
		return bem;
	}
}
