package view;

import javafx.application.Application;
import javafx.stage.Stage;
import view.repository.FXUIComponentFactory;

public class MainApp extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		DummyView view = new DummyView(new FXUIComponentFactory());
		view.show();
	}
}
