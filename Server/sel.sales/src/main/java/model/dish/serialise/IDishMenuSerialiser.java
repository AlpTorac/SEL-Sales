package model.dish.serialise;

import model.dish.IDishMenuData;
import model.dish.IDishMenuItemData;

public interface IDishMenuSerialiser {
	default String serialise(IDishMenuData data) {
		String result = "";
		IDishMenuItemData[] itemData = data.getAllDishMenuItems();
		for (int i = 0; i < itemData.length - 1; i++) {
			result += this.serialiseItem(itemData[i]);
		}
		if (this.addLastEnd()) {
			result += this.serialiseItem(itemData[itemData.length - 1]);
		} else {
			result += this.serialiseItemWithoutFieldEnd(itemData[itemData.length - 1]);
		}
		return result;
	}
	
	default String serialiseItemWithoutFieldEnd(IDishMenuItemData data) {
		return this.getDishMenuItemSerialiser().serialise(data);
	}
	
	default String serialiseItem(IDishMenuItemData data) {
		return this.serialiseItemWithoutFieldEnd(data) + this.getDishMenuFormat().getDishMenuItemDataFieldEnd();
	}
	
	boolean addLastEnd();
	IDishMenuItemSerialiser getDishMenuItemSerialiser();
	IDishMenuFormat getDishMenuFormat();
}
