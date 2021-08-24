package controller;

public class DummyController extends Controller {
	
	@Override
	protected IBusinessEventManager initEventManager() {
		IBusinessEventManager eventManager = new BusinessEventManager();
		eventManager.addBusinessEventToHandlerMapping(BusinessEvent.SHOW_MENU, new DummyHandler());
		return eventManager;
	}
}
