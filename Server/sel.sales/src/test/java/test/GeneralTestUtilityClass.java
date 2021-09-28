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
		Class<?> classObject = o.getClass();
		try {
			while (f == null) {
				Field[] fields = classObject.getDeclaredFields();
				for (Field field : fields) {
					if (field.getName().equals(fieldName)) {
						f = field;
					}
				}
				classObject = classObject.getSuperclass();
			}
			f.setAccessible(true);
		} catch (SecurityException e) {
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
	
	public static void performWait(long durationInMillis) {
		try {
			Thread.sleep(durationInMillis);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public static long generateRandomNumber(long lowerLimit, long upperLimit) {
		long intervallLength = upperLimit - lowerLimit;
		double factor = Math.random();
		long randomNumber = Math.round(factor * intervallLength);
		return randomNumber;
	}
}
