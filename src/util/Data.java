package util;

public class Data {

	public static boolean CLEARBG = false;
	public static boolean DRAWSPRITE = false;
	public static boolean DEBUG = true;
	public static boolean CLEARBL = false;

	public static String str(int n, int l) {
		String ans = "";
		for (int i = 0; i < l; i++) {
			ans = (n % 10) + ans;
			n /= 10;
		}
		return ans;
	}

}
