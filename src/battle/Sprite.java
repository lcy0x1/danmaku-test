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
import jogl.util.GLImage;
import main.MainTH;
import util.Data;
import util.FM;
import util.P;

public class Sprite implements Comparable<Sprite> {

	public static interface CSParam {

		public ESprite getEntity(Shape.LineSegs d);

		public double getRadius();

	}

	public static interface Dire {

		public double getDire();

		public P getPos();

		public int getTime();

	}

	public static interface DSParam {

		public ESprite getEntity(Dire d);

		public Shape.PosShape getShape(P pos);

	}

	public static interface ESprite {

		public void draw();

		public double radius();

	}

	public static class Pool {

		private static class SubPool {

			private final Sprite s;
			private final List<Coord> reg = new ArrayList<>();
			private final List<Coord> lit = new ArrayList<>();
			private final List<Curve> rgc = new ArrayList<>();
			private final List<Curve> ltc = new ArrayList<>();
			private final Map<Integer, List<Coord>> hlf = new TreeMap<>();

			private SubPool(Sprite sp) {
				s = sp;
			}

			private void addCurve(Curve c, int mode) {
				(mode == 0 ? rgc : ltc).add(c);
			}

			private void addDot(Coord c, int mode, int tra) {
				if (tra == MAX_TRA)
					(mode == 0 ? reg : lit).add(c);
				else {
					List<Coord> list = null;
					if (hlf.containsKey(tra))
						list = hlf.get(tra);
					else
						hlf.put(tra, list = new ArrayList<Coord>());
					list.add(c);
				}
			}

			private int count() {
				int n = reg.size() + lit.size();
				for (Curve c : rgc)
					n += c.ps.length;
				for (Curve c : ltc)
					n += c.ps.length;
				for (List<Coord> lc : hlf.values())
					n += lc.size();
				return n;
			}

			private void debug(FakeGraphics fg) {
				int n = reg.size() + lit.size();
				for (Curve c : rgc)
					n += c.ps.length;
				for (Curve c : ltc)
					n += c.ps.length;
				for (List<Coord> lc : hlf.values())
					n += lc.size();
				Coord[] cs = new Coord[n];
				int i = 0;
				for (Curve c : rgc)
					for (P p : c.ps)
						cs[i++] = new Coord(p.x, p.y, c.r * 2, c.r * 2, 0);
				for (Curve c : ltc)
					for (P p : c.ps)
						cs[i++] = new Coord(p.x, p.y, c.r * 2, c.r * 2, 0);
				for (Curve c : ltc)
					n += c.ps.length;
				for (Coord c : reg)
					cs[i++] = c;
				for (Coord c : lit)
					cs[i++] = c;
				for (List<Coord> lc : hlf.values())
					for (Coord c : lc)
						cs[i++] = c;
				fg.drawCircles(cs);
			}

			private void flush(FakeGraphics fg) {
				hlf.forEach((i, l) -> {
					fg.setComposite(FakeGraphics.BLEND, i, 0);
					fg.drawImages(s, l.size(), l.toArray(new Coord[0]));
				});

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

		private Map<Integer, Map<Sprite, SubPool>> map = new TreeMap<>();

		public int count() {
			int[] sum = new int[1];
			map.forEach((i, m) -> m.forEach((s, p) -> sum[0] += p.count()));
			return sum[0];
		}

		public void flush(FakeGraphics fg) {
			if (Data.DRAWSPRITE)
				map.forEach((i, m) -> m.forEach((s, p) -> p.flush(fg)));
			if (Data.DEBUG)
				map.forEach((i, m) -> m.forEach((s, p) -> p.debug(fg)));
			map.clear();
		}

		private SubPool getPool(Sprite s, int layer) {
			Map<Sprite, SubPool> m;

			if (!map.containsKey(layer))
				map.put(layer, m = new TreeMap<Sprite, SubPool>());
			else
				m = map.get(layer);

			SubPool p;
			if (!m.containsKey(s))
				m.put(s, p = new SubPool(s));
			else
				p = m.get(s);
			return p;
		}

	}

	public static interface SParam extends DSParam, CSParam {

	}

	private static class BossESprite implements ESprite {

