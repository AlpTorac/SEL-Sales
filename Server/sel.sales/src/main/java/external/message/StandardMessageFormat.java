package external.message;

public class StandardMessageFormat extends MessageFormat {
	public StandardMessageFormat() {
		super("[|]", ",", "", "\n");
	}

	@Override
	public String getDataFieldSeparatorForString() {
		return "|";
	}

	@Override
	public String getDataFieldElementSeparatorForString() {
		return super.getDataFieldElementSeparator();
	}
}
