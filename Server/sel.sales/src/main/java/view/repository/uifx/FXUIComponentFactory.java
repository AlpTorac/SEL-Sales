package view.repository.uifx;

import javafx.scene.layout.Pane;
import view.repository.IUIComponent;
import view.repository.IUILibraryHelper;
import view.repository.uiwrapper.DirectoryChooserWrapper;
import view.repository.uiwrapper.FileChooserWrapper;
import view.repository.uiwrapper.ToggleGroupWrapper;
import view.repository.uiwrapper.UIButton;
import view.repository.uiwrapper.UICheckBox;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIGridLayout;
import view.repository.uiwrapper.UIHBoxLayout;
import view.repository.uiwrapper.UIInnerFrame;
import view.repository.uiwrapper.UILabel;
import view.repository.uiwrapper.UIListView;
import view.repository.uiwrapper.UIRadioButton;
import view.repository.uiwrapper.UIRootComponent;
import view.repository.uiwrapper.UITabPane;
import view.repository.uiwrapper.UITable;
import view.repository.uiwrapper.UITextBox;
import view.repository.uiwrapper.UIVBoxLayout;

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
	public <T> UIListView<T> createListView() {
		FXListView<T> wrapee = new FXListView<T>();
		UIListView<T> listView = new UIListView<T>(wrapee);
		return listView;
	}
	
	@Override
	public UILabel createLabel() {
		FXLabel wrapee = new FXLabel();
		UILabel listView = new UILabel(wrapee);
		return listView;
	}

	@Override
	public UICheckBox createCheckBox() {
		FXCheckBox wrapee = new FXCheckBox();
		UICheckBox checkBox = new UICheckBox(wrapee);
		return checkBox;
	}

	@Override
	public UITabPane createTabPane() {
		FXTabPane wrapee = new FXTabPane();
		UITabPane tabPane = new UITabPane(wrapee);
		return tabPane;
	}

	@Override
	public IUILibraryHelper createUILibraryHelper() {
		return new FXHelper();
	}
	
	@Override
	public FileChooserWrapper createFileChooser() {
		FXFileChooser wrapee = new FXFileChooser();
		FileChooserWrapper wrapper = new FileChooserWrapper(wrapee);
		return wrapper;
	}
	
	@Override
	public DirectoryChooserWrapper createDirectoryChooser() {
		FXDirectoryChooser wrapee = new FXDirectoryChooser();
		DirectoryChooserWrapper wrapper = new DirectoryChooserWrapper(wrapee);
		return wrapper;
	}
}
