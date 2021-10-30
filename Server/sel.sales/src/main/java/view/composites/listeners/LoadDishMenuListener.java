package view.composites.listeners;

import controller.BusinessEvent;
import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import view.repository.IRootComponent;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;

public class LoadDishMenuListener extends ClickEventListener implements IApplicationEventShooter {

	private IController controller;
	private UIComponentFactory fac;
	private IRootComponent root;
	
	public LoadDishMenuListener(IController controller, UIComponentFactory fac, IRootComponent root) {
		this.controller = controller;
		this.fac = fac;
		this.root = root;
	}
	
	@Override
	public void clickAction() {
		this.fireApplicationEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
//		String path = fac.createFileChooser().showOpenDialog(this.root).getPath();
//		System.out.println("Read path: " + path);
//		return new Object[] {path};
		return new Object[] {fac.createFileChooser().showOpenDialog(this.root).getPath()};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return BusinessEvent.LOAD_DISH_MENU;
	}
}
