package test.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import external.External;
import external.device.IDevice;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.connectivity.DeviceData;
import model.connectivity.IDeviceData;
import model.filemanager.FileManager;
import model.filemanager.IFileManager;
import model.settings.HasSettingsField;
import model.settings.ISettings;
import model.settings.Settings;
import model.settings.SettingsField;
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.model.IServerModel;
import server.model.ServerModel;
import server.view.StandardServerView;
import test.GeneralTestUtilityClass;
import test.StandardServerViewOperationsUtilityClass;
import test.external.dummy.DummyDevice;
import test.external.dummy.DummyDeviceDiscoveryStrategy;
import test.external.dummy.DummyExternal;
import test.external.dummy.DummyService;
import test.external.dummy.DummyServiceConnectionManager;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;

class SettingsAreaTest extends ApplicationTest {

	private volatile boolean actionFinished = false;
	
	private IServerModel model;
	private IServerController controller;
	private StandardServerView view;
	private DummyExternal external;
	
	private StandardServerViewOperationsUtilityClass opHelper;
	
	private DummyService service;
	private DummyServiceConnectionManager dscm;
	
	private DummyDeviceDiscoveryStrategy cds;
	
	private DummyDevice dc1;
	private final String dc1n = "dc1n";
	private final String dc1a = "dc1a";
	
	private DummyDevice dc2;
	private final String dc2n = "dc2n";
	private final String dc2a = "dc2a";
	
	private Collection<IDevice> dcCol;
	private IDeviceData[] dcdArray;
	
	private String menuFolderAddress = "mfa";
	private String orderFolderAddress = "ofa";
	private String ppTimeout = "200";
	private String ppMinimalDelay = "100";
	private String ppResendLimit = "3";
	private String sendTimeout = "1000";
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
//	private Collection<HasSettingsField> part;
//	private IFileManager fm;
	
	private void initDevices() {
//		dc1n = "dc1n";
//		dc1a = "dc1a";
		dc1 = new DummyDevice(dc1n, dc1a);
		
//		dc2n = "dc2n";
//		dc2a = "dc2a";
		dc2 = new DummyDevice(dc2n, dc2a);
		
		dcCol = new ArrayList<IDevice>();
		dcCol.add(dc1);
		dcCol.add(dc2);
	}
	
	private void initDiscoveryStrategy() {
		cds = new DummyDeviceDiscoveryStrategy();
		cds.setDiscoveredDevices(dcCol);
		dcdArray = dcCol.stream()
				.map(c -> {return new DeviceData(c.getDeviceName(), c.getDeviceAddress(), false, false);})
				.toArray(IDeviceData[]::new);
		this.external.setDiscoveryStrategy(cds);
	}
	
	private void initialDeviceDiscovery() {
		this.initDevices();
		this.initDiscoveryStrategy();
	}
	
	private void waitForAction() {
		while (!actionFinished) {
			
		}
		actionFinished = false;
	}
	
	private void runFXAction(Runnable run) {
		Platform.runLater(() -> {
			run.run();
			actionFinished = true;
		});
		waitForAction();
	}
	
	@Override
	public void start(Stage stage) {
		
	}
	
	@BeforeEach
	void prep() {
		runFXAction(()->{
			GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
			model = new ServerModel(this.testFolderAddress);
			
//			fm = new FileManager(model, this.testFolderAddress);
//			fm = GeneralTestUtilityClass.getPrivateFieldValue(model, "fileManager");
//			part = GeneralTestUtilityClass.getPrivateFieldValue(model, "part");
//			part.clear();
//			part.add(fm);
			
			controller = new StandardServerController(model);
			view = new StandardServerView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), controller, model);
			external = new DummyExternal("id", "name", controller, model, 10000, 1000, 2000, 5);
			service = GeneralTestUtilityClass.getPrivateFieldValue((External) external, "service");
			dscm = (DummyServiceConnectionManager) service.getServiceConnectionManager();
			view.startUp();
			view.show();
			this.initialDeviceDiscovery();
			opHelper = new StandardServerViewOperationsUtilityClass(view, controller, model);
			opHelper.clickOnDiscoverDevices();
		});
	}
	
	@AfterEach
	void cleanUp() {
		runFXAction(() -> {
			view.hide();
			model.close();
			GeneralTestUtilityClass.deletePathContent(new File(this.testFolderAddress));
		});
	}
	
	@Test
	void changeSettingsTest() {
		ISettings s = new Settings();
		runFXAction(()->{
			opHelper.inputMenuFolderAddress(menuFolderAddress);
			s.addSetting(SettingsField.DISH_MENU_FOLDER, menuFolderAddress);
		});
		runFXAction(()->{
			opHelper.inputOrderFolderAddress(orderFolderAddress);
			s.addSetting(SettingsField.ORDER_FOLDER, orderFolderAddress);
		});
		runFXAction(()->{
			opHelper.inputPPTimeout(ppTimeout);
			s.addSetting(SettingsField.PING_PONG_TIMEOUT, ppTimeout);
		});
		runFXAction(()->{
			opHelper.inputPPMinimalDelay(ppMinimalDelay);
			s.addSetting(SettingsField.PING_PONG_MINIMAL_DELAY, ppMinimalDelay);
		});
		runFXAction(()->{
			opHelper.inputPPResendLimit(ppResendLimit);
			s.addSetting(SettingsField.PING_PONG_RESEND_LIMIT, ppResendLimit);
		});
		runFXAction(()->{
			opHelper.inputSendTimeout(sendTimeout);
			s.addSetting(SettingsField.SEND_TIMEOUT, sendTimeout);
		});
		runFXAction(()->{
			opHelper.applySettings();
		});
		ISettings modelSettings = model.getSettings();
		Assertions.assertTrue(s.equals(modelSettings));
		
		Assertions.assertEquals(dscm.getSendTimeoutInMillis(), Long.valueOf(s.getSetting(SettingsField.SEND_TIMEOUT)));
		Assertions.assertEquals(dscm.getPingPongTimeoutInMillis(), Long.valueOf(s.getSetting(SettingsField.PING_PONG_TIMEOUT)));
		Assertions.assertEquals(dscm.getPingPongResendLimit(), Integer.valueOf(s.getSetting(SettingsField.PING_PONG_RESEND_LIMIT)));
		Assertions.assertEquals(dscm.getMinimalPingPongDelay(), Long.valueOf(s.getSetting(SettingsField.PING_PONG_MINIMAL_DELAY)));
	}
}
