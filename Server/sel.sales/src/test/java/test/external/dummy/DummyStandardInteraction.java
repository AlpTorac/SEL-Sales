package test.external.dummy;

public class DummyStandardInteraction extends DummyInteraction {

	public DummyStandardInteraction(DummyStandardServer server, DummyStandardClient... clients) {
		super(server, clients);
		
	}
	
	@Override
	public DummyStandardServer getServer() {
		return (DummyStandardServer) super.getServer();
	}
	
	@Override
	protected void initialStart() {
		this.forEachClient(dc -> ((DummyStandardClient) dc).setTargetConnUtil(this.getServer().getConnUtil()));
		
		super.initialStart();
	}
}
