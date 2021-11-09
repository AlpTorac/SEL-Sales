package client.external;

import client.controller.IClientController;
import client.model.IClientModel;

public class BluetoothClientExternal extends ClientExternal {

	public BluetoothClientExternal(IClientController controller, IClientModel model, long pingPongTimeout,
			long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}

}
