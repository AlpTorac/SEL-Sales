package model.util;

import java.math.BigDecimal;

public interface IParser {
	default BigDecimal parseBigDecimal(String s) {
		return BigDecimal.valueOf(Double.valueOf(s));
	}
	
	default boolean parseBoolean(String s) {
		if (s.equals("0")) {
			return false;
		} else {
			return true;
		}
	}
}
