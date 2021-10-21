package view.repository.uifx;

import java.util.Optional;

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
	@Override
	public void selectTab(String title) {
		Optional<Tab> oTab = super.getTabs().stream()
		.filter(t -> t.getText().equals(title))
		.findFirst();
		
		if (oTab != null && oTab.isPresent()) {
			super.getSelectionModel().select(oTab.get());
		}
	}
	@Override
	public void selectTab(int index) {
		super.getSelectionModel().select(index);
	}
}
