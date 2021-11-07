package archive;

public class ClientDataFileInfo extends FieldInfo {
	ClientDataFileInfo() {
		super();
		this.addFieldSeparator("");
		this.addField(ClientDataFileFieldNames.CLIENT_DATA_ARRAY, FieldClass.CLIENT_DATA_ARRAY);
		this.addFieldSeparator("");
	}
}
