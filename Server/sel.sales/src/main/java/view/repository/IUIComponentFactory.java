package view.repository;

public interface IUIComponentFactory {
//	default public IRootComponent createRootComponent(double width, double height) {
//		IRootComponent c = this.createRootComponent();
//		c.setPrefSize(width, height);
//		return c;
//	}
//	default public IButton createButton(double x, double y, double width, double height, String caption, IUIComponent parent) {
//		IButton c = this.createButton();
//		c.setCaption(caption);
//		c.attachTo(parent);
//		c.setLayoutPos(x, y);
//		c.setPrefSize(width, height);
//		return c;
//	}
//	default public ITextBox createTextBox(double x, double y, double width, double height, String caption, IUIComponent parent) {
//		ITextBox c = this.createTextBox();
//		c.setCaption(caption);
//		c.attachTo(parent);
//		c.setLayoutPos(x, y);
//		c.setPrefSize(width, height);
//		return c;
//	}
//	default public IHBoxLayout createHBoxLayout(double x, double y, double width, double height) {
//		IHBoxLayout c = this.createHBoxLayout();
//		c.setLayoutPos(x, y);
//		c.setPrefSize(width, height);
//		return c;
//	}
//	default public IGridLayout createGridLayout(double x, double y, double width, double height) {
//		IGridLayout c = this.createGridLayout();
//		c.setLayoutPos(x, y);
//		c.setPrefSize(width, height);
//		return c;
//	}
//	default public <T> ITable<T> createTable(double x, double y, double width, double height, IUIComponent parent) {
//		ITable<T> c = this.createTable();
//		c.attachTo(parent);
//		c.setLayoutPos(x, y);
//		c.setPrefSize(width, height);
//		return c;
//	}
//	default public IButton createButton(String name, IUIComponent parent) {
//		IButton c = this.createButton();
//		c.attachTo(parent);
//		c.setCaption(name);
//		return c;
//	}
//	default public ITextBox createTextBox(String name, IUIComponent parent) {
//		ITextBox c = this.createTextBox();
//		c.attachTo(parent);
//		c.setCaption(name);
//		return c;
//	}
//	default public <T> ITable<T> createTable(IUIComponent parent) {
//		ITable<T> c = this.createTable();
//		c.attachTo(parent);
//		return c;
//	}
	public IRootComponent createRootComponent();
	public IButton createButton();
	public ITextBox createTextBox();
	public IInnerFrame createInnerFrame(IUIComponent parent);
	public <T> ITable<T> createTable();
	public IHBoxLayout createHBoxLayout();
	public IVBoxLayout createVBoxLayout();
	public IGridLayout createGridLayout();
	public IRadioButton createRadioButton();
	public ICheckBox createCheckBox();
	public IToggleGroup createToggleGroup();
	public <T> IListView<T> createListView();
	public ILabel createLabel();
	public ITabPane createTabPane();
	public IUILibraryHelper createUILibraryHelper();
	public IFileChooser createFileChooser();
	public IDirectoryChooser createDirectoryChooser();
	public <T> IChoiceBox<T> createChoiceBox();
	public IAccordion createAccordion();
}
