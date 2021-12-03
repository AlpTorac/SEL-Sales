package test.model.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import model.settings.ISettings;
import model.settings.ISettingsParser;
import model.settings.SettingsField;
import model.settings.StandardSettingsParser;
//@Execution(value = ExecutionMode.SAME_THREAD)
class SettingsParserTest {

	private ISettingsParser parser = new StandardSettingsParser();
	
	@Test
	void test() {
		ISettings settings = parser.parseSettings("dishMenuFolder|dmfa;"+System.lineSeparator()
				+ "orderFolder|ofa;" + System.lineSeparator());
		
		Assertions.assertEquals(settings.getSetting(SettingsField.ORDER_FOLDER), "ofa");
		Assertions.assertEquals(settings.getSetting(SettingsField.DISH_MENU_FOLDER), "dmfa");
	}

}
