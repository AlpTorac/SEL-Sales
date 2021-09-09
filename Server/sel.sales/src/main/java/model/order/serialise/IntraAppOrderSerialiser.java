package model.order.serialise;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.order.IOrderItemData;

public class IntraAppOrderSerialiser implements IOrderSerialiser {
	private IOrderFormat format;
	
	public IntraAppOrderSerialiser() {
		this.format = new IntraAppOrderFormat();
	}

	@Override
	public IOrderFormat getOrderFormat() {
		return this.format;
	}
	
	@Override
	public String serialiseOrderData(IOrderItemData[] orderItemsData, LocalDateTime date, boolean isCash, boolean isHere, BigDecimal orderDiscount, String orderID) {
		String result = "";
		result += this.serialiseOrderID(orderID) + this.getOrderDataFieldSeperator();
		result += this.serialiseOrderDate(date) + this.getOrderDataFieldSeperator();
		result += this.serialiseIsCash(isCash) + this.getOrderDataFieldSeperator();
		result += this.serialiseIsHere(isHere);
		
		if (orderDiscount.compareTo(BigDecimal.ZERO) != 0) {
			result += this.getOrderDataFieldSeperator() + this.serialiseOrderDiscount(orderDiscount) + this.getOrderDataFieldEnd();
		} else {
			result += this.getOrderDataFieldEnd();
		}
		
		result += this.serialiseOrderItemDatas(orderItemsData);
		
		return result;
	}
}
