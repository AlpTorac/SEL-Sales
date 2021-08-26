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
		
		return bem;
	}
}
