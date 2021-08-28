package entrypoint;

import controller.IController;
import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.IModel;
import model.Model;
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
	}
}
