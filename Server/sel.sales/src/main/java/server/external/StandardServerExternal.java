package server.external;

import external.IConnectionUtility;
import external.standard.StandardDeviceManager;
import external.standard.StandardService;
import model.settings.ISettings;
import server.controller.IServerController;
import server.model.IServerModel;

public class StandardServerExternal extends ServerExternal {
	private IConnectionUtility connUtil;
	
	public StandardServerExternal(IServerController controller, IServerModel model, IConnectionUtility connUtil,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.connUtil = connUtil;
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
