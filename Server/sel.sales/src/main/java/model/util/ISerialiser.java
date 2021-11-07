package model.util;

import java.math.BigDecimal;

public interface ISerialiser {
	default String serialiseBigDecimal(BigDecimal bd) {
		return bd.toPlainString();
	}
	
	default String serialiseBoolean(boolean b) {
		if (b) {
			return "1";
		} else {
			return "0";
		}
	}
}
