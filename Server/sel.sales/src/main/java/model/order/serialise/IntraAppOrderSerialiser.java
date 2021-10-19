package model.order.serialise;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.order.IOrderData;
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
	public String serialiseOrderDatas(IOrderData[] orderDatas) {
		String result = "";
		for (IOrderData data : orderDatas) {
			result += this.getOrderFormat().getOrderStart();
			result += this.serialiseOrderData(data);
			result += this.getOrderFormat().getOrderEnd();
		}
		return result;
	}
	
	@Override
	public String serialiseOrderData(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere, BigDecimal orderDiscount, String orderID) {
		String result = "";
		result += this.serialiseOrderID(orderID) + this.getOrderDataFieldSeperator();
		result += this.serialiseOrderDate(date) + this.getOrderDataFieldSeperator();
		result += this.serialiseIsCash(isCash) + this.getOrderDataFieldSeperator();
		result += this.serialiseIsHere(isHere) + this.getOrderDataFieldEnd();
		result += this.serialiseOrderItemDatas(orderData);
		
		return result;
	}
}
