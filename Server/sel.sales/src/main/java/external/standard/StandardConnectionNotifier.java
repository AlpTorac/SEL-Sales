package external.standard;

import external.IConnectionUtility;
import external.connection.IConnectionObject;
import external.connection.IService;
import external.connection.incoming.IConnectionNotifier;

public class StandardConnectionNotifier implements IConnectionNotifier {
	private IConnectionUtility connUtil;
	private IService service;
	
	private Object connectionNotifierObject;
	
	public StandardConnectionNotifier(IService service, IConnectionUtility connUtil, Object connectionNotifierObject) {
		this.service = service;
		this.connUtil = connUtil;
		this.connectionNotifierObject = connectionNotifierObject;
	}
	
	@Override
	public IConnectionObject acceptAndOpen() {
		return this.connUtil.acceptAndOpenAlgorithm(this);
	}

	@Override
	public IService getService() {
		return this.service;
	}

	@Override
	public Object getConnectionNotifierObject() {
		return this.connectionNotifierObject;
	}
}
