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
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.model.IServerModel;
import server.model.ServerModel;
import server.view.StandardServerView;
import test.GeneralTestUtilityClass;
import test.StandardServerViewOperationsUtilityClass;
import test.ViewOperationsUtilityClass;
import test.external.dummy.DummyDevice;
import test.external.dummy.DummyDeviceDiscoveryStrategy;
import test.external.dummy.DummyServerExternal;
import test.external.dummy.DummyService;
import test.external.dummy.DummyServiceConnectionManager;
import view.repository.uifx.FXUIComponentFactory;

class ConnectivityAreaTest extends ApplicationTest {

	private volatile boolean actionFinished = false;
	
	private IServerModel model;
	private IServerController controller;
	private StandardServerView view;
	private DummyServerExternal external;
	
	private ViewOperationsUtilityClass opHelper;
	
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
	private IDeviceData[] kddArray;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
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
		dcdArray = cds.discoverDevices().stream()
				.map(c -> {return new DeviceData(c.getDeviceName(), c.getDeviceAddress(), false, false);})
				.toArray(IDeviceData[]::new);
		kddArray = cds.discoverDevices().stream()
				.map(c -> {return new DeviceData(c.getDeviceName(), c.getDeviceAddress(), true, false);})
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
			model = new ServerModel(this.testFolderAddress);
			controller = new StandardServerController(model);
			view = new StandardServerView(new FXUIComponentFactory(), controller, model);
			external = new DummyServerExternal("id", "name", controller, model, 200, 100, 2000, 3);
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
		});
	}
	
	@Test
	void discoveredDeviceTest() {
		runFXAction(()->{
			Assertions.assertTrue(opHelper.getDiscoveredDevices().size() == dcCol.size());
			Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(opHelper.getDiscoveredDevices().toArray(IDeviceData[]::new),
					dcdArray));
		});
	}
	
	@Test
	void knownDeviceTest() {
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
				new IDeviceData[] {}));
		runFXAction(()->{opHelper.addKnownDevice(0);});
		IDeviceData[] ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 1);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(kddArray, ddarr[0]));
		runFXAction(()->{opHelper.addKnownDevice(1);});
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 2);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(ddarr, kddArray));
	}
	
	@Test
	void knownDeviceRemoveTest() {
		this.knownDeviceTest();
		runFXAction(()->{opHelper.removeKnownDevice(0);});
		IDeviceData[] kddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(kddarr.length, 1);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(kddArray, kddarr[0]));
	}
	
	@Test
	void knownDeviceWithAllowTest() {
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
				new IDeviceData[] {}));
		runFXAction(()->{opHelper.addKnownDevice(0);});
		IDeviceData[] ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 1);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(kddArray, ddarr[0]));
		runFXAction(()->{opHelper.allowKnownDevice(0);});
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 1);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(kddArray, ddarr[0]));
		runFXAction(()->{opHelper.addKnownDevice(1);});
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 2);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(kddArray, ddarr));
//		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
//				new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
//						dcdArray[0].getDeviceAddress(),	true, false),
//						new DeviceData(dcdArray[1].getDeviceName(),
//								dcdArray[1].getDeviceAddress(),	false, false)}));
	}
	
	@Test
	void knownDeviceAllowBlockTest() {
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
				new IDeviceData[] {}));
		runFXAction(()->{opHelper.addKnownDevice(0);});
		IDeviceData[] ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 1);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(kddArray, ddarr[0]));
		runFXAction(()->{opHelper.allowKnownDevice(0);});
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 1);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(kddArray, ddarr[0]));
		runFXAction(()->{opHelper.blockKnownDevice(0);});
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 1);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(dcdArray, ddarr[0]));
		runFXAction(()->{opHelper.allowKnownDevice(0);});
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 1);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(kddArray, ddarr[0]));
		runFXAction(()->{opHelper.addKnownDevice(1);});
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 2);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(kddArray, ddarr));
	}
	
	@Test
	void knownDeviceConnectionTest() {
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
				new IDeviceData[] {}));
		runFXAction(()->{opHelper.addKnownDevice(0);});
		IDeviceData[] ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 1);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(kddArray, ddarr[0]));
//		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
//				new IDeviceData[] {dcdArray[0]}));
		runFXAction(()->{opHelper.allowKnownDevice(0);});
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 1);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(kddArray, ddarr[0]));
//		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
//				new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
//						dcdArray[0].getDeviceAddress(), true, false)}));
		dscm.setCurrentConnectionObject(this.getDeviceByAddress(ddarr[0].getDeviceAddress()));
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 1);
		ddarr[0].equals(new DeviceData(ddarr[0].getDeviceName(),
				ddarr[0].getDeviceAddress(), true, true));
//		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(ddarr,
//				new IDeviceData[] {new DeviceData(ddarr[0].getDeviceName(),
//						ddarr[0].getDeviceAddress(), true, true)}));
	}
	
	private DummyDevice getDeviceByAddress(String DeviceAddress) {
		DummyDevice Device = null;
		switch (DeviceAddress) {
		case dc1a: Device = dc1; break;
		case dc2a: Device = dc2; break;
		}
		return Device;
	}
	
	@Test
	void knownDeviceDisconnectionTest() {
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
				new IDeviceData[] {}));
		runFXAction(()->{opHelper.addKnownDevice(0);});
		IDeviceData[] ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 1);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(kddArray, ddarr[0]));
		runFXAction(()->{opHelper.addKnownDevice(1);});
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 2);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(ddarr, kddArray));
//		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
//				new IDeviceData[] {}));
//		runFXAction(()->{opHelper.addKnownDevice(0);});
//		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
//				new IDeviceData[] {dcdArray[0]}));
//		runFXAction(()->{opHelper.addKnownDevice(1);});
//		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
//				dcdArray));
		runFXAction(()->{opHelper.allowKnownDevice(0);});
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 2);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(kddArray, ddarr));
		
		runFXAction(()->{opHelper.allowKnownDevice(1);});
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 2);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(kddArray, ddarr));
		
		dscm.setCurrentConnectionObject(getDeviceByAddress(ddarr[0].getDeviceAddress()));
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 2);
		
		GeneralTestUtilityClass.performWait(100);
		
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		
		Assertions.assertTrue(ddarr[0].getIsAllowedToConnect());
		Assertions.assertTrue(ddarr[0].getIsConnected());
		
		Assertions.assertTrue(ddarr[1].getIsAllowedToConnect());
		Assertions.assertFalse(ddarr[1].getIsConnected());		
		
		// wait for the ping pong to timeout and report the disconnection
//		GeneralTestUtilityClass.performWait(DummyServiceConnectionManager.ESTIMATED_PP_TIMEOUT);
		
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		Assertions.assertEquals(ddarr.length, 2);
		
		GeneralTestUtilityClass.performWait(this.dscm.getEstimatedPPCloseTime());
		
		ddarr = opHelper.getKnownDevices().toArray(IDeviceData[]::new);
		
		Assertions.assertTrue(ddarr[0].getIsAllowedToConnect());
		Assertions.assertFalse(ddarr[0].getIsConnected());
		
		Assertions.assertTrue(ddarr[1].getIsAllowedToConnect());
		Assertions.assertFalse(ddarr[1].getIsConnected());		
	}
}
