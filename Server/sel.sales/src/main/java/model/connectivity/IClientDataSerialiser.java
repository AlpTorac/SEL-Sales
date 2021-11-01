package model.connectivity;

public interface IClientDataSerialiser {
	String serialiseClientDatas(IClientData[] cds);
	String serialiseClientData(IClientData cd);
	default String serialiseBoolean(boolean b) {
		if (b == false) {
			return "0";
		} else {
			return "1";
		}
	}
	IClientDataFormat getFormat();
}
