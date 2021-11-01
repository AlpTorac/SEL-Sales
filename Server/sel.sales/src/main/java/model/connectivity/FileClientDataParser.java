package model.connectivity;

import java.util.ArrayList;
import java.util.Collection;

public class FileClientDataParser extends ClientDataParser {
	public FileClientDataParser() {
		super(new FileClientDataFormat());
	}

	@Override
	public IClientData[] parseClientDatas(String serialisedClientDatas) {
		Collection<IClientData> cds = new ArrayList<IClientData>();
		String body = this.getSerialisedClientDatasBody(serialisedClientDatas);
		String[] serialisedDatas = body.split(this.getFormat().getDataFieldEnd());
		for (String sd : serialisedDatas) {
			if (sd != null && sd.length() > 0) {
				cds.add(parseClientData(sd));
			}
		}
		return cds.toArray(IClientData[]::new);
	}

	@Override
	public IClientData parseClientData(String serialisedClientData) {
//		System.out.println("Serialised data: " + serialisedClientData);
		String[] fields = serialisedClientData.split(this.getFormat().getDataFieldSeparator());
		
		String name = null;
		String address = null;
		boolean isAllowedToConnect = false;
		
		if (fields.length > 0) {
			name = fields[0];
		} else {
			return null;
		}
		
		if (fields.length > 1) {
			address = fields[1];
		}
		
		if (fields.length > 2) {
			isAllowedToConnect = this.parseBoolean(fields[2]);
		}
//		System.out.println("Serialised name: " + fields[0]);
//		System.out.println("Serialised address: " + fields[1]);
//		System.out.println("Serialised boolean: " + fields[2]);
		return new ClientData(name, address, isAllowedToConnect);
	}
}
