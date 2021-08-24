package view.repository;

public abstract class UIComponentFactory implements IUIComponentFactory {

	@Override
	public abstract UIRootComponent createRootComponent(double width, double height, String name);

	@Override
	public abstract UIButton createButton(double x, double y, double width, double height, String name, IUIComponent parent);

	@Override
	public abstract UITextBox createTextBox(double x, double y, double width, double height, String name, IUIComponent parent);

	@Override
	public abstract UIInnerFrame createInnerFrame(double width, double height, String name, IUIComponent parent);

	@Override
	public abstract UILayout createLayout(double x, double y, double width, double height);

}
