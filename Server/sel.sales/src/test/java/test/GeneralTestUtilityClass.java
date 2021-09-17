package test;

import java.lang.reflect.Field;

import entrypoint.MainApp;
import view.IView;

public final class GeneralTestUtilityClass {
	/**
	 * @param o Object with the wanted field
	 * @param fieldName
	 * @return
	 */
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
	
	@SuppressWarnings("unchecked")
	public static <T> T getStaticPrivateFieldValue(Class<?> o, String fieldName) {
		Field f = null;
		try {
			f = o.getDeclaredField(fieldName);
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
