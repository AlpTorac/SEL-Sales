package model.dish;

import java.math.BigDecimal;

public interface IDishMenuItemData {
	public String getDishName();

	public BigDecimal getPortionSize();

	public BigDecimal getPrice();

	public BigDecimal getProductionCost();

	public IDishMenuItemID getId();
}
