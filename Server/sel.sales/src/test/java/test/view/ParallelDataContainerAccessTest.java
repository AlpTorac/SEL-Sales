package test.view;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testfx.framework.junit5.ApplicationTest;

import controller.IController;
import controller.MainController;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.IModel;
import model.Model;
import model.dish.serialise.IDishMenuItemSerialiser;
import test.MainViewOperationsUtilityClass;
import view.IView;
import view.MainView;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXListView;
import view.repository.uifx.FXUIComponentFactory;

@Execution(value = ExecutionMode.SAME_THREAD)
class ParallelDataContainerAccessTest extends ApplicationTest {
	private static IModel model;
	private static IController controller;
	private static IView view;
	
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private BigDecimal i1Disc = BigDecimal.valueOf(0);
	private String i1id = "item1";
	
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	private String i2id = "item2";
	
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private BigDecimal i3Disc = BigDecimal.valueOf(1);
	private String i3id = "item3";
	private ExecutorService es = Executors.newCachedThreadPool();
	
	public void start(Stage primaryStage) {
		model = new Model();
		controller = new MainController(model);
		view = new MainView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), controller, model);
		view.startUp();
		view.show();
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
	}
	
	@Test
	void test() {
		MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
		for (int i = 0; i < 10; i++) {
			final int num = i;
			final String serialisedOrder = "order"+num+"-20200813000000183-1-1:item3,5;item3,4;";
			es.submit(() -> {
				model.addUnconfirmedOrder(serialisedOrder);
			});
			es.submit(() -> {
				model.confirmOrder(serialisedOrder);
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
		es.shutdown();
		Platform.runLater(() -> {view.hide();});
	}

}
