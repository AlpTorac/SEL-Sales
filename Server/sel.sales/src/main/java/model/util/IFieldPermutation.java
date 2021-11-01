package model.util;

public interface IFieldPermutation {
	void addField(IFieldNameEnum fieldName, int position);
	void removeField(IFieldNameEnum fieldName);
	int getFieldPosition(IFieldNameEnum fieldName);
}
