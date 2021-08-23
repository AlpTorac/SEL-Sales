package view.repository;

public interface UIComponentFactory {
	public IStage createStage(double width, double height, String name);
	public IButton createButton(double x, double y, double width, double height, String name, IUIComponent parent);
	public ITextBox createTextBox(double x, double y, double width, double height, String name, IUIComponent parent);
	public IScene createScene(double width, double height, String name, IUIComponent parent);
	default public IPane createPane(double x, double y, double width, double height, IUIComponent parent) {
		IPane pane = this.createPane(x, y, width, height);
		pane.attachTo(parent);
		return pane;
	}
	public IPane createPane(double x, double y, double width, double height);
}
