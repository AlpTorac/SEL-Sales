package test.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.controller.IServerController;
import server.model.IServerModel;
import server.view.StandardServerView;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;
import test.model.order.ClientSimulant;

//@Execution(value = ExecutionMode.SAME_THREAD)
class UnconfirmedOrderTest extends FXTestTemplate {
	private IServerModel model;
	private IServerController controller;
	private StandardServerView view;
	
	private ExecutorService es;
	
	@BeforeEach
	public void prep() {
		runFXAction(()->{
			es = this.initExecutorService();
			model = this.initServerModel();
			controller = this.initServerController(model);
			view = this.initServerView(model, controller);
			view.startUp();
			this.addDishMenuToServerModel(model);
			this.initOrders(model);
			this.setServerOpHelper(model, controller, view);
		});
	}
	
	@AfterEach
	public void cleanUp() {
		this.closeExecutorService(es);
		this.closeModel(model);
	}
	
	@Test
	void duplicateOrderTest() {
		model.addOrder(oData1);
		model.addOrder(oData1);
		model.addOrder(oData1);
		model.addOrder(oData1);
		model.addOrder(oData1);
		model.addOrder(oData1);
		model.addOrder(oData1);
		model.addOrder(oData1);
		model.addOrder(oData1);
		model.addOrder(oData1);
		
		Assertions.assertEquals(model.getAllUnconfirmedOrders().length, 1);
		Assertions.assertEquals(serverOpHelper.getUnconfirmedOrders().size(), 1);
	}

	@Test
	void duplicateAsynchroneOrderTest() {
		ClientSimulant c1 = new ClientSimulant(oData1, model);
		ClientSimulant c2 = new ClientSimulant(oData1, model);
		ClientSimulant c3 = new ClientSimulant(oData1, model);
		ClientSimulant c4 = new ClientSimulant(oData1, model);
		ClientSimulant c5 = new ClientSimulant(oData1, model);
		ClientSimulant c6 = new ClientSimulant(oData2, model);
		ClientSimulant c7 = new ClientSimulant(oData3, model);
		ClientSimulant c8 = new ClientSimulant(oData3, model);
		ClientSimulant c9 = new ClientSimulant(oData2, model);
		ClientSimulant c10 = new ClientSimulant(oData2, model);
		
		Collection<ClientSimulant> cs = new ArrayList<ClientSimulant>();
		cs.add(c1);
		cs.add(c2);
		cs.add(c3);
		cs.add(c4);
		cs.add(c5);
		cs.add(c6);
		cs.add(c7);
		cs.add(c8);
		cs.add(c9);
		cs.add(c10);
		
		for (ClientSimulant c : cs) {
			es.submit(c);
		}
		
		GeneralTestUtilityClass.performWait(300);
		Assertions.assertEquals(model.getAllUnconfirmedOrders().length, 3);
		
		GeneralTestUtilityClass.performWait(300);
		Assertions.assertEquals(serverOpHelper.getUnconfirmedOrders().size(), 3);
	}
	
}
