package model.dish;

public class DishMenuItemFinder implements IDishMenuItemFinder {
	private IDishMenu menu;
	
	public DishMenuItemFinder(IDishMenu menu) {
		this.menu = menu;
	}
	
	@Override
	public IDishMenuItemData getDish(String id) {
		return this.menu.getItem(id);
	}

}