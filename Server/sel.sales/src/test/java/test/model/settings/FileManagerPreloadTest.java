package test.model.settings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.connectivity.FileDeviceDataParser;
import model.connectivity.IDeviceData;
import model.dish.IDishMenuData;
import model.filemanager.FileManager;
import model.filemanager.IFileManager;
import model.filemanager.SettingsFile;
import model.filemanager.StandardSettingsFile;
import model.filewriter.DeviceDataFile;
import model.filewriter.DishMenuFile;
import model.filewriter.FileAccess;
import model.filewriter.OrderFile;
import model.filewriter.StandardDeviceDataFile;
import model.filewriter.StandardDishMenuFile;
import model.filewriter.StandardOrderFile;
import model.order.IOrderData;
import model.settings.HasSettingsField;
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

class FileManagerPreloadTest {
	private SettingsFile sf;
	private DishMenuFile dmf;
	private OrderFile of;
	private DeviceDataFile cdf;
	
	private ISettings settings;
	private ISettingsSerialiser settingSerialiser;
	private ISettingsParser settingsParser;
	
	private IServerModel model;
	private Collection<HasSettingsField> part;
//	private IFileManager fm;
	
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
		dmf.writeToFile(model.getDishMenuHelper().serialiseMenuForFile(dishMenuData));
	}
	
	private void fillMenu() {
		this.fillMenuFile();
	}
	
	private void addMenuToModel() {
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
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
		
		model = new ServerModel(this.testFolderAddress);
		this.addMenuToModel();
		dishMenuData = model.getMenuData();
		
		dmf = new StandardDishMenuFile(this.testFolderAddress);
		sf = new StandardSettingsFile(this.testFolderAddress);
		of = new StandardOrderFile(this.testFolderAddress);
		cdf = new StandardDeviceDataFile(this.testFolderAddress);
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
		model.loadSaved();
//		fm.loadSaved();
		Assertions.assertTrue(model.getSettings().equals(settings));
		String menuFileContent = dmf.readFile();
		Assertions.assertTrue(model.getMenuData().equals(model.getDishMenuHelper().parseFileMenuData(menuFileContent)));
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
		String fileAddress = testFolderAddress+File.separator+"testMenuFile.txt";
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
				"aaa,item1,4.0,2.34,2.0;"+System.lineSeparator()+
				"bbb,item2,10.0,1.0,5.67;"+System.lineSeparator()+
				"ccc,item3,4.0,2.5,1.0;"+System.lineSeparator();
		try {
			w.write(fileContent);
			w.flush();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		fm.loadDishMenu(fileAddress);
		model.loadDishMenu(fileAddress);
		IDishMenuData readMenuData = model.getMenuData();
		IDishMenuData expectedMenuData = model.getDishMenuHelper().parseFileMenuData(fileContent);
		Assertions.assertTrue(expectedMenuData.equals(readMenuData));
	}
	
	@Test
	void loadExistingOrdersTest() {
		String fileAddress = testFolderAddress+File.separator+"testOrderFile.txt";
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
		String fileContent = "order1#20200820112233000#0#0#0:item1,2.0;" + System.lineSeparator()
				+ "order2#20200110235959153#1#0#0:item1,2.0;" + System.lineSeparator()
				+ "order2#20200110235959153#1#0#0:item2,3.0;" + System.lineSeparator()
				+ "order3#20201201000000999#1#1#0:item3,5.0;" + System.lineSeparator()
				+ "order4#20211201000000999#1#1#1:item1,2.0;" + System.lineSeparator()
				+ "order4#20211201000000999#1#1#1:item2,3.0;" + System.lineSeparator()
				+ "order4#20211201000000999#1#1#1:item3,5.0;" + System.lineSeparator();
		try {
			w.write(fileContent);
			w.flush();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.addMenuToModel();
//		fm.loadOrders(fileAddress);
		model.loadOrders(fileAddress);
		IOrderData[] readOrderData = model.getAllWrittenOrders();
		IOrderData[] expectedOrderData = model.getOrderHelper().deserialiseOrderDatas(fileContent);
		GeneralTestUtilityClass.arrayContentEquals(readOrderData, expectedOrderData);
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
		GeneralTestUtilityClass.arrayContentEquals(readDeviceData, expectedDeviceData);
	}
}
