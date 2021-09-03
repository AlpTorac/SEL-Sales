package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IOrderData {
	IOrderItemData[] getOrderedItems();
	LocalDateTime getDate();

	boolean getIsCash();
	boolean getIsHere();
	boolean getIsDiscounted();
	String getID();
	BigDecimal getGrossSum();
	BigDecimal getTotalDiscount();
	BigDecimal getOrderDiscount();
	void setOrderDiscount(BigDecimal orderDiscount);
	default BigDecimal getNetSum() {
		return this.getGrossSum().subtract(this.getOrderDiscount());
	}
	String toString();
}
