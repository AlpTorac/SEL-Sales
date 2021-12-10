package server;

import java.math.BigDecimal;

import external.IConnectionUtility;
import external.WindowsBluetoothConnectionUtility;
import javafx.application.Application;
import javafx.stage.Stage;
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.external.IServerExternal;
import server.external.StandardServerExternal;
import server.model.IServerModel;
import server.model.ServerModel;
import server.view.IServerView;
import server.view.StandardServerView;
import view.repository.uifx.FXUIComponentFactory;

public class ServerApp extends Application {
	private static IServerModel model;
	private static IServerController controller;
	private static IServerView view;
	private static IServerExternal external;
	private static IConnectionUtility connUtil;
	
	private static volatile long pingPongTimeout = 10000;
	private static volatile long minimalPingPongDelay = 5000;
	private static volatile long sendTimeout = 5000;
	private static volatile int resendLimit = 3;
	
	public static void main(String[] args) {
		launch(args);
	}

	public static void close() {
		external.close();
		view.close();
		model.close();
		connUtil.close();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		model = new ServerModel();
		controller = new StandardServerController(model);
		view = new StandardServerView(new FXUIComponentFactory(), controller, model);
//		external = new BluetoothServerExternal(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		connUtil = new WindowsBluetoothConnectionUtility();
		external = new StandardServerExternal(controller, model, connUtil, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		view.startUp();
		view.show();
		model.loadSaved();
		model.setAutoConfirmOrders(true);
		// ADD FAKE DATA -------------------------------------------------------
		// ADD FAKE DATA -------------------------------------------------------
		
		model.addMenuItem(model.getMenuItemFactory().constructData(
				"aaa",
				"item1",
				BigDecimal.valueOf(2.34),
				BigDecimal.valueOf(4),
				BigDecimal.valueOf(2)
				));
		
		model.addMenuItem(model.getMenuItemFactory().constructData(
				"bbb",
				"item2",
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(10),
				BigDecimal.valueOf(5.67)
				));
		
		model.addMenuItem(model.getMenuItemFactory().constructData(
				"ccc",
				"item3",
				BigDecimal.valueOf(2.5),
				BigDecimal.valueOf(4),
				BigDecimal.valueOf(1)
				));
		
		model.addMenuItem(model.getMenuItemFactory().constructData(
				"discount",
				"disc",
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(-1),
				BigDecimal.valueOf(0)
				));
		
		connUtil.init();
		connUtil.start();
		
//		model.addUnconfirmedOrder("order1#20200820112233000#0#0:item1,2;");
//		model.addUnconfirmedOrder("order2#20200110235959153#1#0:item1,2;item2,3;");
//		model.addUnconfirmedOrder("order3#20201201000000999#1#1:item3,5;");
//		model.addUnconfirmedOrder("order4#20211201000000999#1#1:item3,5;item1,2;item2,3;disc,12;");
		
		// ADD FAKE DATA -------------------------------------------------------
		// ADD FAKE DATA -------------------------------------------------------
		primaryStage.setOnCloseRequest(e -> {
			close();
		});
	}
}
