package model.dish;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public interface IDishMenuItemData extends Comparable<IDishMenuItemData> {
	default IDishMenuItemData getAssociatedItem(IDishMenuItemFinder finder) {
		return finder.getDish(this.getId());
	}
	
	static final MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
	
	String getDishName();

	default BigDecimal getGrossPricePerPortion() {
		return this.getGrossPrice().divide(this.getPortionSize(), mc);
	}
	
	default BigDecimal getNetPricePerPortion() {
		return this.getNetPrice().divide(this.getPortionSize(), mc);
	}
	
	default BigDecimal getDiscountPerPortion() {
		return this.getDiscount().divide(this.getPortionSize(), mc);
	}
	
	BigDecimal getPortionSize();

	BigDecimal getGrossPrice();

	BigDecimal getProductionCost();

	BigDecimal getDiscount();
	
	default BigDecimal getNetPrice() {
		return this.getGrossPrice().subtract(this.getDiscount());
	}
	
	String getId();
	
	default public int compareTo(IDishMenuItemData data) {
		return this.getId().compareTo(data.getId());
	}
}
