package view;

import view.repository.IButton;
import view.repository.IPane;
import view.repository.IScene;
import view.repository.IStage;
import view.repository.ITextBox;
import view.repository.IUIComponent;
import view.repository.UIComponentFactory;

public class DummyView extends View {
	
	IStage mainWindow;
	IScene frame;
	IPane window;
	
	ITextBox dishName;
	ITextBox price;
	
	IButton showMenu;
	IButton addToMenu;
	IButton removeFromMenu;
	IButton nextDish;
	IButton prevDish;
	
	UIComponentFactory fac;
	
	protected DummyView(UIComponentFactory fac) {
		this.fac = fac;
		
		this.mainWindow = this.initMainWindow();
		this.window = this.initWindow();
		this.frame = this.initFrame(this.window);
		
		this.dishName = this.initDishName(this.window);
		this.price = this.initPrice(this.window);
		
		this.showMenu = this.initShowMenu(this.window);
		this.addToMenu = this.initAddToMenu(this.window);
		this.removeFromMenu = this.initRemoveFromMenu(this.window);
		this.nextDish = this.initNextDish(this.window);
		this.prevDish = this.initPrevDish(this.window);
	}
	protected IStage initMainWindow() {
		return this.fac.createStage(1000, 1000, "stage");
	}
	protected IPane initWindow() {
		return this.fac.createPane(0, 0, 1000, 1000);
	}
	protected IScene initFrame(IUIComponent parent) {
		return this.fac.createScene(800, 800, "scene", parent);
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
		
		this.mainWindow.setScene(frame);
		this.mainWindow.show();
	}
}
