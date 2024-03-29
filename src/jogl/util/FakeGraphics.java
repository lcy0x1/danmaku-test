package jogl.util;

import battle.Sprite;
import jogl.util.GLGraphics.GeomG;
import util.P;

public interface FakeGraphics {

	public static class Coord {

		protected double x, y, w, h, a;

		public Coord(double x2, double y2, double w2, double h2, double a2) {
			x = x2;
			y = y2;
			w = w2;
			h = h2;
			a = a2;
		}

		public Coord copy() {
			return new Coord(x, y, w, h, a);
		}

		public void size(double r) {
			w *= r;
			h *= r;
		}

		public void times(double r) {
			x *= r;
			y *= r;
			w *= r;
			h *= r;
		}

	}

	public static class Curve {

		public P[] ps;
		public double end, dr, r;
		public int mode;

		public Curve(double e, double d, double ra, P[] p, int m) {
			end = e;
			dr = d;
			r = ra;
			ps = p;
			mode = m;
		}

		public Curve copy() {
			P[] np = new P[ps.length];
			for (int i = 0; i < ps.length; i++)
				np[i] = ps[i].copy();
			return new Curve(end, dr, r, np, mode);
		}

		public void size(double m) {
			r *= m;
		}

		public void times(double m) {
			for (P p : ps)
				p.times(m);
			r *= m;
		}

		public double[] transform(float[] r, double tx, double ty) {
			double[] fs = new double[2];
			if (mode == 0) {
				fs[0] = r[0] + tx * r[2];
				fs[1] = r[1] + ty * r[3];
			}
			if (mode == 1) {
				fs[0] = r[0] + (1 - tx) * r[2];
				fs[1] = r[1] + (1 - ty) * r[3];
			}
			if (mode == 2) {
				fs[0] = r[0] + ty * r[2];
				fs[1] = r[1] + tx * r[3];
			}
			if (mode == 3) {
				fs[0] = r[0] + (1 - ty) * r[2];
				fs[1] = r[1] + (1 - tx) * r[3];
			}
			return fs;
		}

	}

	public static class GLT {

		protected float[] data = new float[6];

	}

	public static final int RED = 0, YELLOW = 1, BLACK = 2, MAGENTA = 3, BLUE = 4, CYAN = 5, WHITE = 6;
	public static final int DEF = 0, TRANS = 1, BLEND = 2, GRAY = 3;

	public void colRect(int x, int y, int w, int h, int r, int g, int b, int... a);

	public void drawCircles(Coord[] array);

	public void drawCurve(Sprite s, Curve c);

	public void drawImage(GLImage bimg, double x, double y);

	public void drawImage(GLImage bimg, double x, double y, double d, double e);

	public void drawImages(Sprite s, int size, Coord[] array);

	public void drawLine(int i, int j, int x, int y);

	public void drawOval(int i, int j, int k, int l);

	public void drawRect(int x, int y, int x2, int y2);

	public void fillOval(int i, int j, int k, int l);

	public void fillRect(int x, int y, int w, int h);

	public GLT getTransform();

	public void gradRect(int x, int y, int w, int h, int a, int b, int[] c, int d, int e, int[] f);

	public void rotate(double d);

	public void scale(int hf, int vf);

	public void setColor(int c);

	public void setComposite(int mode, int... para);

	public void setRenderingHint(int key, int object);

	public void setTransform(GLT at);

	public void translate(double x, double y);

}

interface GeoAuto extends FakeGraphics {

	@Override
	public default void colRect(int x, int y, int w, int h, int r, int gr, int b, int... a) {
		getGeo().colRect(x, y, w, h, r, gr, b, a);
	}

	@Override
	public default void drawCircles(Coord[] array) {
		getGeo().drawCircles(array);
	}

	@Override
	public default void drawLine(int i, int j, int x, int y) {
		getGeo().drawLine(i, j, x, y);
	}

	@Override
	public default void drawOval(int i, int j, int k, int l) {
		getGeo().drawOval(i, j, k, l);
	}

	@Override
	public default void drawRect(int x, int y, int x2, int y2) {
		getGeo().drawRect(x, y, x2, y2);
	}

	@Override
	public default void fillOval(int i, int j, int k, int l) {
		getGeo().fillOval(i, j, k, l);
	}

	@Override
	public default void fillRect(int x, int y, int w, int h) {
		getGeo().fillRect(x, y, w, h);
	}

	public GeomG getGeo();

	@Override
	public default void gradRect(int x, int y, int w, int h, int a, int b, int[] c, int d, int e, int[] f) {
		getGeo().gradRect(x, y, w, h, a, b, c, d, e, f);
	}

	@Override
	public default void setColor(int c) {
		getGeo().setColor(c);
	}

}
