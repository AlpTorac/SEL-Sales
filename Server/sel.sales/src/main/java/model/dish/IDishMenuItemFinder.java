package model.dish;

import model.id.EntityID;

public interface IDishMenuItemFinder {
	IDishMenuItem getDish(EntityID id);
	IDishMenuItem getDish(String id);
}
