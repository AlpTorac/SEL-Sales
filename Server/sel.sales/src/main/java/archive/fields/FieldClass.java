package archive.fields;

import java.math.BigDecimal;

import model.connectivity.DeviceData;

public enum FieldClass {
	DEVICE_DATA_ARRAY(DeviceData[].class),
	BOOLEAN(boolean.class),
	INTEGER(int.class),
	DOUBLE(double.class),
	STRING(String.class),
	BIGDECIMAL(BigDecimal.class)
	;
	
	private final Class<?> cls;
	
	private FieldClass(Class<?> className) {
		this.cls = className;
	}
	
	public String getClassName() {
		return this.cls.getSimpleName();
	}
	
	public static FieldClass stringToFieldClass(Class<?> serialisedFieldClass) {
		if (serialisedFieldClass == null) {
			return null;
		}
		for (FieldClass fc : FieldClass.values()) {
			if (serialisedFieldClass.equals(fc.cls)) {
				return fc;
			}
		}
		return null;
	}
}
