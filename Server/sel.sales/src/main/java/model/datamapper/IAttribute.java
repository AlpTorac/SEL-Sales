package model.datamapper;

public interface IAttribute {
	static IAttribute parseAttribute(IAttribute[] attrs, String description) {
		if (description != null) {
			for (IAttribute attr : attrs) {
				if (description.equals(attr.getDescription())) {
					return attr;
				}
			}
		}
		return null;
	}
	IAttribute[] getAllAttributes();
	String getDescription();
	Object getDefaultValue();
	default int compareTo(IAttribute attr) {
		return this.getDescription().compareTo(attr.getDescription());
	}
}