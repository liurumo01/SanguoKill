package space.snowwolf.common.utils;

public class StringUtils {

	private StringUtils() {
		
	}
	
	public static String connect(char ch, Object ... args) {
		StringBuilder b = new StringBuilder();
		for(int i=0;i<args.length;i++) {
			b.append(args[i].toString() + ch);
		}
		b.deleteCharAt(b.length() - 1);
		return b.toString();
	}
}
