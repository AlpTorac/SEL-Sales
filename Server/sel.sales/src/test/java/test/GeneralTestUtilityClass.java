package test;

import java.lang.reflect.Field;

public final class GeneralTestUtilityClass {
	@SuppressWarnings("unchecked")
	public static <T,S> T getPrivateFieldValue(S o, String fieldName) {
		Field f = null;
		try {
			f = o.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		T result = null;
		try {
			result = (T) f.get(o);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
