package model.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FieldPermutation implements IFieldPermutation {

	private Map<IFieldNameEnum, Integer> positions;
	
	public FieldPermutation() {
		this.positions = new ConcurrentHashMap<IFieldNameEnum, Integer>();
	}
	
	@Override
	public void addField(IFieldNameEnum fieldName, int position) {
		this.positions.put(fieldName, Integer.valueOf(position));
	}
	
	@Override
	public void removeField(IFieldNameEnum fieldName) {
		this.positions.remove(fieldName);
	}
	
	@Override
	public int getFieldPosition(IFieldNameEnum fieldName) {
		return this.positions.get(fieldName);
	}
}
