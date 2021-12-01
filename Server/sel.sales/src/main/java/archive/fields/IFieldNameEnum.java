package archive.fields;

public interface IFieldNameEnum {
	default IFieldNameEnum stringToFieldClass(String serialisedFieldClass) {
		if (serialisedFieldClass == null) {
			return null;
		}
		for (IFieldNameEnum fc : this.getValues()) {
			if (serialisedFieldClass.equals(fc.serialiseFieldName())) {
				return fc;
			}
		}
		return null;
	}
	IFieldNameEnum[] getValues();
	String serialiseFieldName();
}
