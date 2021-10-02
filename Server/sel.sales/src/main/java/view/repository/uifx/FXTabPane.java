package view.repository.uifx;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import view.repository.ITabPane;
import view.repository.IUIComponent;

public class FXTabPane extends TabPane implements ITabPane, FXAttachable {
	public FXTabPane() {
		super();
		super.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}
	
	@Override
	public void addTab(String title, IUIComponent tabComponent) {
		super.getTabs().add(new Tab(title, ((Node) tabComponent.getComponent())));
	}
}
