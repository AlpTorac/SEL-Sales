package model.settings;

public abstract class SettingsFormat implements ISettingsFormat {

	private String fileStart;
	private String fileEnd;
	
	private String fieldStart;
	private String fieldDescSeparator;
	private String fieldEnd;
	
	SettingsFormat(String fileStart, String fileEnd, String fieldStart, String fieldDescSeparator, String fieldEnd) {
		this.fileStart = fileStart;
		this.fileEnd = fileEnd;
		this.fieldStart = fieldStart;
		this.fieldDescSeparator = fieldDescSeparator;
		this.fieldEnd = fieldEnd;
	}
	
	@Override
	public String getSettingsFieldStart() {
		return this.fieldStart;
	}

	@Override
	public String getSettingsFieldEnd() {
		return this.fieldEnd;
	}

	@Override
	public String getSettingsFieldDescriptionSeparator() {
		return this.fieldDescSeparator;
	}

	@Override
	public String getSettingsFileStart() {
		return this.fileStart;
	}

	@Override
	public String getSettingsFileEnd() {
		return this.fileEnd;
	}
	@Override
	public String getSettingsFieldStartForString() {
		return this.fieldStart;
	}
	
	@Override
	public String getSettingsFieldEndForString() {
		return this.fieldEnd;
	}
	
	@Override
	public String getSettingsFieldDescriptionSeparatorForString() {
		return this.fieldDescSeparator;
	}
	
	@Override
	public String getSettingsFileStartForString() {
		return this.fileStart;
	}
	
	@Override
	public String getSettingsFileEndForString() {
		return this.fileEnd;
	}

}
