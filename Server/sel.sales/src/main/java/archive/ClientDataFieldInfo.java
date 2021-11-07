package archive;

public class ClientDataFieldInfo extends FieldInfo {
	ClientDataFieldInfo() {
		super();
		this.addFieldSeparator("");
		this.addField(ClientDataFieldNames.CLIENT_NAME, FieldClass.STRING);
		this.addFieldSeparator(",");
		this.addField(ClientDataFieldNames.CLIENT_ADDRESS, FieldClass.STRING);
		this.addFieldSeparator(",");
		this.addField(ClientDataFieldNames.IS_ALLOWED_TO_CONNECT, FieldClass.BOOLEAN);
		this.addFieldSeparator(";"+System.lineSeparator());
	}
}
