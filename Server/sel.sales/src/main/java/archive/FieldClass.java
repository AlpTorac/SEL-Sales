package archive;

import java.math.BigDecimal;

import model.connectivity.ClientData;

public enum FieldClass {
	CLIENT_DATA_ARRAY(ClientData[].class.getSimpleName()),
	BOOLEAN(boolean.class.getSimpleName()),
	INTEGER(int.class.getSimpleName()),
	DOUBLE(double.class.getSimpleName()),
	STRING(String.class.getSimpleName()),
	BIGDECIMAL(BigDecimal.class.getSimpleName())
	;
	
	private final String className;
	
	private FieldClass(String className) {
		this.className = className;
	}
	
	public String getClassName() {
		return this.className;
	}
	
	public static FieldClass stringToFieldClass(String serialisedFieldClass) {
		if (serialisedFieldClass == null) {
			return null;
		}
		for (FieldClass fc : FieldClass.values()) {
			if (serialisedFieldClass.equals(fc.getClassName())) {
				return fc;
			}
		}
		return null;
	}
}
