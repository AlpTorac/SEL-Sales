package test.external.dummy;

public class DummyStandardServer extends DummyServer {
	private DummyConnectionUtility connUtil;
	
	public DummyStandardServer(String name, String address, DummyConnectionUtility connUtil) {
		super(connUtil.getServiceInfo().getServiceName(),
				connUtil.getServiceInfo().getServiceID().toString(), name, address);
		this.connUtil = connUtil;
	}

	protected DummyConnectionUtility getConnUtil() {
		return this.connUtil;
	}
	
	@Override
	protected IDummyExternal initExternal() {
		return new DummyStandardServerExternal(this.getController(), this.getModel(), this.getConnUtil());
	}
	
	@Override
	public IDummyExternal getExternal() {
		return (IDummyExternal) super.getExternal();
	}
}
