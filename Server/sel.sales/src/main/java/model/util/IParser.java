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
	
	default String parseString(String s) {
		return s;
	}
	
	default int parseInteger(String s) {
		return Integer.valueOf(s);
	}
	
	default double parseDouble(String s) {
		return Double.valueOf(s);
	}
	
	default String getDataBody(String data, String startToRemove, String endToRemove) {
//		int begin = 0;
//		int end = 0;
//		
//		if (data.startsWith(startToRemove)) {
//			begin = startToRemove.length();
//		}
//		
//		if (data.endsWith(endToRemove)) {
//			end = data.length() - endToRemove.length();
//		}
//		
//		return data.substring(begin, end);
		
		int startIndex = 0;
		int endIndex = data.length();
		if (data.startsWith(startToRemove)) {
			int len = startToRemove.length();
			startIndex = len == 0 ? 0 : len - 1;
		}
		if (data.endsWith(endToRemove)) {
			endIndex -= endToRemove.length();
		}
		return data.substring(startIndex, endIndex);
	}
}
