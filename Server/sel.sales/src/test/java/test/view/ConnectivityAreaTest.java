package test.view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testfx.framework.junit5.ApplicationTest;

import controller.IController;
import controller.MainController;
import controller.StatusEvent;
import external.External;
import external.client.IClient;
import external.connection.DisconnectionListener;
import external.connection.pingpong.IPingPong;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.IModel;
import model.Model;
import model.connectivity.ClientData;
import model.connectivity.IClientData;
import test.GeneralTestUtilityClass;
import test.MainViewOperationsUtilityClass;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyClientDiscoveryStrategy;
import test.external.dummy.DummyExternal;
import test.external.dummy.DummyService;
import test.external.dummy.DummyServiceConnectionManager;
import view.MainView;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;
@Execution(value = ExecutionMode.SAME_THREAD)
class ConnectivityAreaTest extends ApplicationTest {

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
			model = new Model();
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
		});
	}
	
	@Test
	void discoveredClientTest() {
		runFXAction(()->{
			Assertions.assertTrue(opHelper.getDiscoveredClients().size() == dcCol.size());
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getDiscoveredClients().toArray(IClientData[]::new),
					dcdArray);
		});
	}
	
	@Test
	void knownClientTest() {
		runFXAction(()->{
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					null);
			
			opHelper.addKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {dcdArray[0]});
		});
		runFXAction(()->{
			opHelper.addKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					dcdArray);
		});
	}
	
	@Test
	void knownClientRemoveTest() {
		runFXAction(()->{
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					null);
			
			opHelper.addKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {dcdArray[0]});
		});
		runFXAction(()->{
			opHelper.addKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					dcdArray);
		});
		runFXAction(()->{
			opHelper.removeKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {dcdArray[1]});
		});
	}
	
	@Test
	void knownClientWithAllowTest() {
		runFXAction(()->{
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					null);
			
			opHelper.addKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {dcdArray[0]});
		});
		runFXAction(()->{
			opHelper.allowKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {new ClientData(dcdArray[0].getClientName(),
							dcdArray[0].getClientAddress(), true, false)});
		});
		runFXAction(()->{
			opHelper.addKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {new ClientData(dcdArray[0].getClientName(),
							dcdArray[0].getClientAddress(),	true, false),
							new ClientData(dcdArray[1].getClientName(),
									dcdArray[1].getClientAddress(),	false, false)});
		});
	}
	
	@Test
	void knownClientAllowBlockTest() {
		runFXAction(()->{
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					null);
			
			opHelper.addKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {dcdArray[0]});
		});
		runFXAction(()->{
			opHelper.allowKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {new ClientData(dcdArray[0].getClientName(),
							dcdArray[0].getClientAddress(), true, false)});
		});
		runFXAction(()->{
			opHelper.blockKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {new ClientData(dcdArray[0].getClientName(),
							dcdArray[0].getClientAddress(), false, false)});
		});
		runFXAction(()->{
			opHelper.allowKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {new ClientData(dcdArray[0].getClientName(),
							dcdArray[0].getClientAddress(), true, false)});
		});
		runFXAction(()->{
			opHelper.addKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {new ClientData(dcdArray[0].getClientName(),
							dcdArray[0].getClientAddress(),	true, false),
							new ClientData(dcdArray[1].getClientName(),
									dcdArray[1].getClientAddress(),	false, false)});
		});
	}
	
	@Test
	void knownClientConnectionTest() {
		runFXAction(()->{
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					null);
			
			opHelper.addKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {dcdArray[0]});
		});
		runFXAction(()->{
			opHelper.allowKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {new ClientData(dcdArray[0].getClientName(),
							dcdArray[0].getClientAddress(), true, false)});
		});
		runFXAction(()->{
			dscm.setCurrentConnectionObject(dc1);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {new ClientData(dcdArray[0].getClientName(),
							dcdArray[0].getClientAddress(), true, true)});
		});
	}
	
	private DummyClient getClientByAddress(String clientAddress) {
		DummyClient client = null;
		switch (clientAddress) {
		case dc1a: client = dc1; break;
		case dc2a: client = dc2; break;
		}
		return client;
	}
	
	@Test
	void knownClientDisconnectionTest() {
		runFXAction(()->{
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					null);
			
			opHelper.addKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {dcdArray[0]});
		});
		runFXAction(()->{
			opHelper.addKnownClient(1);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					dcdArray);
		});
		runFXAction(()->{
			opHelper.allowKnownClient(0);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {new ClientData(dcdArray[0].getClientName(),
							dcdArray[0].getClientAddress(), true, false), dcdArray[1]});
		});
		runFXAction(()->{
			opHelper.allowKnownClient(1);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {new ClientData(dcdArray[0].getClientName(),
							dcdArray[0].getClientAddress(), true, false),
							new ClientData(dcdArray[1].getClientName(),
									dcdArray[1].getClientAddress(), true, false)});
		});
		runFXAction(()->{
			dscm.setCurrentConnectionObject(getClientByAddress(dcdArray[0].getClientAddress()));
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {new ClientData(dcdArray[0].getClientName(),
							dcdArray[0].getClientAddress(), true, true), new ClientData(dcdArray[1].getClientName(),
									dcdArray[1].getClientAddress(), true, false)});
		});
		runFXAction(()->{
//			dscm.getConnectionManagers().forEach(cm -> {
//				if (cm.getConnection().getTargetClientAddress().equals(dcdArray[0].getClientAddress())) {
//					DisconnectionListener dl = new DisconnectionListener(controller);
//					cm.getPingPong().setDisconnectionListener(dl);
//					dl.connectionLost(dcdArray[0].getClientAddress());
//				}
//			});
			// wait for the ping pong to timeout and report the disconnection
			GeneralTestUtilityClass.performWait(DummyServiceConnectionManager.ESTIMATED_PP_TIMEOUT);
			GeneralTestUtilityClass.arrayContentEquals(opHelper.getKnownClients().toArray(IClientData[]::new),
					new IClientData[] {new ClientData(dcdArray[0].getClientName(),
							dcdArray[0].getClientAddress(), true, false), new ClientData(dcdArray[1].getClientName(),
									dcdArray[1].getClientAddress(), true, false)});
		});
	}
}
