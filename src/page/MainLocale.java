package page;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;

import io.Writer;
import main.MainBCU;

public strictfp class MainLocale {

	public static final Map<String, MainLocale> NAMP = new TreeMap<>();
	public static final Map<String, TTT> TMAP = new TreeMap<>();
	public static final String[] LOC_NAME = { "English", "\u4E2D\u6587", "\uD55C\uAD6D\uC5B4", "Japanese", "Russian",
			"German", "French", "Dutch", "Spainsh" };
	public static final String[] LOC_CODE = { "en", "zh", "kr", "jp", "ru", "de", "fr", "nl", "es" };
	public static final String[] RENN = { "page", "info", "internet", "util" };
	private static final ResourceBundle[] RENS = new ResourceBundle[4];

	static {
		for (int i = 0; i < 4; i++)
			RENS[i] = ResourceBundle.getBundle((MainBCU.WRITE ? "src/" : "") + "page/" + RENN[i], Locale.ENGLISH);
	}

	public static int lang = 0;
	public static boolean exLang, exTTT;

	public static TTT addTTT(String loc, String page, String text, String cont) {
		TTT ttt = TMAP.get(loc);
		if (ttt == null)
			TMAP.put(loc, ttt = new TTT());
		if (text.equals("*"))
			ttt.tttp.put(page, cont);
		else if (page.equals("*"))
			ttt.ttts.put(text, cont);
		else {
			if (!ttt.ttt.containsKey(page))
				ttt.ttt.put(page, new TreeMap<String, String>());
			ttt.ttt.get(page).put(text, cont);
		}
		return ttt;
	}

	public static String getLoc(int loc, String key) {
		if (loc >= 0 && loc < 4) {
			String loci = RENN[loc] + "_";
			String locl = loci + LOC_CODE[MainLocale.lang];
			if (NAMP.containsKey(locl) && NAMP.get(locl).contains(key)) {
				String str = NAMP.get(loci + LOC_CODE[MainLocale.lang]).get(key);
				if (str.equals("(null)"))
					str = RENS[loc].getString(key);
				String[] strs = str.split("#");
				if (strs.length == 1)
					return str;
				for (int i = 1; i < strs.length; i += 2)
					strs[i] = getLoc(loc, strs[i]);
				String ans = "";
				for (int i = 0; i < strs.length; i++)
					ans += strs[i];
				return ans;
			}
			try {
				return exLang ? "[" + loci + key + "]" : RENS[loc].getString(key);
			} catch (MissingResourceException e) {
				return key;
			}
		}
		return key;
	}

	public static String[] getLoc(int loc, String... strs) {
		String[] ans = new String[strs.length];
		for (int i = 0; i < ans.length; i++)
			ans[i] = getLoc(loc, strs[i]);
		return ans;
	}

	public static String[] getLoc(int loc, String pre, int max) {
		String[] ans = new String[max];
		for (int i = 0; i < ans.length; i++)
			ans[i] = getLoc(loc, pre + i);
		return ans;
	}

	public static void saveWorks() {
		for (String loc : LOC_CODE) {
			TTT ttt = TMAP.get(loc);
			if (ttt != null && ttt.edited)
				ttt.write(Writer.newFile("./lib/lang/" + loc + "/tutorial.txt"));
			for (int i = 0; i < 4; i++) {
				MainLocale ml = NAMP.get(RENN[i] + "_" + loc);
				if (ml != null && ml.edited)
					ml.write(Writer.newFile("./lib/lang/" + loc + "/" + RENN[i] + ".properties"));
			}
		}
	}

	protected static String getTTT(String page, String text) {
		String loc = LOC_CODE[MainLocale.lang];
		String ans = null;
		if (TMAP.containsKey(loc))
			ans = TMAP.get(loc).getTTT(page, text);
		if (ans != null)
			return ans;
		if (exTTT)
			return "[" + page + "_" + text + "]";
		loc = LOC_CODE[0];
		if (TMAP.containsKey(loc))
			return TMAP.get(loc).getTTT(page, text);
		return null;
	}

	protected static void redefine() {
	}

	protected static void setLoc(int i, String key, String value) {
		if (i < 0)
			return;
		String loc = RENN[i] + "_" + LOC_CODE[lang];
		MainLocale ml = NAMP.get(loc);
		if (ml == null)
			NAMP.put(loc, ml = new MainLocale(loc));
		ml.res.put(key, value);
		ml.edited = true;
	}

	protected static void setTTT(String loc, String info, String str) {
		addTTT(LOC_CODE[lang], loc, info, str).edited = true;
	}

	public final Map<String, String> res = new TreeMap<>();

	public boolean edited;

	public MainLocale(String str) {
		NAMP.put(str, this);
	}

	public void write(PrintStream ps) {
		for (Entry<String, String> ent : res.entrySet())
			ps.println(ent.getKey() + "\t" + ent.getValue());
	}

	private boolean contains(String str) {
		return res.containsKey(str);
	}

	private String get(String str) {
		if (res.containsKey(str))
			return res.get(str);
		return "!" + str + "!";
	}

}

class TTT {

	public final Map<String, String> tttp = new TreeMap<>();
	public final Map<String, String> ttts = new TreeMap<>();
	public final Map<String, Map<String, String>> ttt = new TreeMap<>();

	public boolean edited;

	public void write(PrintStream ps) {
		if (ps == null)
			return;
		for (Entry<String, String> ent : tttp.entrySet())
			ps.println(ent.getKey() + "\t*\t" + ent.getValue());
		for (Entry<String, String> ent : ttts.entrySet())
			ps.println("*\t" + ent.getKey() + "\t" + ent.getValue());
		for (Entry<String, Map<String, String>> ent : ttt.entrySet())
			for (Entry<String, String> snt : ent.getValue().entrySet())
				ps.println(ent.getKey() + "\t" + snt.getKey() + "\t" + snt.getValue());
	}

	protected String getTTT(String page, String text) {
		if (ttt.get(page) != null) {
			String strs = ttt.get(page).get(text);
			if (strs != null)
				return strs;
		}
		String strt = ttts.get(text);
		if (strt != null)
			return strt;
		String strp = tttp.get(page);
		if (strp != null)
			return strp;
		return null;
	}
}
