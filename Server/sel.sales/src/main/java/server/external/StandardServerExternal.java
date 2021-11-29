package server.external;

import external.IConnectionUtility;
import external.standard.StandardDeviceManager;
import external.standard.StandardService;
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
