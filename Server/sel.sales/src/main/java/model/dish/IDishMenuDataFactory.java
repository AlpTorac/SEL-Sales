package model.dish;

public interface IDishMenuDataFactory {
	IDishMenuData constructData(IDishMenuItemData[] dishMenuItems);
	default IDishMenuData dishMenuToData(IDishMenu dishMenu) {
		return this.constructData(dishMenu.getAllItemData());
	}
}
