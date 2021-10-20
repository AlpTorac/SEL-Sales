package model.dish;

import java.util.Collection;

public interface IDishMenuData {
	IDishMenuItemData[] getAllDishMenuItems();
	Collection<IDishMenuItemData> getDishMenuItemCol();
	boolean isEmpty();
	boolean equals(Object o);
}
