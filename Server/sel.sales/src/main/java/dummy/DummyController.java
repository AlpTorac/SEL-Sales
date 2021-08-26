package dummy;

import controller.BusinessEvent;
import controller.BusinessEventManager;
import controller.Controller;
import controller.IBusinessEventManager;
import model.IDishMenuItemData;
import model.IModel;

public class DummyController extends Controller {
	
	public DummyController(IModel model) {
		super(model);
	}

	@Override
	protected IBusinessEventManager initEventManager() {
		IBusinessEventManager eventManager = new BusinessEventManager();
		eventManager.addBusinessEventToHandlerMapping(BusinessEvent.SHOW_MENU, new DummyHandler());
		return eventManager;
	}
}
