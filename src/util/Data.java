package util;

public class Data {

	public static boolean CLEARBG = true;
	public static boolean DRAWSPRITE = true;
	public static boolean DEBUG = false;
	public static boolean CLEARBL = true;

	public static String str(int n, int l) {
		String ans = "";
		for (int i = 0; i < l; i++) {
			ans = (n % 10) + ans;
			n /= 10;
		}
		return ans;
	}

}
