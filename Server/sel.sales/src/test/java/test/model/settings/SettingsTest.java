package test.model.settings;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.settings.ISettings;
import model.settings.Settings;
import model.settings.SettingsField;
import test.GeneralTestUtilityClass;
//@Execution(value = ExecutionMode.SAME_THREAD)
class SettingsTest {

	private ISettings settings;
	
	@BeforeEach
	void prep() {
		settings = new Settings();
	}
	
	@AfterEach
	void cleanUp() {
		settings = null;
	}
	
	@Test
	void removeSettingsTest() {
		settings.initAllSettingsFields();
		for (SettingsField sf : SettingsField.values()) {
			settings.addSetting(sf, "someVal");
			Assertions.assertTrue(settings.settingExists(sf));
			settings.removeSetting(sf);
			Assertions.assertFalse(settings.settingExists(sf));
		}
		Assertions.assertTrue(settings.equals(new Settings()));
	}
	
	@Test
	void removeNullSettingsTest() {
		Assertions.assertDoesNotThrow(() -> {settings.removeSetting(SettingsField.ORDER_FOLDER);});
	}
	
	@Test
	void addSettingTest() {
		String fieldVal = "dmfa";
		settings.addSetting(SettingsField.DISH_MENU_FOLDER, fieldVal);
		Assertions.assertTrue(settings.settingExists(SettingsField.DISH_MENU_FOLDER));
		Assertions.assertTrue(settings.getSetting(SettingsField.DISH_MENU_FOLDER).equals(fieldVal));
	}
	
	@Test
	void addNullSettingTest() {
		String fieldVal = null;
		settings.addSetting(SettingsField.DISH_MENU_FOLDER, fieldVal);
		Assertions.assertFalse(settings.settingExists(SettingsField.DISH_MENU_FOLDER));
		Assertions.assertTrue(settings.getSetting(SettingsField.DISH_MENU_FOLDER) == null);
	}
	
	@Test
	void changeSettingValueTest() {
		String fieldVal = "dmfa";
		settings.addSetting(SettingsField.DISH_MENU_FOLDER, fieldVal);
		Assertions.assertTrue(settings.settingExists(SettingsField.DISH_MENU_FOLDER));
		Assertions.assertTrue(settings.getSetting(SettingsField.DISH_MENU_FOLDER).equals(fieldVal));
		
		String newFieldVal = "dfma2";
		settings.changeSettingValue(SettingsField.DISH_MENU_FOLDER, newFieldVal);
		Assertions.assertTrue(settings.settingExists(SettingsField.DISH_MENU_FOLDER));
		Assertions.assertTrue(settings.getSetting(SettingsField.DISH_MENU_FOLDER).equals(newFieldVal));
	}
	
	@Test
	void changeNullSettingValueTest() {
		String fieldVal = null;
		settings.addSetting(SettingsField.DISH_MENU_FOLDER, fieldVal);
		Assertions.assertFalse(settings.settingExists(SettingsField.DISH_MENU_FOLDER));
		Assertions.assertTrue(settings.getSetting(SettingsField.DISH_MENU_FOLDER) == null);
		
		String newFieldVal = "dfma2";
		settings.changeSettingValue(SettingsField.DISH_MENU_FOLDER, newFieldVal);
		Assertions.assertTrue(settings.settingExists(SettingsField.DISH_MENU_FOLDER));
		Assertions.assertTrue(settings.getSetting(SettingsField.DISH_MENU_FOLDER).equals(newFieldVal));
	}
	
	@Test
	void isEmpty() {
		Assertions.assertTrue(settings.isEmpty());
		settings.initAllSettingsFields();
		Assertions.assertTrue(settings.isEmpty());
	}
	
	@Test
	void initSettingsFieldsTest() {
		settings.initAllSettingsFields();
		String[][] ssActual = settings.getAllSettings();
		SettingsField[] vals = SettingsField.values();
		String[][] ssExpected = new String[vals.length][2];
		for (int i = 0; i < ssExpected.length; i++) {
			ssExpected[i] = new String[] {vals[i].toString(), settings.getPlaceholder()};
		}
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(ssExpected, ssActual,
				(ss1,ss2)->{return GeneralTestUtilityClass.arrayContentEquals(ss1, ss2,
						(s1,s2)->{return s1.equals(s2);});}));
	}
	
	@Test
	void normalCaseEqualsTest() {
		settings.initAllSettingsFields();
		ISettings settings2 = new Settings();
		settings2.initAllSettingsFields();
		
		Assertions.assertTrue(settings != settings2);
		Assertions.assertTrue(settings.equals(settings2));
	}
	
	@Test
	void equalsNoExceptionTest() {
		ISettings nullSettings = null;
		Assertions.assertFalse(settings.equals(nullSettings));
		
		Object someRandomObject = new Object();
		Assertions.assertFalse(settings.equals(someRandomObject));
	}
	
	@Test
	void getSettingsTest() {
		Assertions.assertTrue(settings.getSetting(SettingsField.DISH_MENU_FOLDER) == null);
		settings.addSetting(SettingsField.DISH_MENU_FOLDER, null);
		Assertions.assertTrue(settings.getSetting(SettingsField.DISH_MENU_FOLDER) == null);
		settings.addSetting(SettingsField.DISH_MENU_FOLDER, settings.getPlaceholder());
		Assertions.assertTrue(settings.getSetting(SettingsField.DISH_MENU_FOLDER) == null);
		
		String someVal = "someVal";
		settings.addSetting(SettingsField.DISH_MENU_FOLDER, someVal);
		Assertions.assertTrue(settings.getSetting(SettingsField.DISH_MENU_FOLDER).equals(someVal));
	}
	
	@Test
	void isEmptyTest() {
		Assertions.assertTrue(settings.isEmpty());
		settings.initAllSettingsFields();
		Assertions.assertTrue(settings.isEmpty());
		
		String someVal = "someVal";
		settings.addSetting(SettingsField.DISH_MENU_FOLDER, someVal);
		Assertions.assertTrue(settings.getSetting(SettingsField.DISH_MENU_FOLDER).equals(someVal));
		Assertions.assertFalse(settings.isEmpty());
	}
}
