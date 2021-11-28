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
	
	default String serialiseInteger(int integer) {
		return String.valueOf(integer);
	}
	
	default String serialiseString(String string) {
		return string;
	}
	
	default String serialiseDouble(double dbl) {
		return String.valueOf(dbl);
	}
}
