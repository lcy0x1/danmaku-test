package main;

import javax.swing.JOptionPane;

public class Opts {

	public static final int MEMORY = 1001, SECTY = 1002, REQITN = 1003;

	public static boolean conf(String text) {
		return warning(text, "confirmation");
	}

	public static void ioErr(String text) {
		pop(text, "IO error");
	}

	public static void loadErr(String text) {
		pop(text, "loading error");
	}

	public static void pop(int id, String... is) {
		if (id == MEMORY)
			pop("not enough memory. Current memory: " + is[0] + "MB.", "not enough memory");
		if (id == SECTY)
			pop("Failed to access files. Please move this program to another place", "file permission error");
		if (id == REQITN)
			pop("failed to connect to internet while download is necessary", "download error");
	}

	public static void recdErr(String name, String suf) {
		pop("replay " + name + " uses unavailable " + suf, "replay read error");
	}

	public static boolean updateCheck(String s, String p) {
		return warning(s + " update available. do you want to update? " + p, "update check");
	}

	public static boolean writeErr0(String f) {
		return Opts.warning("failed to write file: " + f + " do you want to retry?", "IO error");
	}

	public static boolean writeErr1(String f) {
		return Opts.warning("failed to write file: " + f + " do you want to save it in another place?", "IO error");
	}

	private static void pop(String text, String title) {
		int opt = JOptionPane.PLAIN_MESSAGE;
		JOptionPane.showMessageDialog(null, text, title, opt);
	}

	private static boolean warning(String text, String title) {
		int opt = JOptionPane.OK_CANCEL_OPTION;
		int val = JOptionPane.showConfirmDialog(null, text, title, opt);
		return val == JOptionPane.OK_OPTION;
	}

}
