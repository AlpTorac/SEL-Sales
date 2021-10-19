package test.model.settings;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.settings.ISettings;
import model.settings.ISettingsSerialiser;
import model.settings.Settings;
import model.settings.SettingsField;
import model.settings.StandardSettingsSerialiser;
@Execution(value = ExecutionMode.SAME_THREAD)
class SettingsSerialiserTest {

	private ISettingsSerialiser serialiser = new StandardSettingsSerialiser();
	
	/**
	 * Stream operation can change the order
	 */
	@Test
	void test() {
		ISettings s = new Settings();
		s.addSetting(SettingsField.DISH_MENU_FOLDER, "dmfa");
		s.addSetting(SettingsField.ORDER_FOLDER, "ofa");
		String serialisedS = serialiser.serialise(s);
		
		Assertions.assertTrue(("dishMenuFolder|dmfa;\r\norderFolder|ofa;\r\n").equals(serialisedS) ||
				("orderFolder|ofa;\r\ndishMenuFolder|dmfa;\r\n").equals(serialisedS));
	}

}
