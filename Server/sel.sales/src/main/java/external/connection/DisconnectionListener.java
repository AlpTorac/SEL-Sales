package external.connection;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import controller.StatusEvent;

public class DisconnectionListener implements IApplicationEventShooter {

	private IController controller;
	private String clientAddress;
	
	public DisconnectionListener(IController controller) {
		this.controller = controller;
	}
	
	public void connectionLost(String clientAddress) {
		this.clientAddress = clientAddress;
		this.fireApplicationEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.clientAddress};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return StatusEvent.CLIENT_DISCONNECTED;
	}
}
