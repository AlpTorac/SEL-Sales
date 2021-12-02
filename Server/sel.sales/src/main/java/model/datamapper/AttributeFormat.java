package model.datamapper;

public class AttributeFormat {
	private String fileStart = "";
	
	private String fieldStart = "";
	private String fieldSeparator = "#";
	private String fieldEnd = ";" + System.lineSeparator();
	
	private String fileEnd = "";
	
	
	public String getFieldStart() {
		return this.fieldStart;
	}
	
	public String getFieldSeparator() {
		return this.fieldSeparator;
	}
	
	public String getFieldEnd() {
		return this.fieldEnd;
	}
	
	public String getFileStart() {
		return this.fileStart;
	}
	
	public String getFileEnd() {
		return this.fileEnd;
	}
}
