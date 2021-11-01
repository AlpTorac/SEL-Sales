package model.connectivity;

public interface IClientDataParser {
	IClientData[] parseClientDatas(String serialisedClientDatas);
	IClientData parseClientData(String serialisedClientData);
	IClientDataFormat getFormat();
	default String getSerialisedClientDatasBody(String serialisedClientDatas) {
		int begin = 0;
		int end = serialisedClientDatas.length();
		if (serialisedClientDatas.startsWith(this.getFormat().getStartIndicator())) {
			begin += this.getFormat().getStartIndicator().length();
		}
		if (serialisedClientDatas.endsWith(this.getFormat().getEndIndicator())) {
			end -= this.getFormat().getEndIndicator().length();
		}
		return serialisedClientDatas.substring(begin, end);
	}
	default boolean parseBoolean(String serialisedBoolean) {
//		System.out.println("Serialised boolean: " + serialisedBoolean);
		if (serialisedBoolean.equals("0")) {
			return false;
		} else {
			return true;
		}
	}
}
