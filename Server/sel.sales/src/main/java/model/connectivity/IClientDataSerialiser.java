package model.connectivity;

import model.util.ISerialiser;

public interface IClientDataSerialiser extends ISerialiser {
	String serialiseClientDatas(IClientData[] cds);
	String serialiseClientData(IClientData cd);
	IClientDataFormat getFormat();
}
