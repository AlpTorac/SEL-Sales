package test.model.fileaccess;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.filemanager.SettingsFile;
import model.filemanager.StandardSettingsFile;
import model.filewriter.FileAccess;
import model.settings.ISettings;
import model.settings.ISettingsParser;
import model.settings.ISettingsSerialiser;
import model.settings.Settings;
import model.settings.SettingsField;
import model.settings.StandardSettingsParser;
import model.settings.StandardSettingsSerialiser;
import test.GeneralTestUtilityClass;
//@Execution(value = ExecutionMode.SAME_THREAD)
class SettingsFileAccessTest {
	private SettingsFile sf;
	private ISettings settings;
	private ISettingsSerialiser serialiser;
	private ISettingsParser parser;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private String settingsFileNAE = SettingsFile.getDefaultFileNameForClass()+FileAccess.getExtensionForClass();
//	private String dishMenuFileNAE = DishMenuFile.getDefaultFileNameForClass()+FileAccess.getExtensionForClass();
//	private String orderFileNAE = OrderFile.getDefaultFileNameForClass()+FileAccess.getExtensionForClass();
	
//	private String getSettingsAddress() {
//		return this.testFolderAddress+File.separator+this.settingsFileNAE;
//	}
	
//	private String getMenuAddress() {
//		return this.testFolderAddress+File.separator+this.dishMenuFileNAE;
//	}
//	
//	private String getOrderAddress() {
//		return this.testFolderAddress+File.separator+this.orderFileNAE;
//	}
	
	private ISettings initSettings() {
		ISettings settings = new Settings();
		settings.addSetting(SettingsField.DISH_MENU_FOLDER, this.testFolderAddress);
		settings.addSetting(SettingsField.ORDER_FOLDER, this.testFolderAddress);
		return settings;
	}
	
	private void fillSettingsFile() {
		settings = this.initSettings();
		sf.writeToFile(serialiser.serialise(settings));
	}
	
	@BeforeEach
	void prep() {
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
		sf = new StandardSettingsFile(this.testFolderAddress);
		serialiser = new StandardSettingsSerialiser();
		parser = new StandardSettingsParser();
		this.fillSettingsFile();
	}
	
	@AfterEach
	void cleanUp() {
		sf.close();
		sf.deleteFile();
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
		settings = null;
	}
	
//	@Test
//	void addressTest() {
//		Assertions.assertEquals(this.getSettingsAddress(), sf.getFilePath());
//	}
	
	@Test
	void loadTest() {
		ISettings s = parser.parseSettings(sf.readFile());
		Assertions.assertTrue(settings.equals(s));
	}
	
//	@Test
//	void deleteTest() {
//		Assertions.assertTrue((new File(this.getSettingsAddress())).exists());
//		Assertions.assertTrue(sf.deleteFile());
//		Assertions.assertTrue(!(new File(this.getSettingsAddress())).exists());
//	}
	
//	@Test
//	void changeFolderAddressTest() {
//		String newFolderAddress = this.testFolderAddress+File.separator+"subfolder";
//		File folder = new File(newFolderAddress);
//		folder.mkdir();
//		Assertions.assertTrue(folder.exists() && folder.isDirectory());
//		sf.setFolderAddress(folder.getPath());
//		Assertions.assertEquals(sf.getFolderAddress(), newFolderAddress);
//	}
	
//	@Test
//	void migrationTest() {
//		String newFolderAddress = this.testFolderAddress+File.separator+"subfolder";
//		File folder = new File(newFolderAddress);
//		folder.mkdir();
//		Assertions.assertTrue(folder.exists() && folder.isDirectory());
//		sf.setFolderAddress(folder.getPath());
//		String newFileAddress = newFolderAddress+File.separator+this.settingsFileNAE;
//		Assertions.assertEquals(sf.getFilePath(), newFileAddress);
//		this.fillSettingsFile();
//		Assertions.assertTrue(settings.equals(sf.loadSettings()));
//		Assertions.assertTrue(sf.deleteFile());
//	}
}
