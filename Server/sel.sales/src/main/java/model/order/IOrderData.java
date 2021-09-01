package model.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IOrderData {
	IOrderItemData[] getOrderedItems();
	LocalDateTime getDate();

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
