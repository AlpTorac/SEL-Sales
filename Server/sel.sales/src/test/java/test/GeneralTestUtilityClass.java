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
		long randomNumber = lowerLimit + Math.round(factor * intervallLength);
		return randomNumber;
	}
	public static int generateRandomNumber(int lowerLimit, int upperLimit) {
		int intervallLength = upperLimit - lowerLimit;
		float factor = (float) Math.random();
		int randomNumber = lowerLimit + Math.round(factor * intervallLength);
		return randomNumber;
	}
	public static byte generateRandomByte() {
		int intervalLength = 0;
		if (Math.round(Math.random()) < 0.5) {
			intervalLength = Byte.MAX_VALUE;
		} else {
			intervalLength = Byte.MIN_VALUE;
		}
		return (byte) Math.round(Math.random() * intervalLength);
	}
	public static String generateRandomWord(int length) {
		if (length <= 0) {
			return "";
		} else {
			byte[] bytes = new byte[length];
			for (int i = 0; i < bytes.length; i++) {
				while (!new String(new byte[] {bytes[i]}).matches("\\w")) {
					bytes[i] = generateRandomByte();
				}
			}
			
			return new String(bytes);
		}
	}
}
