package view.composites.listeners;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import controller.GeneralEvent;
import model.connectivity.IDeviceData;
import view.repository.ITable;
import view.repository.uiwrapper.ClickEventListener;

public class ConnectionBlockedListener extends ClickEventListener implements IApplicationEventShooter {

	private ITable<IDeviceData> knownDevices;
	private IController controller;
	
	public ConnectionBlockedListener(IController controller, ITable<IDeviceData> knownDevices) {
		this.controller = controller;
		this.knownDevices = knownDevices;
	}

	public void clickAction() {
		this.fireApplicationEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.knownDevices.getSelectedElement().getDeviceAddress()};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return GeneralEvent.KNOWN_DEVICE_BLOCKED;
	}
}
