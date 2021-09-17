package external;

public abstract class Service implements IService {
	private IServiceConnectionManager scm;
	private String id;
	private String url;
	private String name;
	
	public Service(String id, String name) {
		this.id = id;
		this.name = name;
		this.generateAndSetURL();
	}
	
	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getURL() {
		return this.url;
	}

	@Override
	public IServiceConnectionManager getServiceConnectionManager() {
		return this.scm;
	}

}
