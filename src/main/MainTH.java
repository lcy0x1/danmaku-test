package main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.Reader;
import io.Writer;
import page.MainFrame;
import page.MainPage;

public class MainTH {

	public static final int ver = 1;

	public static final boolean WRITE = !new File("./.project").exists();

	public static String getTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}

	public static void main(String[] args) {
		Writer.logPrepare();
		Writer.logSetup();
		Reader.getData$0();
		new MainFrame("danmaku ver 4.0.1").initialize();
		Reader.getData$1();
		new Timer().start();
		MainFrame.changePanel(new MainPage());
	}

	public static String validate(String str) {
		char[] chs = new char[] { '.', '/', '\\', ':', '*', '?', '"', '<', '>', '|' };
		for (char c : chs)
			str = str.replace(c, '#');
		return str;
	}

}
