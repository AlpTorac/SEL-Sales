package client;

import javafx.application.Application;
import javafx.stage.Stage;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;
import client.controller.IClientController;
import client.controller.StandardClientController;
import client.external.BluetoothClientExternal;
import client.external.IClientExternal;
import client.external.StandardClientExternal;
import client.model.ClientModel;
import client.model.IClientModel;
import client.view.IClientView;
import client.view.StandardClientView;
import external.bluetooth.WindowsBluetoothConnectionUtility;

public class ClientApp extends Application {
	private static IClientModel model;
	private static IClientController controller;
	private static IClientView view;
	private static IClientExternal external;
	
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
		model = new ClientModel();
		controller = new StandardClientController(model);
		view = new StandardClientView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), controller, model);
//		external = new BluetoothClientExternal(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		external = new StandardClientExternal(controller, model, new WindowsBluetoothConnectionUtility(), pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		view.startUp();
		view.show();
		model.loadSaved();
		
		primaryStage.setOnCloseRequest(e -> {
			close();
		});
	}
}
