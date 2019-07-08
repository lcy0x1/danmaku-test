package battle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;

import battle.Shape.PosShape;
import jogl.util.FakeGraphics;
import jogl.util.FakeGraphics.Coord;
import jogl.util.FakeGraphics.Curve;
import main.MainTH;
import jogl.util.GLImage;
import util.Data;
import util.P;

public class Sprite implements Comparable<Sprite> {

	public static class CSP implements SParam {

		private final Sprite s;
		private final int mode;
		private final double r;

		public CSP(int id, int t, double m) {
			this(get(id), t, m);
		}

		private CSP(Sprite spr, int t, double m) {
			s = spr;
			mode = t;
			r = s.size * m * MAGNIFY;
		}

		@Override
		public DotESprite getEntity(DotESprite.Dire d) {
			return null;
		}

		@Override
		public CurveESprite getEntity(Shape.LineSegs d) {
			return new CurveESprite(d, r, s, mode & 1, mode >> 1);
		}

		@Override
		public double getRadius() {
			return r;
		}

		@Override
		public PosShape getShape(P pos) {
			return new Shape.Circle(pos, r);
		}

	}

	public static interface CSParam {

		public CurveESprite getEntity(Shape.LineSegs d);

		public double getRadius();

	}

	public static class CurveESprite {

		private final Sprite s;
		private double r;
		private int mode, rev;
		private Shape.LineSegs dire;

		private CurveESprite(Shape.LineSegs d, double ra, Sprite img, int m, int rv) {
			s = img;
			dire = d;
			r = ra;
			mode = m;
			rev = rv;
		}

		public void draw() {
			double h = Engine.BOUND.y;
			for (P[] ps : dire.getPos()) {
				if (ps.length == 1)
					continue;
				P[] np = new P[ps.length];
				for (int i = 0; i < ps.length; i++)
					np[i] = ps[i].copy();
				Curve c = new Curve(LONG_END, LONG_EDR, r, np, rev);
				c.times(1 / h);
				Engine.RENDERING.getPool(s).addCurve(c, mode | (s.horiz ? 0 : 2));
			}
		}

	}

	public static class DotESprite {

		public static interface Dire {

			public double getDire();

			public P getPos();

			public int getTime();

		}

		private final Sprite s;
		private final double w, h;
		private final int mode;
		private final Dire dire;

		private DotESprite(Dire d, double sw, double sh, Sprite img, int m) {
			s = img;
			dire = d;
			w = sw;
			h = sh;
			mode = m;
		}

		public void draw() {
			double r = Engine.BOUND.y;
			P pos = dire.getPos();
			double d = dire.getDire();
			if (s.roting)
				d += ROTRATE * dire.getTime();
			Coord c = new Coord(pos.x, pos.y, w, h, d + (s.horiz ? 0 : Math.PI / 2));
			c.times(1 / r);
			Engine.RENDERING.getPool(s).addDot(c, mode);
		}

		public double radius() {
			return new P(w, h).times(s.piv).toBound(w, h);
		}

	}

	public static class DSP implements SParam {

		private final Sprite s;
		private final int mode;
		private final double r;

		public DSP(int id, int t, double m) {
			this(get(id), t, m);
		}

		private DSP(Sprite spr, int t, double m) {
			s = spr;
			mode = t;
			r = s.size * m * MAGNIFY;
		}

		@Override
		public DotESprite getEntity(DotESprite.Dire d) {
			if (s == null || s.id / 100 == 114)
				return null;
			return new DotESprite(d, r * 2, r * 2, s, mode);
		}

		@Override
		public CurveESprite getEntity(Shape.LineSegs d) {
			return new CurveESprite(d, r, s, mode & 1, mode >> 1);
		}

		@Override
		public double getRadius() {
			return r;
		}

		@Override
		public PosShape getShape(P pos) {
			return new Shape.Circle(pos, r);
		}

	}

	public static interface DSParam {

		public DotESprite getEntity(DotESprite.Dire d);

		public Shape.PosShape getShape(P pos);

	}

	public static class Pool {

		private static class SubPool {

