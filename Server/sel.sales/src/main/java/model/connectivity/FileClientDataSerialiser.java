package model.connectivity;

public class FileClientDataSerialiser extends ClientDataSerialiser {
	public FileClientDataSerialiser() {
		super(new FileClientDataFormat());
	}
	
	@Override
	public String serialiseClientDatas(IClientData[] cds) {
		String result = "";
		result += this.getFormat().getStartIndicator();
		for (int i = 0; i < cds.length; i++) {
			result += this.serialiseClientData(cds[i]);
		}
		result += this.getFormat().getEndIndicator();
		return result;
	}

	@Override
	public String serialiseClientData(IClientData cd) {
		String result = "";
		if (cd.getClientName() != null) {
			result += cd.getClientName() + this.getFormat().getDataFieldSeparator();
		}
		if (cd.getClientAddress() != null) {
			result += cd.getClientAddress() + this.getFormat().getDataFieldSeparator();
		}
		result += this.serialiseBoolean(cd.getIsAllowedToConnect());
		result += this.getFormat().getDataFieldEnd();
		return result;
	}
}
