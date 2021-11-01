package model.connectivity;

public interface IClientDataFormat {
	String getStartIndicator();
	String getDataFieldSeparator();
	String getDataFieldEnd();
	String getEndIndicator();
}
