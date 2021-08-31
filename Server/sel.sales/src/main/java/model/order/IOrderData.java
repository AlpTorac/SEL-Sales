package model.order;

import java.math.BigDecimal;
import java.util.Calendar;

public interface IOrderData {
	IOrderItemData[] getOrderedItems();
	Calendar getDate();

	boolean getCashOrCard();
	boolean getHereOrToGo();
	boolean getIsDiscounted();
	IOrderID getID();
	BigDecimal getGrossSum();
	BigDecimal getTotalDiscount();
	BigDecimal getOrderDiscount();
	void setOrderDiscount(BigDecimal orderDiscount);
	default BigDecimal getNetSum() {
		return this.getGrossSum().subtract(this.getTotalDiscount());
	}
	String toString();
}
