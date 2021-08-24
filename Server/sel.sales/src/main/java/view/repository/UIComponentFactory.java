package view.repository;

public interface UIComponentFactory {
	public IRootComponent createRootComponent(double width, double height, String name);
	public IButton createButton(double x, double y, double width, double height, String name, IUIComponent parent);
	public ITextBox createTextBox(double x, double y, double width, double height, String name, IUIComponent parent);
	public IInnerFrame createInnerFrame(double width, double height, String name, IUIComponent parent);
	default public ILayout createLayout(double x, double y, double width, double height, IUIComponent parent) {
		ILayout pane = this.createLayout(x, y, width, height);
		pane.attachTo(parent);
		return pane;
	}
	public ILayout createLayout(double x, double y, double width, double height);
}
