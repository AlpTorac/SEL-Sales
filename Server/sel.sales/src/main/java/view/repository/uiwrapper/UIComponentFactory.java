package view.repository.uiwrapper;

import view.repository.IUIComponent;
import view.repository.IUIComponentFactory;

public abstract class UIComponentFactory implements IUIComponentFactory {
	@Override
	public abstract UIRootComponent createRootComponent();

	@Override
	public abstract UIButton createButton();

	@Override
	public abstract UITextBox createTextBox();

	@Override
	public abstract UIInnerFrame createInnerFrame(IUIComponent parent);

	@Override
	public abstract UIHBoxLayout createHBoxLayout();
	
	@Override
	public abstract UIVBoxLayout createVBoxLayout();
	
	@Override
	public abstract UIGridLayout createGridLayout();
	
	@Override
	public abstract <T> UITable<T> createTable();
	
	@Override
	public abstract UIRadioButton createRadioButton();
	
	@Override
	public abstract <T> UIListView<T> createListView();
	
	@Override
	public abstract ToggleGroupWrapper createToggleGroup();
	
	@Override
	public abstract UILabel createLabel();
}
