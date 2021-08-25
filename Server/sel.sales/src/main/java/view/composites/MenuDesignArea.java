package view.composites;

import view.repository.IButton;
import view.repository.ITable;
import view.repository.ITextBox;
import view.repository.UIComponentFactory;
import view.repository.UIGridLayout;

public class MenuDesignArea extends UIGridLayout {
	private UIComponentFactory fac;
	
	public MenuDesignArea(UIComponentFactory fac) {
		super(fac.createGridLayout().getComponent());
		this.fac = fac;
		this.init();
	}
	
	private void init() {
		this.addUIComponent(this.initMenuItemTable(), 0, 0, 3, 1);
		this.addUIComponent(this.initDishNameBox(),   0, 1, 1, 1);
		this.addUIComponent(this.initPriceBox(),      1, 1, 1, 1);
		this.addUIComponent(this.initMenuItemIDBox(), 2, 1, 1, 1);
		this.addUIComponent(this.initAddButton(),     0, 2, 3, 1);
	}
	
	protected ITable initMenuItemTable() {
		ITable table = this.fac.createTable();
		table.addColumns(new String[] {
				"Dish Name",
				"Price",
				"ID"
		});
		return table;
	}
	
	protected ITextBox initDishNameBox() {
		ITextBox dishNameBox = this.fac.createTextBox();
		dishNameBox.setCaption("Dish Name");
		return dishNameBox;
	}
	
	protected ITextBox initPriceBox() {
		ITextBox priceBox = this.fac.createTextBox();
		priceBox.setCaption("Price");
		return priceBox;
	}
	
	protected ITextBox initMenuItemIDBox() {
		ITextBox menuItemIDBox = this.fac.createTextBox();
		menuItemIDBox.setCaption("ID");
		return menuItemIDBox;
	}
	
	protected IButton initAddButton() {
		IButton addButton = this.fac.createButton();
		addButton.setCaption("Add");
		return addButton;
	}
}
