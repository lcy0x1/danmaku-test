package entity;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import jogl.util.FakeGraphics;
import jogl.util.GLImage;

public class Sprite implements Comparable<Sprite> {

	public static class Pool {

		private static class Coord {

			private double x, y, w, h, a;

			private Coord(double x2, double y2, double w2, double h2, double a2) {
				x = x2;
				y = y2;
				w = w2;
				h = h2;
				a = a2;
			}

		}

		private static class SubPool {

			private final Sprite s;

			private Queue<Coord> reg = new ArrayDeque<>();
			private Queue<Coord> lit = new ArrayDeque<>();

			private SubPool(Sprite sp) {
				s = sp;
			}

			private void flush(FakeGraphics fg, int mode) {
				Queue<Coord> qs = mode == 0 ? reg : lit;
				int n = qs.size();
				double[] x = new double[n];
				double[] y = new double[n];
				double[] w = new double[n];
				double[] h = new double[n];
				double[] a = new double[n];
				int i = 0;
				for (Coord c : qs) {
					x[i] = c.x;
					y[i] = c.y;
					w[i] = c.w;
					h[i] = c.h;
					a[i] = c.a;
				}
				fg.drawImages(s.img, n, x, y, w, h, a);
			}

		}

		private Map<Sprite, SubPool> map = new TreeMap<>();

		public void draw(Sprite s, double x, double y, double w, double h, double a, int mode) {
			SubPool p;
			if (!map.containsKey(s))
				map.put(s, p = new SubPool(s));
			else
				p = map.get(s);
			(mode == 0 ? p.reg : p.lit).add(new Coord(x, y, w, h, a));
		}

		public void flush(FakeGraphics fg) {
			map.forEach((s, p) -> {
				p.flush(fg, 0);
				p.flush(fg, 1);
			});
			map.clear();
		}

	}

	public static final int SRC_GREY = 0;
	public static final int SRC_REDX = 1;
	public static final int SRC_RED = 2;
	public static final int SRC_PINKX = 3;
	public static final int SRC_PINK = 4;
	public static final int SRC_BLUEX = 5;
	public static final int SRC_BLUE = 6;
	public static final int SRC_CYANX = 7;
	public static final int SRC_CYAN = 8;
	public static final int SRC_GREENX = 9;
	public static final int SRC_GREEN = 10;
	public static final int SRC_LEMON = 11;
	public static final int SRC_YELLOWX = 12;
	public static final int SRC_YELLOW = 13;
	public static final int SRC_ORANGE = 14;
	public static final int SRC_WHITE = 15;

	public static final int SRB_SLASER = 0;
	public static final int SRB_SCALE = 1;
	public static final int SRB_CIRCLE = 2;
	public static final int SRB_BALL = 3;
	public static final int SRB_OVAL = 4;
	public static final int SRB_NIDDLE = 5;
	public static final int SRB_KNIFE = 6;
	public static final int SRB_SPELL = 7;
	public static final int SRB_BULLET = 8;
	public static final int SRB_OVALX = 9;
	public static final int SRB_STAR = 10;
	public static final int SRB_PLANET = 11;
	public static final int SRB_S_CROSS = 12;
	public static final int SRB_S_BALL = 13;
	public static final int SRB_LONG = 14;
	public static final int SRB_DROP = 15;

	public static final int SOC_GREY = 0;
	public static final int SOC_RED = 1;
	public static final int SOC_PINK = 2;
	public static final int SOC_BLUE = 3;
	public static final int SOC_CYAN = 4;
	public static final int SOC_GREEN = 5;
	public static final int SOC_YELLOW = 6;
	public static final int SOC_WHITE = 7;

	public static final int SOB_LIGHT = 0;
	public static final int SOB_STAR = 1;
	public static final int SOB_BALL = 2;
	public static final int SOB_BUTTERFLY = 3;
	public static final int SOB_KNIFE = 4;
	public static final int SOB_OVAL = 5;
	public static final int SOB_LIGHTX = 6;
	public static final int SOB_HEART = 7;

	public static final int SLC_RED = 0;
	public static final int SLC_BLUE = 1;
	public static final int SLC_GREEN = 2;
	public static final int SLC_YELLOW = 3;

	public static final int SLB_BALL = 0;
	public static final int SLB_ROSE = 1;

	public static final Sprite[][] REG = new Sprite[16][16];
	public static final Sprite[][] OCT = new Sprite[8][8];
	public static final Sprite[][] LRG = new Sprite[2][4];

	public static final Sprite[][] OTH = new Sprite[4][];

	public static void read() {

		GLImage gli = GLImage.build(new File("./assets/bullet_000.png"));
		for (int i = 0; i < 12; i++)
			for (int j = 0; j < 16; j++)
				REG[i][j] = new Sprite(gli.getSubimage(1 + j * 16, 1 + i * 16, 16, 16), 10000 + i * 100 + j);
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 8; j++) {
				REG[SRB_S_CROSS][i * 8 + j] = new Sprite(gli.getSubimage(1 + j * 8, 193 + i * 8, 8, 8),
						11200 + i * 8 + j);
				REG[SRB_S_BALL][i * 8 + j] = new Sprite(gli.getSubimage(1 + j * 8, 241 + i * 8, 8, 8),
						11300 + i * 8 + j);
			}
		for (int i = 0; i < 16; i++) {
			REG[SRB_LONG][i] = new Sprite(gli.getSubimage(1, 515 + i * 16, 256, 16), 11400 + i);
			REG[SRB_DROP][i] = new Sprite(gli.getSubimage(258 + i * 16, 449, 16, 16), 11500 + i);
		}
		for (int i = 0; i < 8; i++) {
			OCT[SOB_LIGHT][i] = new Sprite(gli.getSubimage(1 + i * 32, 209, 32, 32), 20000 + i);
			OCT[SOB_HEART][i] = new Sprite(gli.getSubimage(258 + i * 32, 257, 32, 32), 20700 + i);
		}
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 8; j++)
				OCT[i + 1][j] = new Sprite(gli.getSubimage(1 + j * 32, 257 + i * 32, 32, 32), 20100 + i * 100 + j);

		for (int i = 0; i < 4; i++) {
			LRG[SLB_BALL][i] = new Sprite(gli.getSubimage(1 + i * 64, 449, 64, 64), 30100 + i);
			LRG[SLB_ROSE][i] = new Sprite(gli.getSubimage(258 + i * 64, 290, 64, 64), 30200 + i);
		}

	}

	private final GLImage img;
	private final int id;

	private Sprite(GLImage gl, int lv) {
		img = gl;
		id = lv;
	}

	@Override
	public int compareTo(Sprite o) {
		return Integer.compare(getLayer(), o.getLayer());
	}

	private int getLayer() {
		return id;// FXIME
	}

}
