package test.model.settings;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.IModel;
import model.Model;
import model.dish.IDishMenuData;
import model.dish.serialise.FileDishMenuSerialiser;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.dish.serialise.IDishMenuParser;
import model.dish.serialise.IDishMenuSerialiser;
import model.filemanager.FileManager;
import model.filemanager.IFileManager;
import model.filemanager.SettingsFile;
import model.filemanager.StandardSettingsFile;
import model.filewriter.DishMenuFile;
import model.filewriter.FileAccess;
import model.filewriter.FileDishMenuItemSerialiser;
import model.filewriter.StandardDishMenuFile;
import model.settings.HasSettingsField;
import model.settings.ISettings;
import model.settings.ISettingsParser;
import model.settings.ISettingsSerialiser;
import model.settings.Settings;
import model.settings.SettingsField;
import model.settings.StandardSettingsParser;
import model.settings.StandardSettingsSerialiser;
import test.GeneralTestUtilityClass;

class FileManagerPreloadTest {
	private SettingsFile sf;
	private DishMenuFile dmf;
	
	private ISettings settings;
	private ISettingsSerialiser settingSerialiser;
	private ISettingsParser settingsParser;
	
	private IModel model;
	private Collection<HasSettingsField> part;
	private IFileManager fm;
	
	private static IDishMenuItemSerialiser menuItemSerialiser;
	private static IDishMenuParser menuParser;
	private static IDishMenuSerialiser menuSerialiser;
	
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private String i1id = "item1";
	
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private String i2id = "item2";
	
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private String i3id = "item3";
	
	private IDishMenuData dishMenuData;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private String settingsFileNAE = SettingsFile.getDefaultFileNameForClass()+FileAccess.getExtensionForClass();

	private ISettings initSettings() {
		ISettings settings = new Settings();
		settings.addSetting(SettingsField.DISH_MENU_FOLDER, this.testFolderAddress);
		settings.addSetting(SettingsField.ORDER_FOLDER, this.testFolderAddress);
		return settings;
	}
	
	private void fillSettingsFile() {
		settings = this.initSettings();
		sf.writeToFile(settingSerialiser.serialise(settings));
	}
	
	private void fillMenuFile() {
		dmf.writeToFile(menuSerialiser.serialise(dishMenuData));
	}
	
	private void fillMenu() {
		dmf = new StandardDishMenuFile(this.testFolderAddress);
		this.fillMenuFile();
	}
	
	@BeforeEach
	void prep() {
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
		model = new Model();
		menuItemSerialiser = model.getDishMenuItemSerialiser();
		model.addMenuItem(menuItemSerialiser.serialise(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(menuItemSerialiser.serialise(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(menuItemSerialiser.serialise(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		dishMenuData = model.getMenuData();
		model = new Model();
		menuItemSerialiser = model.getDishMenuItemSerialiser();
		menuSerialiser = GeneralTestUtilityClass.getPrivateFieldValue((Model) model, "fileMenuSerialiser");
		menuParser = GeneralTestUtilityClass.getPrivateFieldValue((Model) model, "dishMenuParser");
		
		fm = new FileManager(model, this.testFolderAddress);
//		fm = GeneralTestUtilityClass.getPrivateFieldValue(model, "fileManager");
		part = GeneralTestUtilityClass.getPrivateFieldValue(model, "part");
		part.clear();
		part.add(fm);
		sf = new StandardSettingsFile(this.testFolderAddress);
		settingSerialiser = new StandardSettingsSerialiser();
		settingsParser = new StandardSettingsParser();
		this.fillSettingsFile();
		this.fillMenu();
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
//		sf.deleteFile();
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
		settings = null;
	}

	@Test
	void preloadTest() {
		fm.loadSaved();
		Assertions.assertTrue(model.getSettings().equals(settings));
		String menuFileContent = dmf.readFile();
		Assertions.assertTrue(model.getMenuData().equals(menuParser.parseDishMenuData(menuFileContent)));
	}
	
	@Test
	void writeSettingsTest() {
		sf.deleteFile();
		fm.writeSettings(settingSerialiser.serialise(settings));
		fm.loadSaved();
		Assertions.assertTrue(settings.equals(settingsParser.parseSettings(sf.readFile())));
	}
}