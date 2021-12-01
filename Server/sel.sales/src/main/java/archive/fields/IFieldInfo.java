package archive.fields;

public interface IFieldInfo {
	void addField(IFieldNameEnum fieldName, FieldClass className);
	void removeField(IFieldNameEnum fieldName);
	void addFieldSeparator(String separator);
	void removeSeparatorField(int position);
	default String getStartIndicator() {
		return this.getFieldSeparator(0);
	}
	default String getEndIndicator() {
		return this.getFieldSeparator(this.getFieldSeparatorCount() - 1);
	}
	String getFieldSeparator(int position);
	int getFieldPosition(IFieldNameEnum fieldName);
	int getFieldCount();
	int getFieldSeparatorCount();
	FieldClass getFieldClass(IFieldNameEnum fieldName);
}
