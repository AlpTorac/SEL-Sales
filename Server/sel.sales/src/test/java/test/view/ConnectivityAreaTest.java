package test.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
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
import test.external.dummy.DummyExternal;
import test.external.dummy.DummyService;
import test.external.dummy.DummyServiceConnectionManager;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;
@Execution(value = ExecutionMode.SAME_THREAD)
class ConnectivityAreaTest extends ApplicationTest {

	private volatile boolean actionFinished = false;
	
	private IServerModel model;
	private IServerController controller;
	private StandardServerView view;
	private DummyExternal external;
	
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
			model = new ServerModel(this.testFolderAddress);
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
		});
	}
	
	@Test
	void discoveredDeviceTest() {
		runFXAction(()->{
			Assertions.assertTrue(opHelper.getDiscoveredDevices().size() == dcCol.size());
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getDiscoveredDevices().toArray(IDeviceData[]::new),
					dcdArray);
		});
	}
	
	@Test
	void knownDeviceTest() {
		runFXAction(()->{
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					null);
			
			opHelper.addKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {dcdArray[0]});
		});
		runFXAction(()->{
			opHelper.addKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					dcdArray);
		});
	}
	
	@Test
	void knownDeviceRemoveTest() {
		runFXAction(()->{
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					null);
			
			opHelper.addKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {dcdArray[0]});
		});
		runFXAction(()->{
			opHelper.addKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					dcdArray);
		});
		runFXAction(()->{
			opHelper.removeKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {dcdArray[1]});
		});
	}
	
	@Test
	void knownDeviceWithAllowTest() {
		runFXAction(()->{
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					null);
			
			opHelper.addKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {dcdArray[0]});
		});
		runFXAction(()->{
			opHelper.allowKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
							dcdArray[0].getDeviceAddress(), true, false)});
		});
		runFXAction(()->{
			opHelper.addKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
							dcdArray[0].getDeviceAddress(),	true, false),
							new DeviceData(dcdArray[1].getDeviceName(),
									dcdArray[1].getDeviceAddress(),	false, false)});
		});
	}
	
	@Test
	void knownDeviceAllowBlockTest() {
		runFXAction(()->{
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					null);
			
			opHelper.addKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {dcdArray[0]});
		});
		runFXAction(()->{
			opHelper.allowKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
							dcdArray[0].getDeviceAddress(), true, false)});
		});
		runFXAction(()->{
			opHelper.blockKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
							dcdArray[0].getDeviceAddress(), false, false)});
		});
		runFXAction(()->{
			opHelper.allowKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
							dcdArray[0].getDeviceAddress(), true, false)});
		});
		runFXAction(()->{
			opHelper.addKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
							dcdArray[0].getDeviceAddress(),	true, false),
							new DeviceData(dcdArray[1].getDeviceName(),
									dcdArray[1].getDeviceAddress(),	false, false)});
		});
	}
	
	@Test
	void knownDeviceConnectionTest() {
		runFXAction(()->{
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					null);
			
			opHelper.addKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {dcdArray[0]});
		});
		runFXAction(()->{
			opHelper.allowKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
							dcdArray[0].getDeviceAddress(), true, false)});
		});
		runFXAction(()->{
			dscm.setCurrentConnectionObject(dc1);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
							dcdArray[0].getDeviceAddress(), true, true)});
		});
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
		runFXAction(()->{
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					null);
			
			opHelper.addKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {dcdArray[0]});
		});
		runFXAction(()->{
			opHelper.addKnownDevice(1);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					dcdArray);
		});
		runFXAction(()->{
			opHelper.allowKnownDevice(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
							dcdArray[0].getDeviceAddress(), true, false), dcdArray[1]});
		});
		runFXAction(()->{
			opHelper.allowKnownDevice(1);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
							dcdArray[0].getDeviceAddress(), true, false),
							new DeviceData(dcdArray[1].getDeviceName(),
									dcdArray[1].getDeviceAddress(), true, false)});
		});
		runFXAction(()->{
			dscm.setCurrentConnectionObject(getDeviceByAddress(dcdArray[0].getDeviceAddress()));
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
							dcdArray[0].getDeviceAddress(), true, true), new DeviceData(dcdArray[1].getDeviceName(),
									dcdArray[1].getDeviceAddress(), true, false)});
		});
		runFXAction(()->{
//			dscm.getConnectionManagers().forEach(cm -> {
//				if (cm.getConnection().getTargetDeviceAddress().equals(dcdArray[0].getDeviceAddress())) {
//					DisconnectionListener dl = new DisconnectionListener(controller);
//					cm.getPingPong().setDisconnectionListener(dl);
//					dl.connectionLost(dcdArray[0].getDeviceAddress());
//				}
//			});
			// wait for the ping pong to timeout and report the disconnection
			GeneralTestUtilityClass.performWait(DummyServiceConnectionManager.ESTIMATED_PP_TIMEOUT);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownDevices().toArray(IDeviceData[]::new),
					new IDeviceData[] {new DeviceData(dcdArray[0].getDeviceName(),
							dcdArray[0].getDeviceAddress(), true, false), new DeviceData(dcdArray[1].getDeviceName(),
									dcdArray[1].getDeviceAddress(), true, false)});
		});
	}
}
