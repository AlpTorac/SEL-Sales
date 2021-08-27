package view.repository;

public abstract class UIComponentFactory implements IUIComponentFactory {
	
//	public UIRootComponent createRootComponent(double width, double height) {
//		UIRootComponent c = this.createRootComponent();
//		c.setPrefSize(width, height);
//		return c;
//	}
//	public UIButton createButton(double x, double y, double width, double height, String caption, IUIComponent parent) {
//		UIButton c = this.createButton();
//		c.setCaption(caption);
//		c.attachTo(parent);
//		c.setLayoutPos(x, y);
//		c.setPrefSize(width, height);
//		return c;
//	}
//	public UITextBox createTextBox(double x, double y, double width, double height, String caption, IUIComponent parent) {
//		UITextBox c = this.createTextBox();
//		c.setCaption(caption);
//		c.attachTo(parent);
//		c.setLayoutPos(x, y);
//		c.setPrefSize(width, height);
//		return c;
//	}
//	public UIHBoxLayout createHBoxLayout(double x, double y, double width, double height) {
//		UIHBoxLayout c = this.createHBoxLayout();
//		c.setLayoutPos(x, y);
//		c.setPrefSize(width, height);
//		return c;
//	}
//	public UIGridLayout createGridLayout(double x, double y, double width, double height) {
//		UIGridLayout c = this.createGridLayout();
//		c.setLayoutPos(x, y);
//		c.setPrefSize(width, height);
//		return c;
//	}
//	public <T> UITable<T> createTable(double x, double y, double width, double height, IUIComponent parent) {
//		UITable<T> c = this.createTable();
//		c.attachTo(parent);
//		c.setLayoutPos(x, y);
//		c.setPrefSize(width, height);
//		return c;
//	}
//	public UIButton createButton(String name, IUIComponent parent) {
//		UIButton c = this.createButton();
//		c.attachTo(parent);
//		c.setCaption(name);
//		return c;
//	}
//	public UITextBox createTextBox(String name, IUIComponent parent) {
//		UITextBox c = this.createTextBox();
//		c.attachTo(parent);
//		c.setCaption(name);
//		return c;
//	}
//	public <T> UITable<T> createTable(IUIComponent parent) {
//		UITable<T> c = this.createTable();
//		c.attachTo(parent);
//		return c;
//	}
	
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
	public abstract UIListView createListView();
	
	@Override
	public abstract ToggleGroupWrapper createToggleGroup();
	
	@Override
	public abstract UILabel createLabel();
}
