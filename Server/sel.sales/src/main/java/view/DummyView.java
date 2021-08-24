package view;

import view.repository.UIComponentFactory;
import controller.IController;
import view.repository.UIButton;
import view.repository.UIComponent;
import view.repository.UIInnerFrame;
import view.repository.UILayout;
import view.repository.UIRootComponent;
import view.repository.UITextBox;

public class DummyView extends View {
	
	UIRootComponent mainWindow;
	UIInnerFrame frame;
	UILayout layout;
	
	UITextBox dishName;
	UITextBox price;
	
	UIButton showMenu;
	UIButton addToMenu;
	UIButton removeFromMenu;
	UIButton nextDish;
	UIButton prevDish;
	
	UIComponentFactory fac;
	
	public DummyView(UIComponentFactory fac, IController controller) {
		super(controller);
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
	protected UIRootComponent initMainWindow() {
		return this.fac.createRootComponent(1000, 1000, "stage");
	}
	protected UILayout initWindow() {
		return this.fac.createLayout(0, 0, 1000, 1000);
	}
	protected UIInnerFrame initFrame(UIComponent parent) {
		return this.fac.createInnerFrame(800, 800, "scene", parent);
	}
	protected UITextBox initDishName(UIComponent parent) {
		return this.fac.createTextBox(100, 100, 200, 100, "dishName", parent);
	}
	protected UITextBox initPrice(UIComponent parent) {
		UITextBox textBox = this.fac.createTextBox(400, 100, 200, 100, "price", parent);
		return textBox;
	}
	protected UIButton initShowMenu(UIComponent parent) {
		UIButton button = this.fac.createButton(400, 400, 200, 100, "showMenu", parent);
		button.addClickListener(new DummyListener(button, this.getController()));
		return button;
	}
	protected UIButton initAddToMenu(UIComponent parent) {
		UIButton button = this.fac.createButton(400, 500, 200, 100, "addToMenu", parent);
		return button;
	}
	protected UIButton initRemoveFromMenu(UIComponent parent) {
		return this.fac.createButton(400, 600, 200, 100, "removeFromMenu", parent);
	}
	protected UIButton initNextDish(UIComponent parent) {
		return this.fac.createButton(200, 500, 200, 100, "nextDish", parent);
	}
	protected UIButton initPrevDish(UIComponent parent) {
		return this.fac.createButton(600, 500, 200, 100, "prevDish", parent);
	}
	
	public void show() {
		this.mainWindow.setInnerFrame(frame);
		this.mainWindow.show();
	}
}
