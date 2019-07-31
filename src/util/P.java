package util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public strictfp class P {

	public static class Line {

		public P[] ps;

		public Line(P... parr) {
			ps = parr;
		}

		public double dis(P p) {
			double dis = ps[ps.length - 1].dis(p);
			for (int i = 1; i < ps.length; i++) {
				P p0 = ps[i - 1];
				P p1 = ps[i];
				P v0 = p0.sf(p);
				P v1 = p1.sf(p);
				P l = p0.sf(p1);
				// dis to point
				dis = FM.min(dis, v0.abs());
				// dis to line
				if (l.dotP(v0) >= 0 && l.dotP(v1) <= 0)
					dis = FM.min(dis, FM.abs(l.crossP(v0)) / l.abs());
			}
			return dis;
		}

	}

	public static class Polygon {

		public P[] ps;
		public double rad;

		public Polygon(double r, P... parr) {
			ps = parr;
			rad = r;
		}

		public double dis(P p) {
			double dire = randpi();
			int count = 0;
			double dis = Double.MAX_VALUE;
			P up = polar(1, dire);
			for (int i = 0; i < ps.length; i++) {
				P p0 = get(i - 1);
				P p1 = get(i);
				P v0 = p0.sf(p);
				P v1 = p1.sf(p);
				P l = p0.sf(p1);
				// dis to point
				dis = FM.min(dis, v0.abs());
				// dis to line
				if (l.dotP(v0) >= 0 && l.dotP(v1) <= 0)
					dis = FM.min(dis, FM.abs(l.crossP(v0)) / l.abs());
				// intersect
				if (v0.crossP(up) * v0.crossP(v1) < 0 && -v0.dotP(up) * v1.abs() > v0.dotP(v1))
					count++;
			}
			if (count % 2 > 0)
				return -dis;
			return dis;
		}

		private P get(int i) {
			if (i < 0)
				return ps[i + ps.length];
			if (i >= ps.length)
				return ps[i - ps.length];
			return ps[i];
		}

	}

	public static double goOut(P p, P v, double r) {
		double a = v.x * v.x + v.y * v.y;
		double b = 2 * p.dotP(v);
		double c = p.x * p.x + p.y * p.y - r * r;
		double d2 = b * b - 4 * a * c;
		if (d2 < 0)
			return Double.NaN;
		double d = FM.sqrt(d2);
		double t0 = (-b + d) / (2 * a);
		double t1 = (-b - d) / (2 * a);
		return t0 < t1 ? t1 : t0;
	}

	public static double middleC(double d) {
		return (1 - FM.cos(FM.PI * d)) / 2;
	}

	public static P polar(double r, double t) {
		return new P(r * FM.cos(t), r * FM.sin(t));
	}

	public static float reg(float cx) {
		if (cx < 0)
			return 0;
		if (cx > 1)
			return 1;
		return cx;
	}

	private static double randpi() {
		return FM.PI * FM.random();
	}

	public double x, y;

	public P(Dimension d) {
		x = d.getWidth();
		y = d.getHeight();
	}

	public P(double X, double Y) {
		x = X;
		y = Y;
	}

	public P(Point p) {
		x = p.getX();
		y = p.getY();
	}

	public P(Point2D p) {
		x = p.getX();
		y = p.getY();
	}

	public double abs() {
		return dis(0, 0);
	}

	public double atan2() {
		return FM.atan2(y, x);
	}

	public double atan2(P p) {
		return FM.atan2(p.y - y, p.x - x);
	}

	public P copy() {
		return new P(x, y);
	}

	public double crossP(P p) {
		return x * p.y - y * p.x;
	}

	public double dis(double px, double py) {
		return FM.sqrt(FM.sq(px - x) + FM.sq(py - y));
	}

	public double dis(P p) {
		return FM.sqrt(FM.sq(p.x - x) + FM.sq(p.y - y));
	}

	public P divide(P p) {
		x /= p.x;
		y /= p.y;
		return this;
	}

	public double dotP(P p) {
		return x * p.x + y * p.y;
	}

	@Override
	public boolean equals(Object obj) {
		if (P.class.isAssignableFrom(obj.getClass())) {
			P i = (P) obj;
			if (FM.abs(i.x - x) + FM.abs(i.y - y) < 1e-10)
				return true;
		}
		return false;
	}

	public P flip(P p, P o) {
		plus(p, -1);
		x %= o.x * 2;
		y %= o.y * 2;
		if (x < 0)
			x = -x;
		if (y < 0)
			y = -y;
		if (x > o.x)
			x = o.x * 2 - x;
		if (y > o.y)
			y = o.y * 2 - y;
		plus(p);
		return this;
	}

	public boolean limit(P b2) {
		return limit(new P(0, 0), b2);
	}

	public boolean limit(P b1, P b2) {
		boolean ans = out(b1, b2, 0);
		if (x < b1.x)
			x = b1.x;
		if (x > b2.x)
			x = b2.x;
		if (y < b1.y)
			y = b1.y;
		if (y > b2.y)
			y = b2.y;
		return ans;
	}

	public P middle(P p, double per) {
		return copy().plus(sf(p), per);
	}

	public P middleC(P p, double per) {
		return copy().plus(sf(p), (1 - FM.cos(FM.PI * per)) / 2);
	}

	public boolean moveOut(P v, P b2, double r) {
		return moveOut(v, new P(0, 0), b2, r);
	}

	public boolean moveOut(P v, P b1, P b2, double r) {
		return x + r < b1.x && v.x <= 0 || y + r < b1.y && v.y <= 0 || x - r > b2.x && v.x >= 0
				|| y - r > b2.y && v.y >= 0;
	}

	public P onLimit(P v, P b1, P b2) {
		if (x < b1.x)
			return new P(b1.x, y - (x - b1.x) / v.x * v.y);
		if (x > b2.x)
			return new P(b2.x, y - (x - b2.x) / v.x * v.y);
		if (y < b1.y)
			return new P(x - (y - b1.y) / v.y * v.x, b1.y);
		if (y > b2.y)
			return new P(x - (y - b2.y) / v.y * v.x, b2.y);
		return this;
	}

	public boolean out(int[] rect, double sca, double r) {
		P p0 = new P(rect[0], rect[1]);
		P p1 = new P(rect[2], rect[3]).plus(p0);
		p0.times(sca);
		p1.times(sca);
		return out(p0, p1, r);
	}

	public boolean out(P b2, double r) {
		return out(new P(0, 0), b2, r);
	}

	public boolean out(P b1, P b2, double r) {
		return x + r < b1.x || y + r < b1.y || x - r > b2.x || y - r > b2.y;
	}

	public P plus(double px, double py) {
		x += px;
		y += py;
		return this;
	}

	public P plus(P p) {
		x += p.x;
		y += p.y;
		return this;
	}

	public P plus(P p, double n) {
		x += p.x * n;
		y += p.y * n;
		return this;
	}

	public P positivize() {
		if (x < 0)
			x = -x;
		if (y < 0)
			y = -y;
		return this;
	}

	public P rotate(double t) {
		return setTo(x * FM.cos(t) - y * FM.sin(t), y * FM.cos(t) + x * FM.sin(t));
	}

	public P setTo(double tx, double ty) {
		x = tx;
		y = ty;
		return this;
	}

	/* return this */
	public P setTo(P p) {
		x = p.x;
		y = p.y;
		return this;
	}

	public P sf(P p) {
		return substractFrom(p);
	}

	public P substractFrom(P p) {
		return new P(p.x - x, p.y - y);
	}

	public P times(double d) {
		x *= d;
		y *= d;
		return this;
	}

	public P times(double hf, double vf) {
		x *= hf;
		y *= vf;
		return this;
	}

	public P times(P p) {
		x *= p.x;
		y *= p.y;
		return this;
	}

	public double toBound(double px, double py) {
		return FM.max(FM.max(dis(0, 0), dis(px, 0)), FM.max(dis(0, py), dis(px, py)));
	}

	public double toBound(P o) {
		return toBound(o.x, o.y);
	}

	public Dimension toDimension() {
		return new Dimension((int) x, (int) y);
	}

	public Point toPoint() {
		return new Point((int) x, (int) y);
	}

	public Point2D toPoint2D() {
		return new Point2D.Double(x, y);
	}

	public Rectangle toRectangle(int w, int h) {
		return new Rectangle((int) x, (int) y, w, h);
	}

	public Rectangle toRectangle(P p) {
		return new Rectangle((int) x, (int) y, (int) p.x, (int) p.y);
	}

	public Rectangle2D toRectangle2D(P p) {
		return new Rectangle2D.Double(x, y, p.x, p.y);
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

}
