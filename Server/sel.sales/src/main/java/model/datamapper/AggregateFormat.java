package model.datamapper;

import java.util.regex.Pattern;

public class AggregateFormat implements IFormat {
	private String aggregateEntryFieldSeparator = ",";
	private String aggregateEntrySeparator = ";";
	
	private Pattern pattern = Pattern.compile("(" + "[^"+aggregateEntryFieldSeparator + aggregateEntrySeparator +"]"
			+ aggregateEntryFieldSeparator + "[^"+aggregateEntryFieldSeparator + aggregateEntrySeparator +"]" + ")*"
			+ aggregateEntrySeparator);
	
	public String getFieldSeparator() {
		return this.aggregateEntryFieldSeparator;
	}
	
	public String getEntrySeparator() {
		return this.aggregateEntrySeparator;
	}

	@Override
	public Pattern getPattern() {
		return this.pattern;
	}
	
	public String format(String... serialisedItems) {
		String result = "";
		int size = serialisedItems.length;
		for (int i = 0; i < size - 1; i++) {
			result += serialisedItems[i];
			result += this.getFieldSeparator();
		}
		
		result += serialisedItems[size - 1];
		result += this.getEntrySeparator();
		
		return result;
	}
	
	public String[] getFields(String text) {
		String match;
		if ((match = this.getMatch(text)) != null) {
			match = match.replaceAll("["+this.getEntrySeparator()+"]", "");
			return match.split(this.getFieldSeparator());
		}
		return null;
	}
	
}
