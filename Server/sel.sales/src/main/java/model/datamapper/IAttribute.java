package model.datamapper;

public interface IAttribute {
	default IAttribute parseAttribute(String description) {
		if (description != null) {
			for (IAttribute attr : this.getAllAttributes()) {
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
}