package entrypoint;

import controller.DummyController;
import javafx.application.Application;
import javafx.stage.Stage;
import view.DummyView;
import view.repository.FXUIComponentFactory;

public class MainApp extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		DummyController controller = new DummyController();
		DummyView view = new DummyView(new FXUIComponentFactory(), controller);
		view.show();
	}
}
