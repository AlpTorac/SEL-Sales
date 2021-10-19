package model.filewriter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.order.IOrderData;
import model.order.IOrderItemData;
import model.order.serialise.IOrderFormat;
import model.order.serialise.IOrderSerialiser;

public class FileOrderSerialiser implements IOrderSerialiser {
	private IOrderFormat format = new FileOrderFormat();
	@Override
	public String serialiseOrderDatas(IOrderData[] orderDatas) {
		String result = "";
		for (IOrderData data : orderDatas) {
			result += this.serialiseOrderData(data);
		}
		return result;
	}
	
	@Override
	public String serialiseOrderData(IOrderItemData[] orderData, LocalDateTime date, boolean isCash,
			boolean isHere, BigDecimal orderDiscount, String orderID) {
		String result = "";
		for (IOrderItemData d : orderData) {
			result += this.getOrderFormat().getOrderStart();
			result += this.serialiseOrderID(orderID) + this.getOrderDataFieldSeperator();
			result += this.serialiseOrderDate(date) + this.getOrderDataFieldSeperator();
			result += this.serialiseIsCash(isCash) + this.getOrderDataFieldSeperator();
			result += this.serialiseIsHere(isHere) + this.getOrderDataFieldSeperator();
			result += this.serialiseIsDiscounted(orderDiscount) + this.getOrderDataFieldEnd();
			result += this.serialiseDishMenuItemID(d.getItemData()) + this.getOrderItemDataFieldSeperator();
			result += this.serialiseOrderItemAmount(d) + this.getOrderItemDataFieldEnd();
			result += this.getOrderFormat().getOrderEnd();
		}
		return result;
	}

	@Override
	public IOrderFormat getOrderFormat() {
		return this.format;
	}

	public String serialiseIsDiscounted(BigDecimal orderDiscount) {
		return this.serialiseBoolean(orderDiscount.compareTo(BigDecimal.ZERO) > 0);
	}
	
}
