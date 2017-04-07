package space.snowwolf.common.utils;

public class ArrayUtils {

	private ArrayUtils() {
		
	}
	
	public static <T> int index(T[] array, T obj) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == obj) {
				return i;
			}
		}
		return -1;
	}

}
