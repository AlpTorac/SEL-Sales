package test.model.order;

import java.util.concurrent.ExecutorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.IServerModel;
import test.GeneralTestUtilityClass;
import test.FXTestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderConcurrencyTest extends FXTestTemplate {
	private ExecutorService es;
	private IServerModel model;
	
	@BeforeEach
	void startUp() {
		model = this.initServerModel();
		es = this.initExecutorService();
		this.addDishMenuToServerModel(model);
		this.initOrders(model);
		this.addOrdersToModel(model);
	}
	
	@AfterEach
	void cleanUp() {
		this.closeModel(model);
		this.closeExecutorService(es);
	}
	
	@Test
	void test() {
//		ClientSimulant c1 = new ClientSimulant("order1#20200809112233123#0#0:item1,2;", model);
//		ClientSimulant c2 = new ClientSimulant("order2#20200809235959456#1#0:item1,12;item2,3;", model);
//		ClientSimulant c3 = new ClientSimulant("order3#20210809000000789#1#1:item3,5;item1,10;", model);
//		ClientSimulant c4 = new ClientSimulant("order4#20200810112233012#0#0:item1,2;item2,0;", model);
//		ClientSimulant c5 = new ClientSimulant("order5#20200812235959475#1#0:item1,2;item2,3;", model);
//		ClientSimulant c6 = new ClientSimulant("order6#20200813000000183#1#1:item3,5;item3,4;", model);
//		ClientSimulant c7 = new ClientSimulant("order7#20200909112233937#0#0:item1,2;item2,5;", model);
//		ClientSimulant c8 = new ClientSimulant("order8#20210809235959253#1#0:item1,2;item2,3;", model);
//		ClientSimulant c9 = new ClientSimulant("order9#20210709000000745#1#1:item3,5;", model);
//		ClientSimulant c10 = new ClientSimulant("order10#20210809000000111#1#1:item3,5;", model);
//		
//		pool.submit(c1);
//		pool.submit(c2);
//		pool.submit(c3);
//		pool.submit(c4);
//		pool.submit(c5);
//		pool.submit(c6);
//		pool.submit(c7);
//		pool.submit(c8);
//		pool.submit(c9);
//		pool.submit(c10);
		
		ClientSimulant c1 = new ClientSimulant(oData1, model);
		ClientSimulant c2 = new ClientSimulant(oData2, model);
		ClientSimulant c3 = new ClientSimulant(oData3, model);
		
		es.submit(c1);
		es.submit(c2);
		es.submit(c3);
		
		GeneralTestUtilityClass.performWait(10);
		
		Assertions.assertEquals(model.getAllUnconfirmedOrders().length, 3);
	}
	
}
