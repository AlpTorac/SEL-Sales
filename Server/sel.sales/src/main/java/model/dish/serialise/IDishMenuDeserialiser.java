package model.dish.serialise;

import model.dish.IDishMenuItemData;

public interface IDishMenuDeserialiser {
	IDishMenuItemData deserialise(String serialisedMenuItemData);
}