		private final int id;
		private final double w, h;
		private final int layer;
		private final Dire dire;

		private BossESprite(int boss, Dire d, double sw, double sh, int lay) {
			id = boss;
			dire = d;
			w = sw;
			h = sh;
			layer = lay;
		}

		@Override
		public void draw() {
			double r = Engine.BOUND.y;
			P pos = dire.getPos();
			// double d = dire.getDire(); TODO
			Sprite s = BOSS[id][0];
			Coord c = new Coord(pos.x, pos.y, w, h, 0);
			c.times(1 / r);
			Engine.RENDERING.getPool(s, layer).addDot(c, 0, MAX_TRA);
		}

		@Override
		public double radius() {
			return new P(w, h).times(0.5).abs();
		}

	}

	private static class BSP implements DSParam {

		private final int s;
		private final int mode, layer;
		private final double r;

		private BSP(int spr, int t, double m, int lay) {
			s = spr;
			mode = t;
			layer = lay;
			r = BOSS[s][0].size * m * MAGNIFY;
		}

		@Override
		public ESprite getEntity(Dire d) {
			return new BossESprite(s, d, r * 2, r * 2, layer);
		}

		@Override
		public PosShape getShape(P pos) {
			if (mode == 2)
				return new Shape.NonShape(pos);
			return new Shape.Circle(pos, r);
		}

	}

	private static class CSP implements SParam {

		private final Sprite s;
		private final int mode, layer;
		private final double r;

		private CSP(int id, int t, double m, int layer) {
			this(get(id), t, m, layer);
		}

		private CSP(Sprite spr, int t, double m, int lay) {
			s = spr;
			mode = t;
			layer = lay;
			r = s.size * m * MAGNIFY;
		}

		@Override
		public ESprite getEntity(Dire d) {
			return new Radius(DEFRAD);
		}

