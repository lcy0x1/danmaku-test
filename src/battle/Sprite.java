package battle;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import jogl.util.FakeGraphics;
import jogl.util.FakeGraphics.Coord;
import jogl.util.GLImage;
import util.Data;
import util.P;

public class Sprite implements Comparable<Sprite> {

	public static class DESParam {

		public final Sprite s;

		public final int mode;

		public final double r;

		public DESParam(int id, int t, double m) {
			this(get(id), t, m);
		}

		public DESParam(Sprite spr, int t, double m) {
			s = spr;
			mode = t;
			r = m * MAGNIFY;
		}

		public DotESprite getEntity(DotESprite.Dire d) {
			return new DotESprite(d, s.size * r, s.size * r, s, mode);
		}

	}

	public static class DotESprite {

		public static interface Dire {

			public double getDire();

			public P getPos();

		}

		public final Sprite s;

		private double w, h;

		private int mode;

		private Dire dire;

		public DotESprite(Dire d, double sw, double sh, Sprite img, int m) {
			s = img;
			dire = d;
			w = sw;
			h = sh;
			mode = m;
		}

		public void draw() {
			Engine.RENDERING.draw(s, dire.getPos(), w, h, dire.getDire(), mode);
		}

		public double radius() {
			return new P(w, h).times(s.piv).toBound(w, h);
		}

	}

	public static class Pool {

		private static class DotPool extends SubPool {

			private DotPool(Sprite sp) {
				super(sp);
			}

			@Override
			void debug(FakeGraphics fg, int mode) {
				Queue<Coord> qs = mode == 0 ? reg : lit;
				fg.drawCircles(s, qs.size(), qs.toArray(new Coord[0]));
			}

			@Override
			void flush(FakeGraphics fg, int mode) {
				Queue<Coord> qs = mode == 0 ? reg : lit;
				fg.setComposite(FakeGraphics.BLEND, 256, mode);
				fg.drawImages(s, qs.size(), qs.toArray(new Coord[0]));
			}

		}

		private static abstract class SubPool {

			final Sprite s;

			Queue<Coord> reg = new ArrayDeque<>();
			Queue<Coord> lit = new ArrayDeque<>();

			SubPool(Sprite sp) {
				s = sp;
			}

			abstract void debug(FakeGraphics fg, int mode);

			abstract void flush(FakeGraphics fg, int mode);

		}

		private Map<Sprite, SubPool> map = new TreeMap<>();

		public void flush(FakeGraphics fg) {
			if (Data.DRAWSPRITE)
				map.forEach((s, p) -> {
					p.flush(fg, 0);
					p.flush(fg, 1);
				});
			if (Data.DEBUG)
				map.forEach((s, p) -> {
					p.debug(fg, 0);
					p.debug(fg, 1);
				});
			map.clear();
		}

		private void draw(Sprite s, P pos, double w, double h, double a, int mode) {
			SubPool p;
			if (!map.containsKey(s))
				map.put(s, p = new DotPool(s));
			else
				p = map.get(s);
			double r = Engine.BOUND.y;
			Coord c = new Coord(pos.x, pos.y, w, h, a + s.rot);
			c.times(1 / r);
			(mode == 0 ? p.reg : p.lit).add(c);
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
	public static final Sprite[][] NON = new Sprite[1][1];
	public static final Sprite[][] REG = new Sprite[16][16];
	public static final Sprite[][] OCT = new Sprite[8][8];
	public static final Sprite[][] LRG = new Sprite[2][4];
	public static final Sprite[][] OTH = new Sprite[4][];

	public static final Sprite[][][] TOT = { NON, REG, OCT, LRG, OTH };

	public static final double MAGNIFY = 3;

	private static final double[][] SIZE = { { 2 },
			{ 0, 2.4, 4, 4, 2.4, 2.4, 2.4, 2.8, 2.4, 2.4, 4, 0, 2.4, 2.4, 2.4, 2.4 }, { 0, 7, 8.5, 7, 6, 7, 0, 10 },
			{ 14, 14 } };

	public static Sprite get(int id) {
		return TOT[id / 10000][id / 100 % 100][id % 100];
	}

	public static void read() {
		File f = new File(Sprite.class.getResource("/assets/bullet_000.png").getFile());
		GLImage gli = GLImage.build(f);
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
			LRG[SLB_BALL][i] = new Sprite(gli.getSubimage(1 + i * 64, 449, 64, 64), 30000 + i);
			LRG[SLB_ROSE][i] = new Sprite(gli.getSubimage(258 + i * 64, 290, 64, 64), 30100 + i);
		}

		NON[0][0] = new Sprite(gli.getSubimage(258, 17, 64, 64), 0);

	}

	public final GLImage img;
	private final int id;

	public final P piv;
	public final double rot, rad, size;

	private Sprite(GLImage gl, int lv) {
		img = gl;
		id = lv;
		piv = new P(0.5, 0.5);
		rot = id / 100 == 114 ? 0 : Math.PI / 2;
		size = SIZE[lv / 10000][lv / 100 % 100];
		rad = size * 2 / gl.getHeight();
	}

	@Override
	public int compareTo(Sprite o) {
		return Integer.compare(getLayer(), o.getLayer());
	}

	private int getLayer() {
		return -id;// FIXME
	}

}
