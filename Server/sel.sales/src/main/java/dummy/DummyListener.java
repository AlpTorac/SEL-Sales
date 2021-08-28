package dummy;

import controller.BusinessEvent;
import controller.IBusinessEventShooter;
import controller.IController;
import view.repository.uiwrapper.ClickEventListener;

public class DummyListener extends ClickEventListener implements IBusinessEventShooter {

	private IController controller;
	
	DummyListener(IController controller) {
		super();
		this.controller = controller;
	}

	@Override
	public void clickAction() {
		this.fireBusinessEvent(this.controller);
	}

	@Override
	public Object[] getArgs() {
		return null;
	}

	@Override
	public BusinessEvent getBusinessEvent() {
		return BusinessEvent.SHOW_MENU;
	}
}
