package view;

import view.repository.IButton;
import view.repository.ILayout;
import view.repository.IInnerFrame;
import view.repository.IRootComponent;
import view.repository.ITextBox;
import view.repository.IUIComponent;
import view.repository.UIComponentFactory;

public class DummyView extends View {
	
	IRootComponent mainWindow;
	IInnerFrame frame;
	ILayout layout;
	
	ITextBox dishName;
	ITextBox price;
	
	IButton showMenu;
	IButton addToMenu;
	IButton removeFromMenu;
	IButton nextDish;
	IButton prevDish;
	
	UIComponentFactory fac;
	
	public DummyView(UIComponentFactory fac) {
		this.fac = fac;
		
		this.mainWindow = this.initMainWindow();
		this.layout = this.initWindow();
		this.frame = this.initFrame(this.layout);
		
		this.dishName = this.initDishName(this.layout);
		this.price = this.initPrice(this.layout);
		
		this.showMenu = this.initShowMenu(this.layout);
		this.addToMenu = this.initAddToMenu(this.layout);
		this.removeFromMenu = this.initRemoveFromMenu(this.layout);
		this.nextDish = this.initNextDish(this.layout);
		this.prevDish = this.initPrevDish(this.layout);
	}
	protected IRootComponent initMainWindow() {
		return this.fac.createRootComponent(1000, 1000, "stage");
	}
	protected ILayout initWindow() {
		return this.fac.createLayout(0, 0, 1000, 1000);
	}
	protected IInnerFrame initFrame(IUIComponent parent) {
		return this.fac.createInnerFrame(800, 800, "scene", parent);
	}
	protected ITextBox initDishName(IUIComponent parent) {
		return this.fac.createTextBox(100, 100, 200, 100, "dishName", parent);
	}
	protected ITextBox initPrice(IUIComponent parent) {
		return this.fac.createTextBox(400, 100, 200, 100, "price", parent);
	}
	protected IButton initShowMenu(IUIComponent parent) {
		return this.fac.createButton(400, 400, 200, 100, "showMenu", parent);
	}
	protected IButton initAddToMenu(IUIComponent parent) {
		return this.fac.createButton(400, 500, 200, 100, "addToMenu", parent);
	}
	protected IButton initRemoveFromMenu(IUIComponent parent) {
		return this.fac.createButton(400, 600, 200, 100, "removeFromMenu", parent);
	}
	protected IButton initNextDish(IUIComponent parent) {
		return this.fac.createButton(200, 500, 200, 100, "nextDish", parent);
	}
	protected IButton initPrevDish(IUIComponent parent) {
		return this.fac.createButton(600, 500, 200, 100, "prevDish", parent);
	}
	
	public void show() {
		this.mainWindow.setInnerFrame(frame);
		this.mainWindow.show();
	}
}
