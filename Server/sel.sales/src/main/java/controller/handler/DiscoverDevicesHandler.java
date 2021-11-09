package controller.handler;

import controller.IController;

public class DiscoverDevicesHandler extends ApplicationEventHandler {
	public DiscoverDevicesHandler(IController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().requestDeviceRediscovery(()->{});
	}
}
