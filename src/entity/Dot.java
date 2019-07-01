package entity;

import entity.LifeControl.MoveControl;
import util.P;

public class Dot {

	public static class CurveMover extends TimeMover implements MoveControl {

		private final double ir, ia, v, w, ar;

		private final int it;

		public CurveMover(double r, double a, double dv, double dw) {
			this(r, a, dv, dw, 0, 0);
		}

		public CurveMover(double r, double a, double dv, double dw, double aa, int tt) {
			ir = r;
			ia = a;
			v = dv;
			w = dw;
			ar = aa;
			it = tt;
		}

		@Override
		public P disp(int ct) {
			int at = ct;
			if (at > it)
				at = it;
			double r = ir + v * ct + ar * at * at / 2;
			return P.polar(r, ia + w * ct);
		}

		@Override
		public boolean out(P pos, double r) {
			P o = Engine.BOUND;
			if (!pos.out(o, r))
				return false;
			P ori = pos.copy().plus(disp(t), -1);
			double dis = Math.max(Math.max(ori.dis(0, 0), ori.dis(o.x, 0)), Math.max(ori.dis(0, o.y), ori.dis(o)));
			double rm = ir + v * t + ar * it * it / 2;
			if (t > it && Math.abs(rm) > dis && rm * v > 0)
				return true;
			return false;
		}

	}

	public static class LineMover extends TimeMover implements MoveControl {

		public final P a, v;

		public int it;

		public LineMover(double ap, int tt, P vp) {
			v = vp;
			it = tt;
			a = P.polar(ap, vp.atan2());
		}

		public LineMover(P vp) {
			v = vp;
			a = null;
			it = 0;
		}

		public LineMover(P ap, int tt, P vp) {
			v = vp;
			it = tt;
			a = ap;
		}

		@Override
		public P disp(int ct) {
			P ad = v.copy().times(ct);
			if (ct > it)
				ct = it;
			if (a != null)
				ad.plus(a, ct * ct / 2);
			return ad;
		}

		@Override
		public int getType() {
			if (t > it)
				return TYPE_TIME | TYPE_LINE;
			return TYPE_TIME;
		}

		@Override
		public boolean out(P pos, double r) {
			return pos.moveOut(v, Engine.BOUND, r);
		}

	}

	public static interface Mover {

		public static final int TYPE_TIME = 1, TYPE_LINE = 2;

		public int getType();

		public void update(Dot d, int t);

	}

	public static class RefMover implements Mover, MoveControl {

		private static final int LEFT = 1, RIGHT = 2, UP = 4, DOWN = 8;

		private final P ul, dr, v;

		private final int mode;

		private double r;

		private int rem;

		public RefMover(P vp, P b0, P b1, double ra, int tot, int mod) {
			v = vp;
			ul = b0;
			dr = b1;
			r = ra;
			rem = tot;
			mode = mod;
		}

		@Override
		public int getType() {
			return TYPE_LINE;
		}

		@Override
		public boolean out(P pos, double r) {
			return pos.moveOut(v, Engine.BOUND, r);
		}

		@Override
		public void update(Dot d, int t) {
			d.tmp.plus(v, t);
			if (rem == 0)
				return;

			if (d.tmp.x + r > dr.x && (mode & RIGHT) > 0) {
				d.tmp.x = 2 * dr.x - 2 * r - d.tmp.x;
				v.x *= -1;
				rem--;
			}
			if (d.tmp.x - r < ul.x && (mode & LEFT) > 0) {
				d.tmp.x = 2 * ul.x + 2 * r - d.tmp.x;
				v.x *= -1;
				rem--;
			}
			if (d.tmp.y + r > dr.y && (mode & DOWN) > 0) {
				d.tmp.y = 2 * dr.y - 2 * r - d.tmp.y;
				v.y *= -1;
				rem--;
			}
			if (d.tmp.y - r < ul.y && (mode & UP) > 0) {
				d.tmp.y = 2 * ul.y + 2 * r - d.tmp.y;
				v.y *= -1;
				rem--;
			}

		}

	}

	public static abstract class TimeMover implements Mover {

		protected int t = 0;

		public abstract P disp(int t);

		@Override
		public int getType() {
			return TYPE_TIME;
		}

		@Override
		public void update(Dot d, int dt) {
			P p0 = disp(t);
			P p1 = disp(t + dt);
			d.tmp.plus(p0, -1).plus(p1);
			t += dt;
		}

	}

	public final P pos, tmp;
	public final Sprite sprite;
	public final Shape.PosShape shape;

	public double dire;
	public Mover move = null;

	public Dot(P p, double r, Sprite img) {
		pos = p;
		tmp = p.copy();
		shape = new Shape.Circle(pos, r);
		sprite = img;
	}

	public void post() {
		if (pos.dis(tmp) > 0)
			dire = pos.atan2(tmp);
		pos.setTo(tmp);
	}

	public void update(int t) {
		if (move != null)
			move.update(this, t);
	}

}