		@Override
		public ESprite getEntity(Shape.LineSegs d) {
			return new CurveESprite(d, r, s, mode & 1, mode >> 1, layer);
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

	private static class CurveESprite implements ESprite {

		private final Sprite s;
		private final double r;
		private final int mode, rev, layer;
		private final Shape.LineSegs dire;

		private CurveESprite(Shape.LineSegs d, double ra, Sprite img, int m, int rv, int l) {
			s = img;
			dire = d;
			r = ra;
			mode = m;
			rev = rv;
			layer = l;
		}

		@Override
		public void draw() {
			double h = Engine.BOUND.y;
			for (P[] ps : dire.getPos()) {
				if (ps.length == 1)
					continue;
				P[] np = new P[ps.length];
				for (int i = 0; i < ps.length; i++)
					np[i] = ps[i].copy();
				Curve c = getCurve(s.id, rev, r, np, 0);
				c.times(1 / h);
				Engine.RENDERING.getPool(s, layer).addCurve(c, mode);
			}
		}

		@Override
		public double radius() {
			return r / s.rad;
		}

	}

	private static class DotCurveSprite implements ESprite {

		private final Sprite s;
		private final double r, max, maxc;
		private final int mode, rev, layer;
		private final boolean conn;
		private final Shape.LineSegs dire;

		private DotCurveSprite(Shape.LineSegs d, double ra, Sprite img, int m, int rv, double ma, double mc,
				boolean con, int lay) {
			s = img;
			dire = d;
			r = ra;
			mode = m;
			rev = rv;
			max = ma;
			maxc = mc;
			conn = con;
			layer = lay;
		}

		@Override
		public void draw() {
			List<P> list = new ArrayList<>();
			for (P[] ps : dire.getPos()) {
				if (ps.length == 1)
					continue;
				for (int i = 0; i < ps.length; i++) {
					if (list.size() < 2) {
						list.add(ps[i]);
						continue;
					} else {
						double rx = r / s.rad;
						P p0 = list.get(list.size() - 2);
						P p1 = list.get(list.size() - 1);
						P p2 = ps[i];
						double d0 = p0.dis(p1);
						double d1 = p1.dis(p2);
						double ax = FM.PI - FM.atan2(rx, d0) - FM.atan2(rx, d1);
						if (ax > max)
							ax = max;
						double a1 = p0.atan2(p1) - p1.atan2(p2);
						if (FM.cos(ax) > FM.cos(a1)) {
							if (conn && i < ps.length - 1) {
								P p3 = ps[i + 1];
								P pv = connect(p0, p1, p2, p3);
								if (pv != null)
									list.add(pv);
								subdraw(list);
								if (pv != null)
									list.add(pv);
							} else
								subdraw(list);
						}
						list.add(p2);
					}
				}
				subdraw(list);
			}
		}

		@Override
		public double radius() {
			return r / s.rad;
		}

		private P connect(P p0, P p1, P p2, P p3) {
			P dp = p0.sf(p1);
			double den = dp.crossP(p3.sf(p2));
			if (den == 0 || Double.isNaN(den))
				return null;
			double t = p0.sf(p3).crossP(dp) / den;
			return p3.middle(p2, t);
		}

		private void subdraw(List<P> list) {
			if (list.size() > 1) {
				double h = Engine.BOUND.y;
				P[] np = new P[list.size()];
				double l = 0;
				for (int i = 0; i < np.length; i++) {
					np[i] = list.get(i).copy();
					if (i != 0)
						l += np[i - 1].dis(np[i]);
				}
				if (1 - np[0].dis(np[np.length - 1]) / l < maxc)
					np = new P[] { np[0], np[np.length - 1] };
				Curve c = getCurve(s.id, rev, r, np, 0);
				c.times(1 / h);
				Engine.RENDERING.getPool(s, layer).addCurve(c, mode);
			}
			list.clear();
		}

	}

	private static class DotESprite implements ESprite {

		private final Sprite s;
		private final double w, h, radius;
		private final int mode, layer, tra;
		private final Dire dire;

		private DotESprite(Dire d, double sw, double sh, Sprite img, int m, int lay, int trans) {
			s = img;
			dire = d;
			w = sw;
			h = sh;
			mode = m;
			layer = lay;
			tra = trans;
			radius = new P(w, h).times(s.piv).toBound(w, h) / s.rad;
		}

		@Override
		public void draw() {
			double r = Engine.BOUND.y;
			P pos = dire.getPos();
			double d = dire.getDire();
			if (s.roting)
				d += ROTRATE * dire.getTime();
			Coord c = new Coord(pos.x, pos.y, w, h, d + (s.horiz ? 0 : FM.PI / 2));
			c.times(1 / r);
			Engine.RENDERING.getPool(s, layer).addDot(c, mode, tra);
		}

		@Override
		public double radius() {
			return radius;
		}

	}

	private static class DSP implements SParam {

		private final Sprite s;
		private final int mode, layer, tra;
		private final double r;

		private DSP(int id, int t, double m, int layer, int tra) {
			this(get(id), t, m, layer, tra);
		}

		private DSP(Sprite spr, int t, double m, int lay, int trans) {
			s = spr;
			mode = t;
			layer = lay;
			tra = trans;
			r = s.size * m * MAGNIFY;
		}

		@Override
		public ESprite getEntity(Dire d) {
			if (s == null || s.id / 100 == 114)
				return null;
			return new DotESprite(d, r * 2, r * 2, s, mode, layer, tra);
		}

		@Override
		public ESprite getEntity(Shape.LineSegs d) {
			return new CurveESprite(d, r, s, mode & 1, mode >> 1, layer);
		}

		@Override
		public double getRadius() {
			return r;
		}

		@Override
		public PosShape getShape(P pos) {
			if (mode == 2)
				return new Shape.NonShape(pos);
			return new Shape.Circle(pos, r);
		}

	}

	private static class Radius implements ESprite {

		private final double r;

		private Radius(double ra) {
			r = ra;
		}

		@Override
		public void draw() {
		}

		@Override
		public double radius() {
			return r;
		}

	}

	private static class RCSP extends CSP {

		private final double ma, mc;
		private final boolean conn;

		private RCSP(int id, int t, double m, double max, double maxc, boolean con, int layer) {
			this(get(id), t, m, max, maxc, con, layer);
		}

		private RCSP(Sprite spr, int t, double m, double max, double maxc, boolean con, int layer) {
			super(spr, t, m, layer);
			ma = max;
			conn = con;
			mc = maxc;
		}

		@Override
		public ESprite getEntity(Shape.LineSegs d) {
			int mode = super.mode;
			int l = super.layer;
			return new DotCurveSprite(d, super.r, super.s, mode & 1, mode >> 1, ma, mc, conn, l);
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

	public static final int BS_SAKUYA = 0;

	public static final Sprite[][] NON = new Sprite[1][2];
	public static final Sprite[][] REG = new Sprite[16][16];
	public static final Sprite[][] OCT = new Sprite[8][8];
	public static final Sprite[][] LRG = new Sprite[2][4];
	public static final Sprite[][] OTH = new Sprite[4][];

	public static final Sprite[][][] TOT = { NON, REG, OCT, LRG, OTH };

	public static final Sprite[][] BOSS = new Sprite[1][];

	public static final int P_D = 0, P_C = 1, P_CR = 2, P_SR = 3;

	public static final int L_BG = Integer.MIN_VALUE;
	public static final int L_TOP = Integer.MAX_VALUE;
	public static final int L_PLATK = -2 << 8;
	public static final int L_BOSS = 1 << 8;

	public static final double DEFRAD = 20;

	private static final int ROT_CONST = 3000;
	private static final int BOSSSIZE = 30;
	private static final int MAX_TRA = 256;

	private static final double MAGNIFY = 1.5;

	private static final double LONG_END = 6.0 / 256;
	private static final double LONG_EDR = 6.0 / 16;
	private static final double LONG_SEG = 6.0 / 256;
	private static final double OVAL_END = 2.0 / 16;
	private static final double OVAL_EDR = 4.0 / 16;
	private static final double OVAL_SEG = 1.0 / 16;

	private static final double ROTRATE = FM.PI / ROT_CONST;
	private static final double MAX_ANGLE = FM.PI / 4;
	private static final double MIN_ANGLE = 1e-3;
	private static final double MAX_CURVE = 1e-3;

	private static final int[] ROT = { 0, 110, 111, 201 };

	private static final double[][] SIZE = { { 2 },
			{ 0, 2.4, 4, 4, 2.4, 2.4, 2.4, 2.8, 2.4, 2.4, 4, 0, 2.4, 2.4, 2.4, 2.4 }, { 6, 7, 8.5, 7, 6, 7, 0, 10 },
			{ 14, 14 } };

	public static DSParam getBoss(int boss) {
		return new BSP(BS_SAKUYA, 0, 1, L_BOSS);// TODO add more bosses later
	}

	/**
	 * type: P_CR (curve reflection) and P_SR (straight reflection)<br>
	 * <br>
	 * mode: +1: highlight, +2: reverse direction<br>
	 * <br>
	 * a: max angle between 2 points to be considered the same curve<br>
	 * <br>
	 * c: min curvature to be considered not straight<br>
	 * CR means 0 and SR means a small endurance <br>
	 * <br>
	 * b: when not in the same curve, generate a connecting dot<br>
	 * CR means false for b and SR means true for b
	 */
	public static SParam getCurve(int id, int mode, double mult, double a, double c, boolean b) {
		return new RCSP(id, mode, mult, a, c, b, 0);
	}

	/**
	 * type: P_C (normal curve), P_CR (curve reflection), P_SR (straight
	 * reflection)<br>
	 * mode: +1: hight light, +2: reverse direction
	 */
	public static SParam getCurve(int type, int id, int mode, double mult) {
		return getSprite(type, id, mode, mult, 0);
	}

	/** mode: 0: normal, 1: highlight, 2: no box */
	public static SParam getDot(int id, int mode) {
		return getDot(id, mode, 1, 0, MAX_TRA);
	}

	/** mode: 0: normal, 1: highlight, 2: no box */
	public static SParam getDot(int id, int mode, double mult, int layer) {
		return getDot(id, mode, mult, layer, MAX_TRA);
	}

	/** mode: 0: normal, 1: highlight, 2: no box */
	public static SParam getDot(int id, int mode, double mult, int layer, int tra) {
		return new DSP(id, mode, mult, layer, tra);
	}

	/** mode: 0: normal, 1: highlight, 2: no box */
	public static SParam getDot(int id, int mode, int layer) {
		return getDot(id, mode, 1, layer, MAX_TRA);
	}

	public static void read() {
		readBullets();
		readBoss();
	}

	private static Sprite get(int id) {
		if (id == -1)
			return null;
		return TOT[id / 10000][id / 100 % 100][id % 100];
	}

	private static Curve getCurve(int id, int rev, double r, P[] np, int mode) {
		double end, edr, seg;
		if (id / 100 == 114) {
			end = LONG_END;
			edr = LONG_EDR;
			seg = LONG_SEG;
		} else if (id / 100 == 104) {
			end = OVAL_END;
			edr = OVAL_EDR;
			seg = OVAL_SEG;
			rev += 2;
		} else
			return null;
		if (mode == 1) {
			edr = 0;
			end = seg;
		}
		return new Curve(end, edr, r, np, rev);
	}

	private static SParam getSprite(int type, int id, int mode, double mult, int layer) {
		if (type == P_D)
			return new DSP(id, mode, mult, layer, MAX_TRA);
		if (type == P_C)
			return new CSP(id, mode, mult, layer);
		if (type == P_CR)
			return new RCSP(id, mode, mult, MAX_ANGLE, 0, false, layer);
		if (type == P_SR)
			return new RCSP(id, mode, mult, MIN_ANGLE, MAX_CURVE, true, layer);
		return null;
	}

	private static void readBoss() {
		String path = (MainTH.WRITE ? "/src/" : "/") + "assets/Sakuya.png";
		GLImage gli;
		try {
			gli = GLImage.build(IOUtils.resourceToByteArray(path));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		BOSS[BS_SAKUYA] = new Sprite[8];
		for (int i = 0; i < 8; i++)
			BOSS[BS_SAKUYA][i] = new Sprite(gli.getSubimage(i % 4 * 64, i / 4 * 64, 64, 64));

	}

	private static void readBullets() {
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
				new Sprite(gli.getSubimage(1 + j * 16, 1 + i * 16, 16, 16), 10000 + i * 100 + j);
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 8; j++) {
				new Sprite(gli.getSubimage(1 + j * 8, 193 + i * 8, 8, 8), 11200 + i * 8 + j);
				new Sprite(gli.getSubimage(1 + j * 8, 241 + i * 8, 8, 8), 11300 + i * 8 + j);
			}
		for (int i = 0; i < 16; i++) {
			new Sprite(gli.getSubimage(1, 515 + i * 16, 256, 16), 11400 + i);
			new Sprite(gli.getSubimage(258 + i * 16, 449, 16, 16), 11500 + i);
		}
		for (int i = 0; i < 8; i++) {
			new Sprite(gli.getSubimage(1 + i * 32, 209, 32, 32), 20000 + i);
			new Sprite(gli.getSubimage(258 + i * 32, 257, 32, 32), 20700 + i);
		}
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 8; j++)
				new Sprite(gli.getSubimage(1 + j * 32, 257 + i * 32, 32, 32), 20100 + i * 100 + j);

		for (int i = 0; i < 4; i++) {
			new Sprite(gli.getSubimage(1 + i * 64, 449, 64, 64), 30000 + i);
			new Sprite(gli.getSubimage(258 + i * 64, 290, 64, 64), 30100 + i);
		}

		new Sprite(gli.getSubimage(258, 17, 64, 64), 0);
		new Sprite(gli.getSubimage(386, 81, 128, 128), 1);
	}

	public final GLImage img;

	public final P piv;
	public final double rad, size;

	private final int id;
	private final boolean horiz, roting;

	private Sprite(GLImage gl) {
		img = gl;
		piv = new P(0.5, 0.5);

		id = (int) (FM.random() * 100) - 10000;
		size = BOSSSIZE;
		rad = size * 2 / gl.getHeight();
		horiz = true;
		roting = false;
	}

	private Sprite(GLImage gl, int lv) {
		TOT[lv / 10000][lv / 100 % 100][lv % 100] = this;
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
		if (id == 1)
			b = true;
		roting = b;
	}

	@Override
	public int compareTo(Sprite o) {
		return Integer.compare(getLayer(), o.getLayer());
	}

	private int getLayer() {
		return -id;
	}

}