			private final Sprite s;
			private final List<Coord> reg = new ArrayList<>();
			private final List<Coord> lit = new ArrayList<>();
			private final List<Curve> rgc = new ArrayList<>();
			private final List<Curve> ltc = new ArrayList<>();

			private SubPool(Sprite sp) {
				s = sp;
			}

			private void addCurve(Curve c, int mode) {
				(mode == 0 ? rgc : ltc).add(c);
			}

			private void addDot(Coord c, int mode) {
				(mode == 0 ? reg : lit).add(c);
			}

			private void debug(FakeGraphics fg) {
				int n = 0;
				for (Curve c : rgc)
					n += c.ps.length;
				for (Curve c : ltc)
					n += c.ps.length;
				Coord[] cs = new Coord[n + reg.size() + lit.size()];
				int i = 0;
				for (Curve c : rgc)
					for (P p : c.ps)
						cs[i++] = new Coord(p.x, p.y, c.r, c.r, 0);
				for (Curve c : ltc)
					for (P p : c.ps)
						cs[i++] = new Coord(p.x, p.y, c.r, c.r, 0);
				for (Curve c : ltc)
					n += c.ps.length;
				for (Coord c : reg)
					cs[i++] = c;
				for (Coord c : lit)
					cs[i++] = c;
				fg.drawCircles(cs);
			}

			private void flush(FakeGraphics fg) {
				fg.setComposite(FakeGraphics.BLEND, 256, 0);
				for (Curve c : rgc)
					fg.drawCurve(s, c);
				if (reg.size() > 0)
					fg.drawImages(s, reg.size(), reg.toArray(new Coord[0]));

				fg.setComposite(FakeGraphics.BLEND, 256, 1);
				for (Curve c : ltc)
					fg.drawCurve(s, c);
				if (lit.size() > 0)
					fg.drawImages(s, lit.size(), lit.toArray(new Coord[0]));

			}

		}

		private Map<Sprite, SubPool> map = new TreeMap<>();

		public void flush(FakeGraphics fg) {
			if (Data.DRAWSPRITE)
				map.forEach((s, p) -> p.flush(fg));
			if (Data.DEBUG)
				map.forEach((s, p) -> p.debug(fg));
			map.clear();
		}

		private SubPool getPool(Sprite s) {
			SubPool p;
			if (!map.containsKey(s))
				map.put(s, p = new SubPool(s));
			else
				p = map.get(s);
			return p;
		}

	}

	public static interface SParam extends DSParam, CSParam {

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

	public static final double MAGNIFY = 1.5;

	private static final double LONG_END = 6.0 / 256, LONG_EDR = 6.0 / 16;
	private static final double ROTRATE = Math.PI / 3000;

	private static final int[] ROT = { 0, 110, 111, 201 };

	private static final double[][] SIZE = { { 2 },
			{ 0, 2.4, 4, 4, 2.4, 2.4, 2.4, 2.8, 2.4, 2.4, 4, 0, 2.4, 2.4, 4, 2.4 }, { 0, 7, 8.5, 7, 6, 7, 0, 10 },
			{ 14, 14 } };

	public static final double DEFRAD = 10;

	public static Sprite get(int id) {
		return TOT[id / 10000][id / 100 % 100][id % 100];
	}

	public static void read() {
		String path = (MainTH.WRITE ? "/src/" : "/") + "assets/bullet_000.png";
		GLImage gli;
		try {
			gli = GLImage.build(IOUtils.resourceToByteArray(path));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
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
	public final double rad, size;

	private final boolean horiz, roting;

	private Sprite(GLImage gl, int lv) {
		img = gl;
		id = lv;
		piv = new P(0.5, 0.5);
		horiz = id / 100 == 114;
		size = SIZE[lv / 10000][lv / 100 % 100];
		rad = size * 2 / gl.getHeight();

		boolean b = false;
		for (int i : ROT)
			if (id / 100 == i)
				b = true;
		roting = b;
	}

	@Override
	public int compareTo(Sprite o) {
		return Integer.compare(getLayer(), o.getLayer());
	}

	private int getLayer() {
		if (id / 100 == 301)
			return -id + 20200;
		return -id;// FIXME
	}

}
