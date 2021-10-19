package model.settings;

public interface ISettingsSerialiser {
	default String serialise(ISettings s) {
		String result = "";
		
		result += this.getFormat().getSettingsFileStartForString();
		for (String[] setting : s.getAllSettings()) {
			result += this.getFormat().getSettingsFieldStartForString();
			result += setting[0];
			result += this.getFormat().getSettingsFieldDescriptionSeparatorForString();
			result += setting[1];
			result += this.getFormat().getSettingsFieldEndForString();
		}
		result += this.getFormat().getSettingsFileEndForString();
		
		return result;
	}
	
	ISettingsFormat getFormat();
}
