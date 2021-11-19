package model.dish.serialise;

import model.dish.IDishMenuData;
import model.dish.IDishMenuItemData;
import model.util.ISerialiser;

public interface IDishMenuSerialiser extends ISerialiser {
	default String serialise(IDishMenuData data) {
		String result = "";
		IDishMenuItemData[] itemData = data.getAllDishMenuItems();
		for (int i = 0; i < itemData.length - 1; i++) {
			result += this.serialiseItem(itemData[i]);
		}
		if (itemData.length > 0) {
			if (this.addLastEnd()) {
				result += this.serialiseItem(itemData[itemData.length - 1]);
			} else {
				result += this.serialiseItemWithoutFieldEnd(itemData[itemData.length - 1]);
			}
		} else {
			if (this.addLastEnd()) {
				result += this.getDishMenuFormat().getDishMenuItemDataFieldEnd();
			}
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
