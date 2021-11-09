package external.connection;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import controller.GeneralEvent;

public class ConnectionListener implements IApplicationEventShooter {

	private IController controller;
	private String deviceAddress;
	
	public ConnectionListener(IController controller) {
		this.controller = controller;
	}
	
	public void connectionEstablished(String deviceAddress) {
		this.deviceAddress = deviceAddress;
		this.fireApplicationEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.deviceAddress};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return GeneralEvent.DEVICE_CONNECTED;
	}
}
