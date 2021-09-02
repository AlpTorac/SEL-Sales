package entrypoint;

import java.math.BigDecimal;

import controller.IController;
import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.IModel;
import model.Model;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;
import view.IView;
import view.MainView;
import view.repository.uifx.FXUIComponentFactory;

public class MainApp extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		IModel model = new Model();
		IController controller = new MainController(model);
		IView view = new MainView(new FXUIComponentFactory(), controller, model);
		view.startUp();
		view.show();
		// ADD FAKE DATA -------------------------------------------------------
		// ADD FAKE DATA -------------------------------------------------------
		IDishMenuItemDataFactory menuItemDataFac = model.getItemDataCommunicationProtocoll();
		IDishMenuItemIDFactory menuItemIDFac = model.getItemIDCommunicationProtocoll();
		model.addMenuItem(menuItemDataFac.constructData(
				"aaa",
				BigDecimal.valueOf(2.34),
				BigDecimal.valueOf(5),
				BigDecimal.valueOf(4),
				"item1", menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				"bbb",
				BigDecimal.valueOf(5.67),
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(0.5),
				"item2", menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				"ccc",
				BigDecimal.valueOf(3.34),
				BigDecimal.valueOf(4),
				BigDecimal.valueOf(3.5),
				"item3", menuItemIDFac));
		
		model.addUnconfirmedOrder("order1-20200820112233000-0-0:item1,2;");
		model.addUnconfirmedOrder("order2-20200110235959153-1-0:item1,2;item2,3;");
		model.addUnconfirmedOrder("order3-20201201000000999-1-1:item3,5;");
		// ADD FAKE DATA -------------------------------------------------------
		// ADD FAKE DATA -------------------------------------------------------
	}
}
