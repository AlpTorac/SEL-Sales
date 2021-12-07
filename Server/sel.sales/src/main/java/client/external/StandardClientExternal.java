package client.external;

import client.controller.IClientController;
import client.model.IClientModel;
import external.IConnectionUtility;
import external.standard.StandardDeviceManager;
import external.standard.StandardExternalConnector;
import external.standard.StandardService;

public class StandardClientExternal extends ClientExternal {
	private IConnectionUtility connUtil;
	
	public StandardClientExternal(IClientController controller, IClientModel model, IConnectionUtility connUtil, long pingPongTimeout,
			long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.connUtil = connUtil;
	}

	@Override
	protected StandardExternalConnector initConnector() {
		return new StandardExternalConnector(this.getService(), this.getController(), this.getES(),
				this.connUtil, this.getPingPongTimeoutInMillis(), this.getMinimalPingPongDelay(), this.getSendTimeoutInMillis(), this.getPingPongResendLimit());
	}

	@Override
	protected StandardDeviceManager initDeviceManager() {
		return new StandardDeviceManager(this.getES(), this.getController(), this.connUtil);
	}

	@Override
	protected StandardService initService() {
		return new StandardService(this.initDeviceManager(), this.getController(), this.getES(), this.connUtil,
				this.getPingPongTimeoutInMillis(), this.getMinimalPingPongDelay(), this.getSendTimeoutInMillis(), this.getPingPongResendLimit());
	}

	protected IConnectionUtility getConnectionUtility() {
		return this.connUtil;
	}
}
