package model.datamapper;

import java.util.regex.Pattern;

/**
 * {someAttrName:field1}
 */
public class AttributeFormat implements IFormat {
	private String attributeStart = "{";
	
	private String nameAndFieldSeparator = ":";
	
	private String attributeEnd = "}";
	
	private Pattern pattern = Pattern.compile("["+ attributeStart + "]" + "[a-zA-Z]+" + "[" + nameAndFieldSeparator + "]" + "[^" + attributeStart+ attributeEnd + "]*" + "[" + attributeEnd + "]");
	
	protected String getAttributeNameFieldSeparator() {
		return this.nameAndFieldSeparator;
	}
	
	protected String getAttributeStart() {
		return this.attributeStart;
	}
	
	protected String getAttributeEnd() {
		return this.attributeEnd;
	}
	
	public String format(IAttribute attribute, String serialisedValue) {
		String result = "";
		
		result += this.getAttributeStart();
		result += attribute.getDescription();
		result += this.getAttributeNameFieldSeparator();
		result += serialisedValue;
		result += this.getAttributeEnd();
		
		return result;
	}

	public String[] getFields(String text) {
		String match;
		if ((match = this.getMatch(text)) != null) {
			match = match.replaceAll("["+this.getAttributeStart()+"]", "");
			match = match.replaceAll("["+this.getAttributeEnd()+"]", "");
			return match.split(this.getAttributeNameFieldSeparator());
		}
		return null;
	}
	
	@Override
	public Pattern getPattern() {
		return this.pattern;
	}
}
