package model;

import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemID;

public interface IDishMenuItemFinder {
	IDishMenuItem getDish(IDishMenuItemID id);
}
