package view;

import controller.BusinessEvent;
import controller.IBusinessEventShooter;
import controller.IController;
import view.repository.ClickEventListener;
import view.repository.HasText;
import view.repository.IEventShooterUIComponent;

public class DummyListener extends ClickEventListener implements IBusinessEventShooter {

	private IController controller;
	
	DummyListener(IEventShooterUIComponent component, IController controller) {
		super(component);
		this.controller = controller;
	}

	@Override
	public void clickAction() {
		((HasText) this.getComponent()).setCaption("Selected");
		this.fireBusinessEvent(this.controller);
	}

	@Override
	public void fireBusinessEvent(IController controller) {
		this.controller.handleBusinessEvent(BusinessEvent.SHOW_MENU, null);
	}
}
