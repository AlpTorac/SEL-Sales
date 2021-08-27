package view.repository;

import javafx.scene.layout.Pane;

public class FXUIComponentFactory extends UIComponentFactory {
	public UIHBoxLayout createHBoxLayout() {
		FXHBox wrapee = new FXHBox();
		UIHBoxLayout layout = new UIHBoxLayout(wrapee);
		return layout;
	}
	
	public UIVBoxLayout createVBoxLayout() {
		FXVBox wrapee = new FXVBox();
		UIVBoxLayout layout = new UIVBoxLayout(wrapee);
		return layout;
	}
	
	public UIGridLayout createGridLayout() {
		FXGrid wrapee = new FXGrid();
		UIGridLayout layout = new UIGridLayout(wrapee);
		return layout;
	}
	
	public UIRootComponent createRootComponent() {
		FXStage wrapee = new FXStage();
		UIRootComponent rootComponent = new UIRootComponent(wrapee);
		return rootComponent;
	}
	
	public UIButton createButton() {
		FXButton wrapee = new FXButton();
		UIButton button = new UIButton(wrapee);
		return button;
	}
	
	public UITextBox createTextBox() {
		FXTextBox wrapee = new FXTextBox();
		UITextBox textBox = new UITextBox(wrapee);
		return textBox;
	}
	
	public UIInnerFrame createInnerFrame(IUIComponent parent) {
		FXScene wrapee = new FXScene((Pane) parent.getComponent());
		UIInnerFrame innerFrame = new UIInnerFrame(wrapee);
		return innerFrame;
	}
	
	@Override
	public <T> UITable<T> createTable() {
		FXTable<T> wrapee = new FXTable<T>();
		UITable<T> table = new UITable<T>(wrapee);
		return table;
	}
	
	@Override
	public UIRadioButton createRadioButton() {
		FXRadioButton wrapee = new FXRadioButton();
		UIRadioButton radioButton = new UIRadioButton(wrapee);
		return radioButton;
	}

	@Override
	public ToggleGroupWrapper createToggleGroup() {
		FXToggleGroup tg = new FXToggleGroup();
		ToggleGroupWrapper wrapper = new ToggleGroupWrapper(tg);
		return wrapper;
	}

	@Override
	public UIListView createListView() {
		FXListView wrapee = new FXListView();
		UIListView listView = new UIListView(wrapee);
		return listView;
	}
	
	@Override
	public UILabel createLabel() {
		FXLabel wrapee = new FXLabel();
		UILabel listView = new UILabel(wrapee);
		return listView;
	}
}
