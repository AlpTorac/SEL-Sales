package test.model.settings;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.connectivity.FileDeviceDataParser;
import model.connectivity.IDeviceData;
import model.dish.DishMenuData;
import model.dish.DishMenuItemData;
import model.filewriter.IFileAccess;
import model.filewriter.StandardFileAccess;
import model.order.OrderData;
import model.settings.ISettings;
import model.settings.ISettingsParser;
import model.settings.ISettingsSerialiser;
import model.settings.Settings;
import model.settings.SettingsField;
import model.settings.StandardSettingsParser;
import model.settings.StandardSettingsSerialiser;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import test.FXTestTemplate;

class FileManagerPreloadTest extends FXTestTemplate {
	private IFileAccess sf;
	private IFileAccess dmf;
	private IFileAccess of;
	private IFileAccess cdf;
	
	private ISettings settings;
	private ISettingsSerialiser settingSerialiser;
	private ISettingsParser settingsParser;
	
	private IServerModel model;
	
	private DishMenuData dishMenuData;

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
		dmf.writeToFile(model.serialiseMenuData());
	}
	
	private void fillMenu() {
		this.fillMenuFile();
	}
	
	private void addMenuToModel() {
		this.addDishMenuToServerModel(model);
	}
	
	@BeforeEach
	void prep() {
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
//		model = new ServerModel();
//		this.addMenuToModel();
//		dishMenuData = model.getMenuData();
//		model = new ServerModel();
//		
//		fm = new FileManager(model, this.testFolderAddress);
//		part = GeneralTestUtilityClass.getPrivateFieldValue(model, "part");
//		part.clear();
//		part.add(fm);
		
		model = new ServerModel(this.testFolderAddress) {
			@Override
			protected void externalStatusChanged(Runnable afterDiscoveryAction) {
				afterDiscoveryAction.run();
			}
		};
		this.addMenuToModel();
		dishMenuData = model.getMenuData();
		
		dmf = new StandardFileAccess(this.testFolderAddress, "menu");
		sf = new StandardFileAccess(this.testFolderAddress, "settings");
		of = new StandardFileAccess(this.testFolderAddress, "orders");
		cdf = new StandardFileAccess(this.testFolderAddress, "knownDevices");
		settingSerialiser = new StandardSettingsSerialiser();
		settingsParser = new StandardSettingsParser();
		this.fillSettingsFile();
		this.fillMenu();
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
		sf.close();
		dmf.close();
		of.close();
		cdf.close();
		GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
		settings = null;
	}

	@Test
	void preloadTest() {
		model.writeDishMenu();
		model.close();
		model = this.initServerModel();
		model.loadSaved();
		Assertions.assertTrue(model.getSettings().equals(settings));
		String menuFileContent = dmf.readFile();
		DishMenuItemData[] modelItems = model.getMenuData().getAllElements().toArray(DishMenuItemData[]::new);
		DishMenuItemData[] parsedItems = menuDAO.parseValueObjects(menuFileContent).toArray(DishMenuItemData[]::new);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(modelItems, parsedItems));
	}
	
	@Test
	void writeSettingsTest() {
		sf.deleteFile();
//		fm.writeSettings(settingSerialiser.serialise(settings));
//		fm.loadSaved();
		model.setSettings(settingSerialiser.serialise(settings));
		model.writeSettings();
		model.setSettings(new Settings());
		model.loadSaved();
		Assertions.assertTrue(settings.equals(settingsParser.parseSettings(sf.readFile())));
	}
	
	@Test
	void loadExistingDishMenuTest() {
		DishMenuData pastMenu = model.getMenuData();
		model.writeDishMenu();
		model.close();
		model = this.initServerModel();
		model.loadSaved();
		DishMenuItemData[] modelItems = model.getMenuData().getAllElements().toArray(DishMenuItemData[]::new);
		DishMenuItemData[] parsedItems = pastMenu.getAllElements().toArray(DishMenuItemData[]::new);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(modelItems, parsedItems));
	}
	
	@Test
	void loadExistingOrdersTest() {
		this.addDishMenuToServerModel(model);
		this.initOrders(model);
		this.addOrdersToModel(model);
		model.writeOrders();
		model.close();
		BufferedReader r = null;
		try {
			r = new BufferedReader(new FileReader(new File(this.testFolderAddress+File.separator+"orders.txt")));
		} catch (FileNotFoundException e) {
			this.cleanTestFolder();
			fail();
		}
		String readContent = r.lines().reduce("", (l1,l2)->l1+System.lineSeparator()+l2);
		
		model = this.initServerModel();
		model.loadSaved();
		OrderData[] readOrderData = model.getAllWrittenOrders();
		OrderData[] expectedOrderData = orderDAO.parseValueObjects(readContent).toArray(OrderData[]::new);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(readOrderData, expectedOrderData));
	}
	
	@Test
	void loadExistingKnownDevicesTest() {
		String fileAddress = testFolderAddress+File.separator+"testDeviceFile.txt";
		File f = new File(fileAddress);
		Assertions.assertFalse(f.length() > 0);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedWriter w = null;
		
		try {
			w = new BufferedWriter(new FileWriter(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileContent = 
				"c1n,c1a,1;" + System.lineSeparator()
				+ "c2n,c2a,0;" + System.lineSeparator();
		try {
			w.write(fileContent);
			w.flush();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		fm.loadKnownDevices(fileAddress);
		model.loadKnownDevices(fileAddress);
		IDeviceData[] readDeviceData = model.getAllKnownDeviceData();
		IDeviceData[] expectedDeviceData = (new FileDeviceDataParser()).parseDeviceDatas(fileContent);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(readDeviceData, expectedDeviceData));
	}
}
