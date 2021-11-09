package external.connection;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import controller.GeneralEvent;

public class DisconnectionListener implements IApplicationEventShooter {

	private IController controller;
	private String deviceAddress;
	
	public DisconnectionListener(IController controller) {
		this.controller = controller;
	}
	
	public void connectionLost(String deviceAddress) {
		this.deviceAddress = deviceAddress;
		this.fireApplicationEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.deviceAddress};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return GeneralEvent.DEVICE_DISCONNECTED;
	}
}
