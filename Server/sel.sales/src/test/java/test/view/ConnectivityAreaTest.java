package test.view;

import static org.junit.jupiter.api.Assertions.*;

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
import external.client.IClient;
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
	
	private DummyClientDiscoveryStrategy cds;
	
	private DummyClient dc1;
	private String dc1n;
	private String dc1a;
	
	private DummyClient dc2;
	private String dc2n;
	private String dc2a;
	
	private Collection<IClient> dcCol;
	private IClientData[] dcdArray;
	
	private void initClients() {
		dc1n = "dc1n";
		dc1a = "dc1a";
		dc1 = new DummyClient(dc1n, dc1a);
		
		dc2n = "dc2n";
		dc2a = "dc2a";
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
}
