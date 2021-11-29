package test.external.dummy;

public class DummyStandardClient extends DummyClient {
	private DummyConnectionUtility connUtil;
	
	public DummyStandardClient(String name, String address, DummyConnectionUtility connUtil) {
		super(connUtil.getServiceInfo().getServiceName(),
				connUtil.getServiceInfo().getServiceID().toString(), name, address);
		this.connUtil = connUtil;
	}
	
	protected DummyConnectionUtility getConnUtil() {
		return this.connUtil;
	}
	
	protected void setTargetConnUtil(DummyConnectionUtility connUtil) {
		this.connUtil.setConnectionTarget(connUtil);
	}
	
	@Override
	protected IDummyExternal initExternal() {
		return new DummyStandardClientExternal(this.getController(), this.getModel(), this.getConnUtil());
	}
	
	@Override
	public IDummyExternal getExternal() {
		return (IDummyExternal) super.getExternal();
	}
}
