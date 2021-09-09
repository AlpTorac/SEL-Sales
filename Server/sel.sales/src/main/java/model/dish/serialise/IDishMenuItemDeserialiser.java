package model.dish.serialise;

import model.dish.IDishMenuItemData;

public interface IDishMenuItemDeserialiser {
	IDishMenuItemData deserialise(String serialisedMenuItemData);
}
