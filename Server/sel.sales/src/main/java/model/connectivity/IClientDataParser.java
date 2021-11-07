package model.connectivity;

import model.util.IParser;

public interface IClientDataParser extends IParser {
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
}
