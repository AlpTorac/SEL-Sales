package external.standard;

import external.IConnectionUtility;
import external.connection.IConnectionObject;
import external.connection.IService;
import external.connection.incoming.IConnectionNotifier;

public class StandardConnectionNotifier implements IConnectionNotifier {

	private IConnectionUtility connUtil;
	private IService service;
	
	public StandardConnectionNotifier(IService service, IConnectionUtility connUtil) {
		this.service = service;
		this.connUtil = connUtil;
	}
	
	@Override
	public IConnectionObject acceptAndOpen() {
		return this.connUtil.acceptAndOpenAlgorithm(this);
	}

	@Override
	public IService getService() {
		return this.service;
	}
	
}
