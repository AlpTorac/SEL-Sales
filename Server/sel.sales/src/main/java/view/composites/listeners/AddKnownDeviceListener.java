package view.composites.listeners;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import controller.GeneralEvent;
import model.connectivity.IDeviceData;
import view.repository.ITable;
import view.repository.uiwrapper.ClickEventListener;

public class AddKnownDeviceListener extends ClickEventListener implements IApplicationEventShooter {

	private ITable<IDeviceData> discoveredDevices;
	private IController controller;
	
	public AddKnownDeviceListener(IController controller, ITable<IDeviceData> discoveredDevices) {
		this.controller = controller;
		this.discoveredDevices = discoveredDevices;
	}
	
	public void clickAction() {
		this.fireApplicationEvent(this.controller);
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.discoveredDevices.getSelectedElement().getDeviceAddress()};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return GeneralEvent.KNOWN_DEVICE_ADDED;
	}
}
