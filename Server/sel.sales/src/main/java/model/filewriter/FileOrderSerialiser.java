package model.filewriter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.order.IOrderItemData;
import model.order.serialise.IOrderFormat;
import model.order.serialise.IOrderSerialiser;

public class FileOrderSerialiser implements IOrderSerialiser {
	private IOrderFormat format = new FileOrderFormat();
	
	@Override
	public String serialiseOrderData(IOrderItemData[] orderItemsData, LocalDateTime date, boolean isCash,
			boolean isHere, BigDecimal orderDiscount, String orderID) {
		String result = "";
		for (IOrderItemData d : orderItemsData) {
			result += this.serialiseOrderID(orderID) + this.getOrderDataFieldSeperator();
			result += this.serialiseOrderDate(date) + this.getOrderDataFieldSeperator();
			result += this.serialiseDishMenuItemID(d.getItemData()) + this.getOrderItemDataFieldSeperator();
			result += this.serialiseOrderItemAmount(d) + this.getOrderItemDataFieldEnd();
			result += this.serialiseIsCash(isCash) + this.getOrderDataFieldSeperator();
			result += this.serialiseIsHere(isHere) + this.getOrderDataFieldSeperator();
			result += this.serialiseIsDiscounted(d) + this.getOrderDataFieldEnd();
		}
		return result;
	}

	@Override
	public IOrderFormat getOrderFormat() {
		return this.format;
	}

	public String serialiseIsDiscounted(IOrderItemData d) {
		return this.serialiseBoolean(d.getTotalDiscount().compareTo(BigDecimal.ZERO) != 0);
	}
	
}
