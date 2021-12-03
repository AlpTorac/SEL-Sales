package model.datamapper;

public class AggregateFormat {
	private String aggregateEntryFieldSeparator = ",";
	private String aggregateEntrySeparator = ";";
	
	public String getFieldSeparator() {
		return this.aggregateEntryFieldSeparator;
	}
	
	public String getEntrySeparator() {
		return this.aggregateEntrySeparator;
	}
}
