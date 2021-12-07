package test.view;

import java.util.concurrent.ExecutorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.order.OrderData;
import server.controller.IServerController;
import server.model.IServerModel;
import server.view.StandardServerView;
import test.FXTestTemplate;

//@Execution(value = ExecutionMode.SAME_THREAD)
class ParallelDataContainerAccessTest extends FXTestTemplate {
	private IServerModel model;
	private IServerController controller;
	private StandardServerView view;
	private ExecutorService es;
	
	@BeforeEach
	void prep() {
		runFXAction(()->{
			es = this.initExecutorService();
			model = this.initServerModel();
			controller = this.initServerController(model);
			view = this.initServerView(model, controller);
			view.startUp();
//			view.show();
			this.addDishMenuToServerModel(model);
			this.setServerOpHelper(model, controller, view);
		});
	}
	
	@AfterEach
	void cleanUp() {
		runFXAction(()->{
			es.shutdown();
//			view.hide();
			this.closeModel(model);
		});
	}
	
	@Test
	void test() {
		for (int i = 0; i < 10; i++) {
			final int num = i;
			
			OrderData data = model.getOrderFactory().constructData("order"+num, model.getDateSettings()
					.parseDate(this.serialiseDate("20200809112233343")), false, false);
			data.addOrderItem(iData1, o1a1);
			
			es.submit(() -> {
				model.addUnconfirmedOrder(data);
			});
		}
		final Object lock = new Object();
		synchronized (lock) {
			try {
				lock.wait(500);
				lock.notifyAll();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Assertions.assertEquals(model.getAllUnconfirmedOrders().length, 10);
		
		for (int i = 0; i < 10; i++) {
			final int num = i;
			
			OrderData data = model.getOrderFactory().constructData("order"+num, model.getDateSettings()
					.parseDate(this.serialiseDate("20200809112233343")), false, false);
			data.addOrderItem(iData1, o1a1);
			
			es.submit(() -> {
				model.confirmOrder(data);
			});
		}
		synchronized (lock) {
			try {
				lock.wait(500);
				lock.notifyAll();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Assertions.assertEquals(model.getAllConfirmedOrders().length, 10);
	}

}
