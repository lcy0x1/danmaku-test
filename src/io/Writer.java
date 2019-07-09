package io;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import main.MainTH;
import main.Opts;
import main.Printer;
import page.MainFrame;

public class Writer extends DataIO {

	private static File log, ph;
	private static WriteStream ps;

	public static boolean check(File f) {
		boolean suc = true;
		if (!f.getParentFile().exists())
			suc &= f.getParentFile().mkdirs();
		if (suc)
			try {
				if (!f.exists())
					if (f.isDirectory())
						suc &= f.mkdir();
					else
						suc &= f.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				suc = false;
			}
		if (!suc) {
			ps.println("failed to create file: " + f.getPath());
			Opts.ioErr("failed to create file: " + f.getPath());
		}
		return suc;
	}

	public static void delete(File f) {
		if (f == null || !f.exists())
			return;
		if (f.isDirectory())
			for (File i : f.listFiles())
				delete(i);
		if (!f.delete())
			Opts.ioErr("failed to delete " + f);
	}

	public static void logClose(boolean save) {
		try {
			writeOptions();
		} catch (Exception e) {
			e.printStackTrace();
		}
		delete(new File("./lib/"));
		if (ps.writed) {
		}
		ps.close();
		ph.deleteOnExit();
		if (log.length() == 0)
			log.deleteOnExit();
	}

	public static void logPrepare() {
		String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		log = new File("./logs/" + str + ".log");
		ph = new File("./logs/placeholder");

		if (!log.getParentFile().exists())
			log.getParentFile().mkdirs();
		try {
			if (!log.exists())
				log.createNewFile();
			ps = new WriteStream(log);
		} catch (IOException e) {
			e.printStackTrace();
			Opts.pop(Opts.SECTY);
			System.exit(0);
		}
		try {
			if (ph.exists()) {
				if (!Opts.conf("<html>" + "Another thread is running in this folder, "
						+ "<br> or last time this program doesn't close properly. "
						+ "<br> Are you sure to run? It might damage your save.</html>")) {
					System.exit(0);
				}
			}
			ph.createNewFile();
		} catch (IOException e) {
		}
	}

	public static void logSetup() {
		if (MainTH.WRITE) {
			System.setErr(ps);
			System.setOut(ps);
		}
	}

	public static PrintStream newFile(String str) {
		File f = new File(str);
		check(f);
		PrintStream out = null;
		try {
			out = new PrintStream(f, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public static boolean writeBytes(byte[] bs, String path) {
		File f = new File(path);
		check(f);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			fos.write(bs);
			fos.close();
			return true;
		} catch (IOException e) {
			Printer.w(130, "IOE!!!");
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e1) {
					Printer.w(131, "cannot close fos");
					e1.printStackTrace();
				}
			e.printStackTrace();
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e1) {
					Printer.w(139, "finally cannot close fos neither!");
					e1.printStackTrace();
				}
		}
		return false;
	}

	public static boolean writeBytes(OutStream os, String path) {
		os.terminate();
		byte[] md5 = os.MD5();
		File f = new File(path);
		boolean suc;
		if (!(suc = writeFile(os, f, md5))) {
			ps.println("failed to write file: " + f.getPath());
		}
		return suc;
	}

	private static boolean writeFile(OutStream os, File f, byte[] md5) {
		boolean suc = check(f);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			os.flush(fos);
			fos.close();
		} catch (IOException e) {
			suc = false;
			e.printStackTrace();
			Printer.w(130, "IOE!!!");
			Opts.ioErr("failed to write file " + f);
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			e.printStackTrace();
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
		try {
			byte[] cont = Files.readAllBytes(f.toPath());
			byte[] nmd = MessageDigest.getInstance("MD5").digest(cont);
			suc &= Arrays.equals(md5, nmd);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return suc;
	}

	private static void writeOptions() {
		PrintStream out = newFile("./user/data.ini");
		Rectangle r = MainFrame.crect;
		out.println("rect= {" + r.x + ',' + r.y + ',' + r.width + ',' + r.height + '}');
		out.close();
	}

}

class WriteStream extends PrintStream {

	protected boolean writed = false;

	public WriteStream(File file) throws FileNotFoundException {
		super(file);
	}

	@Override
	public void println(Object str) {
		super.println(str);
		writed = true;
	}

	@Override
	public void println(String str) {
		super.println(str);
		writed = true;
	}

}
