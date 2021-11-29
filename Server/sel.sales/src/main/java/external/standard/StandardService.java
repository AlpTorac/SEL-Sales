package external.standard;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.IConnectionUtility;
import external.connection.Service;

public class StandardService extends Service {
	private IConnectionUtility connUtil;
	
	public StandardService(StandardDeviceManager deviceManager, IController controller, ExecutorService es,
			IConnectionUtility connUtil, long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(connUtil.getServiceInfo().getServiceID(), connUtil.getServiceInfo().getServiceName(), deviceManager, controller, es, pingPongTimeout,
				minimalPingPongDelay, sendTimeout, resendLimit);
		this.connUtil = connUtil;
	}
	
	@Override
	public String generateURL() {
		return this.connUtil.getServiceURL(this);
	}
	
	@Override
	public StandardDeviceManager getDeviceManager() {
		return (StandardDeviceManager) super.getDeviceManager();
	}

	@Override
	public void publish() {
		this.scm = new StandardServiceConnectionManager(this, this.getDeviceManager(), this.getController(), es, this.connUtil,
				this.getPingPongTimeout(), this.getMinimalPingPongDelay(), this.getSendTimeout(), this.getResendLimit());
		this.scm.makeNewConnectionThread();
		System.out.println("Service published");
	}
}
