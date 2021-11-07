package test.view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import controller.IController;
import controller.MainController;
import external.External;
import external.client.IClient;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.IModel;
import model.Model;
import model.connectivity.ClientData;
import model.connectivity.IClientData;
import model.filemanager.FileManager;
import model.filemanager.IFileManager;
import model.settings.HasSettingsField;
import model.settings.ISettings;
import model.settings.Settings;
import model.settings.SettingsField;
import test.GeneralTestUtilityClass;
import test.MainViewOperationsUtilityClass;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyClientDiscoveryStrategy;
import test.external.dummy.DummyExternal;
import test.external.dummy.DummyService;
import test.external.dummy.DummyServiceConnectionManager;
import view.MainView;
import view.repository.HasText;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;

class SettingsAreaTest extends ApplicationTest {

	private volatile boolean actionFinished = false;
	
	private IModel model;
	private IController controller;
	private MainView view;
	private DummyExternal external;
	
	private MainViewOperationsUtilityClass opHelper;
	
	private DummyService service;
	private DummyServiceConnectionManager dscm;
	
	private DummyClientDiscoveryStrategy cds;
	
	private DummyClient dc1;
	private final String dc1n = "dc1n";
	private final String dc1a = "dc1a";
	
	private DummyClient dc2;
	private final String dc2n = "dc2n";
	private final String dc2a = "dc2a";
	
	private Collection<IClient> dcCol;
	private IClientData[] dcdArray;
	
	private String menuFolderAddress = "mfa";
	private String orderFolderAddress = "ofa";
	private String ppTimeout = "200";
	private String ppMinimalDelay = "100";
	private String ppResendLimit = "3";
	private String sendTimeout = "1000";
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	private Collection<HasSettingsField> part;
	private IFileManager fm;
	
	private void initClients() {
//		dc1n = "dc1n";
//		dc1a = "dc1a";
		dc1 = new DummyClient(dc1n, dc1a);
		
//		dc2n = "dc2n";
//		dc2a = "dc2a";
		dc2 = new DummyClient(dc2n, dc2a);
		
		dcCol = new ArrayList<IClient>();
		dcCol.add(dc1);
		dcCol.add(dc2);
	}
	
	private void initDiscoveryStrategy() {
		cds = new DummyClientDiscoveryStrategy();
		cds.setDiscoveredClients(dcCol);
		dcdArray = dcCol.stream()
				.map(c -> {return new ClientData(c.getClientName(), c.getClientAddress(), false, false);})
				.toArray(IClientData[]::new);
		this.external.setDiscoveryStrategy(cds);
	}
	
	private void initialClientDiscovery() {
		this.initClients();
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
			model = new Model();
			fm = new FileManager(model, this.testFolderAddress);
//			fm = GeneralTestUtilityClass.getPrivateFieldValue(model, "fileManager");
			part = GeneralTestUtilityClass.getPrivateFieldValue(model, "part");
			part.clear();
			part.add(fm);
			controller = new MainController(model);
			view = new MainView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), controller, model);
			external = new DummyExternal("id", "name", controller, model, 10000, 1000, 2000, 5);
			service = GeneralTestUtilityClass.getPrivateFieldValue((External) external, "service");
			dscm = (DummyServiceConnectionManager) service.getServiceConnectionManager();
			view.startUp();
			view.show();
			this.initialClientDiscovery();
			opHelper = new MainViewOperationsUtilityClass(view, controller, model);
			opHelper.clickOnDiscoverClients();
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
