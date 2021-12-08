package test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.function.BiFunction;

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
	public static boolean generateRandomBoolean() {
		int b = generateRandomNumber(0, 1);
		if (b == 0) {
			return false;
		} else {
			return true;
		}
	}
	public static String generateRandomWord(int length) {
		if (length <= 0) {
			return "";
		} else {
			byte[] bytes = new byte[length];
			for (int i = 0; i < bytes.length; i++) {
				while (!new String(new byte[] {bytes[i]}).matches("[a-zA-Z0-9]")) {
					bytes[i] = generateRandomByte();
				}
			}
			
			return new String(bytes);
		}
	}
	/**
	 * first argument is the element in array, second argument is the element
	 * that is assumed to be inside the array
	 */
	public static <T> boolean arrayContains(T[] array, T element, BiFunction<T,T,Boolean> comparer) {
		if (array == null && element == null) {
			return true;
		} else if (array == null ^ element == null) {
			return false;
		}
		for (T t : array) {
			if (comparer.apply(t, element)) {
				return true;
			}
		}
		return false;
	}
	public static <T> boolean arrayContains(T[] array, T element) {
		return arrayContains(array, element, (t1,t2) -> {
			return t1 != null && t2 != null && t1.equals(t2);
		});
	}
	public static <T> boolean arrayContentEquals(T[] array1, T[] array2) {
		if (array1 == array2) {
			return true;
		}
		if (array1 == null ^ array2 == null) {
			return false;
		}
		for (T element : array2) {
			if (!arrayContains(array1, element)) {
				return false;
			}
		}
		return true;
	}
	public static <T> boolean arrayContentEquals(T[] array1, T[] array2, BiFunction<T,T,Boolean> comparer) {
		if (array1 == array2) {
			return true;
		}
		if (array1 == null ^ array2 == null) {
			return false;
		}
		for (T element : array2) {
			if (!arrayContains(array1, element, comparer)) {
				return false;
			}
		}
		return true;
	}
	public static void deletePathContent(File folderToEmpty) {
		deleteRecursively(folderToEmpty);
	}
	public static void deletePathContent(String folderToEmptyAddress) {
		deleteRecursively(new File(folderToEmptyAddress));
	}
	private static void deleteRecursively(File folderToEmpty) {
		File testFolder = folderToEmpty;
		File[] subFiles = testFolder.listFiles();
		if (subFiles != null) {
			for (File f : subFiles) {
				deleteInnerPathContent(f);
			}
		}
	}
	private static void deleteInnerPathContent(File folderToEmpty) {
		deleteRecursively(folderToEmpty);
		folderToEmpty.delete();
		folderToEmpty.deleteOnExit();
	}
}
