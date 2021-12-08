package model.datamapper;

import model.entity.ValueObject;
import model.util.ISerialiser;

public abstract class ExportSerialiser<A extends IAttribute, V extends ValueObject<A>> implements ISerialiser {
//	private String nonMetaChar = "[^" + getFieldSeparator() + getRowEnd() + "]";
//	private Pattern pattern = Pattern.compile("("+nonMetaChar+"*"+getFieldSeparator()+")*"+getRowEnd());
	
	public ExportSerialiser() {
		
	}
	
//	@Override
//	public Pattern getPattern() {
//		return this.pattern;
//	}
	
	public String getFieldSeparator() {
		return ",";
	}
	
	public String getRowEnd() {
		return ";";
	}
	
	/**
	 * Applies all the functions to each value object and gets the wanted
	 * attributes of the value objects. Each function applied corresponds
	 * to an entry, each value object has its own row.
	 * 
	 * @param valueObject The value object to be serialised
	 * @return The serialised values
	 */
	public abstract String serialise(V valueObject);
	
	/**
	 * Applies all the functions to each value object and gets the wanted
	 * attributes of the value objects. Each function applied corresponds
	 * to an entry, each value object has its own row.
	 * 
	 * @param valueObjects The value objects to be serialised
	 * @return The serialised values
	 */
	public String serialise(V[] valueObjects) {
		String result = "";
		for (V v : valueObjects) {
			result += this.serialise(v);
		}
		return result;
	}
}
