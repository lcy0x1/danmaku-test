package main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.Reader;
import io.Writer;
import page.MainFrame;
import page.MainPage;

public class MainBCU {

	public static final int ver = 40808;

	public static int FILTER_TYPE = 0;
	public static final boolean WRITE = !new File("./.project").exists();
	public static boolean preload = false, trueRun = false, loaded = false, USE_JOGL = false;

	public static String getTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	public static void main(String[] args) {
		trueRun = true;
		long mem = Runtime.getRuntime().maxMemory();
		if (mem >> 28 == 0) {
			Opts.pop(Opts.MEMORY, "" + (mem >> 20));
			System.exit(0);
		}

		Writer.logPrepare();
		Writer.logSetup();
		Reader.getData$0();

		new MainFrame("danmaku").initialize();
		new Timer().start();
		loaded = true;
		MainFrame.changePanel(new MainPage());
	}

	public static String validate(String str) {
		char[] chs = new char[] { '.', '/', '\\', ':', '*', '?', '"', '<', '>', '|' };
		for (char c : chs)
			str = str.replace(c, '#');
		return str;
	}

}
