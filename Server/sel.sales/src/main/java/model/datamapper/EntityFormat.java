package model.datamapper;

import java.util.regex.Pattern;

import model.entity.id.EntityID;

/**
 * <someID@serialisedAttributes>
 */
public class EntityFormat implements IFormat {
	private String entityStart = "<";
	private String idAndAttributesSeparator = "@";
	private String entityEnd = ">";
	
	private Pattern pattern = Pattern.compile(entityStart + "[^" + entityStart + entityEnd + "]*" + entityEnd);
	
	public String getEntityStart() {
		return this.entityStart;
	}
	
	public String getEntityEnd() {
		return this.entityEnd;
	}
	
	public String getIDAndAttributeSeparator() {
		return this.idAndAttributesSeparator;
	}
	
	public Pattern getPattern() {
		return this.pattern;
	}
	
	public String format(EntityID id, String serialisedAttributes) {
		String result = "";
		
		result += this.getEntityStart();
		result += id.toString();
		result += this.getIDAndAttributeSeparator();
		result += serialisedAttributes;
		result += this.getEntityEnd();
		
		return result;
	}
	
	public String[] getFields(String serialisedEntity) {
		String match = this.getMatch(serialisedEntity);
		if (match != null) {
			match = match.replaceAll(this.getEntityStart(), "");
			match = match.replaceAll(this.getEntityEnd(), "");
			return match.split(this.getIDAndAttributeSeparator());
		}
		return null;
	}
}
