package entrypoint;

import java.math.BigDecimal;

import controller.IController;
import controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.IModel;
import model.Model;
import view.IView;
import view.MainView;
import view.repository.uifx.FXUIComponentFactory;

public class MainApp extends Application {
	private static IModel model;
	private static IController controller;
	private static IView view;
	
	public static void main(String[] args) {
		launch(args);
	}

	public static void close() {
		Platform.exit();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		model = new Model();
		controller = new MainController(model);
		view = new MainView(new FXUIComponentFactory(), controller, model);
		view.startUp();
		view.show();
		// ADD FAKE DATA -------------------------------------------------------
		// ADD FAKE DATA -------------------------------------------------------
		
		model.addMenuItem(controller.getDishMenuItemSerialiser().serialise(
				"aaa",
				"item1",
				BigDecimal.valueOf(2.34),
				BigDecimal.valueOf(2),
				BigDecimal.valueOf(4),
				BigDecimal.valueOf(0)
				));
		
		model.addMenuItem(controller.getDishMenuItemSerialiser().serialise(
				"bbb",
				"item2",
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(5.67),
				BigDecimal.valueOf(10),
				BigDecimal.valueOf(0.5)
				));
		
		model.addMenuItem(controller.getDishMenuItemSerialiser().serialise(
				"ccc",
				"item3",
				BigDecimal.valueOf(2.5),
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(4),
				BigDecimal.valueOf(0.5)
				));
		
		model.addUnconfirmedOrder("order1-20200820112233000-0-0:item1,2;");
		model.addUnconfirmedOrder("order2-20200110235959153-1-0:item1,2;item2,3;");
		model.addUnconfirmedOrder("order3-20201201000000999-1-1:item3,5;");
		// ADD FAKE DATA -------------------------------------------------------
		// ADD FAKE DATA -------------------------------------------------------
	}
}
