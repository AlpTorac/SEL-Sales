package model.dish;

import model.entity.id.EntityID;

public interface IDishMenuItemFinder {
	DishMenuItem getDish(EntityID id);
	DishMenuItem getDish(String id);
}
