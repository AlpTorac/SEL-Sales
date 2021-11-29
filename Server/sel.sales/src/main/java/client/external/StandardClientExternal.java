package client.external;

import client.controller.IClientController;
import client.model.IClientModel;
import external.IConnectionUtility;
import external.connection.IService;
import external.connection.outgoing.IExternalConnector;
import external.device.IDeviceManager;
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
		return new StandardExternalConnector(this.getService(), this.getController(), es,
				this.connUtil, this.getPingPongTimeout(), this.getMinimalPingPongDelay(), this.getSendTimeout(), this.getResendLimit());
	}

	@Override
	protected StandardDeviceManager initDeviceManager() {
		return new StandardDeviceManager(es, this.getController(), this.connUtil);
	}

	@Override
	protected StandardService initService() {
		return new StandardService(this.initDeviceManager(), this.getController(), es, this.connUtil,
				this.getPingPongTimeout(), this.getMinimalPingPongDelay(), this.getSendTimeout(), this.getResendLimit());
	}

	protected IConnectionUtility getConnectionUtility() {
		return this.connUtil;
	}
}
