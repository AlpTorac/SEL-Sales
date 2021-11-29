package server;

import java.math.BigDecimal;

import external.bluetooth.WindowsBluetoothConnectionUtility;
import javafx.application.Application;
import javafx.stage.Stage;
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.external.BluetoothServerExternal;
import server.external.IServerExternal;
import server.external.StandardServerExternal;
import server.model.IServerModel;
import server.model.ServerModel;
import server.view.IServerView;
import server.view.StandardServerView;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;

public class ServerApp extends Application {
	private static IServerModel model;
	private static IServerController controller;
	private static IServerView view;
	private static IServerExternal external;
	
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
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		model = new ServerModel();
		controller = new StandardServerController(model);
		view = new StandardServerView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), controller, model);
//		external = new BluetoothServerExternal(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		external = new StandardServerExternal(controller, model, new WindowsBluetoothConnectionUtility(), pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		view.startUp();
		view.show();
		model.loadSaved();
		model.setAutoConfirmOrders(true);
		// ADD FAKE DATA -------------------------------------------------------
		// ADD FAKE DATA -------------------------------------------------------
		
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(
				"aaa",
				"item1",
				BigDecimal.valueOf(2.34),
				BigDecimal.valueOf(2),
				BigDecimal.valueOf(4)
				));
		
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(
				"bbb",
				"item2",
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(5.67),
				BigDecimal.valueOf(10)
				));
		
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(
				"ccc",
				"item3",
				BigDecimal.valueOf(2.5),
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(4)
				));
		
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(
				"discount",
				"disc",
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(0),
				BigDecimal.valueOf(-1)
				));
		
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
