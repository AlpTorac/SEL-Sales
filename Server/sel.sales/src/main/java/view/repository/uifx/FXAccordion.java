package view.repository.uifx;

import java.util.Optional;

import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import view.repository.IAccordion;
import view.repository.ITabPane;
import view.repository.IUIComponent;

public class FXAccordion extends Accordion implements IFXSizable, IAccordion {
	FXAccordion() {
		super();
	}
	
	@Override
	public void addTab(String title, IUIComponent tabComponent) {
		super.getPanes().add(new TitledPane(title, ((Node) tabComponent.getComponent())));
	}
	@Override
	public void selectTab(String title) {
		Optional<TitledPane> oTab = super.getPanes().stream()
		.filter(t -> t.getText().equals(title))
		.findFirst();
		
		if (oTab != null && oTab.isPresent()) {
			super.setExpandedPane(oTab.get());
		}
	}
	@Override
	public void selectTab(int index) {
		super.setExpandedPane(super.getPanes().get(index));
	}
	
	@Override
	public void removeAllTabs() {
		super.getPanes().clear();
	}
}
