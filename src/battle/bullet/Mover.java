package battle.bullet;

import battle.Engine;
import util.P;

public interface Mover {

	public static class CurveMover extends TimeMover {

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
		public Mover copy() {
			return new CurveMover(ir, ia, v, w, ar, it);
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
			P ori = pos.copy().plus(disp(time), -1);
			double dis = ori.toBound(o);
			double rm = ir + v * time + ar * it * it / 2;
			if (time > it && Math.abs(rm) > dis && rm * v > 0)
				return true;
			return false;
		}

	}

	public static class FuncMover extends TimeMover {

		private final Func f;
		private final double a0;
		private final int dt, ind;

		public FuncMover(Func func, int init, int i, double a) {
			f = func;
			dt = init;
			ind = i;
			a0 = a;
		}

		@Override
		public Mover copy() {
			return new FuncMover(f, dt, ind, a0);
		}

		@Override
		public P disp(int t) {
			P rel = f.func(t + dt, ind);
			return rel.rotate(a0);
		}

		@Override
		public boolean out(P pos, double r) {
			return !f.exist(time + dt, ind);
		}

	}

	public static class HomingLM extends LineMover {

		private final double s;

		private boolean invoked = false;

		public HomingLM(double a) {
			super(new P(0, 0));
			s = a;
		}

		public void check(P p) {
			if (!invoked)
				invoke(p);
		}

		@Override
		public Mover copy() {
			return new HomingLM(s);
		}

		public void invoke(P p) {
			invoked = true;
			v.setTo(P.polar(s, p.atan2(Engine.RUNNING.pl.pos)));
		}

		@Override
		public void update(Dot d, int dt) {
			check(d.pos);
			super.update(d, dt);
		}

	}

	public static class LineMover extends TimeMover {

		public final P a, v;

		public final int t0, t1;

		public LineMover(P vp) {
			this(vp, null, 0, 0);
		}

		public LineMover(P vp, double ap, int st, int et) {
			this(vp, P.polar(ap, vp.atan2()), st, et);
		}

		public LineMover(P vp, P ap, int tt) {
			this(vp, ap, 0, tt);
		}

		public LineMover(P vp, P ap, int st, int et) {
			v = vp;
			a = ap;
			t0 = st;
			t1 = et;
		}

		@Override
		public Mover copy() {
			return new LineMover(v.copy(), a == null ? null : a.copy(), t0, t1);
		}

		@Override
		public P disp(int ct) {
			if (ct <= t0 || a == null)
				return v.copy().times(ct);
			if (ct > t0 && ct <= t1 && a != null)
				return v.copy().times(ct).plus(a, (ct - t0) * (ct - t0) / 2);
			return v.copy().times(ct).plus(a, (t1 - t0) * (ct - t1 + (t1 - t0) / 2));
		}

		@Override
		public int getType() {
			if (time > t1)
				return TYPE_TIME | TYPE_LINE;
			return TYPE_TIME;
		}

		@Override
		public boolean out(P pos, double r) {
			return pos.moveOut(v, Engine.BOUND, r);
		}

	}

	public static class RefMover implements Mover {

		private static final int LEFT = 1, RIGHT = 2, UP = 4, DOWN = 8;

		private final P ul, dr, v;
		private final int mode, max;
		private final double r;

		private int rem;

		public RefMover(P vp, int tot) {
			this(vp, new P(0, 0), Engine.BOUND, 0, tot, 15);
		}

		public RefMover(P vp, P b0, P b1, double ra, int tot, int mod) {
			v = vp;
			ul = b0;
			dr = b1;
			r = ra;
			rem = max = tot;
			mode = mod;
		}

		@Override
		public Mover copy() {
			return new RefMover(v.copy(), ul, dr, r, max, mode);
		}

		@Override
		public int getType() {
			return TYPE_LINE;
		}

		@Override
		public boolean out(P pos, double r) {
			return rem == 0 && pos.moveOut(v, Engine.BOUND, r);
		}

		@Override
		public void update(Dot d, int t) {
			d.tmp.plus(v, t);
			if (rem == 0)
				return;

			if (rem != 0 && d.tmp.x + r > dr.x && (mode & RIGHT) > 0) {
				d.tmp.x = 2 * dr.x - 2 * r - d.tmp.x;
				v.x *= -1;
				rem--;
			}
			if (rem != 0 && d.tmp.x - r < ul.x && (mode & LEFT) > 0) {
				d.tmp.x = 2 * ul.x + 2 * r - d.tmp.x;
				v.x *= -1;
				rem--;
			}
			if (rem != 0 && d.tmp.y + r > dr.y && (mode & DOWN) > 0) {
				d.tmp.y = 2 * dr.y - 2 * r - d.tmp.y;
				v.y *= -1;
				rem--;
			}
			if (rem != 0 && d.tmp.y - r < ul.y && (mode & UP) > 0) {
				d.tmp.y = 2 * ul.y + 2 * r - d.tmp.y;
				v.y *= -1;
				rem--;
			}

		}

	}

	public static abstract class TimeMover implements Mover {

		protected int time = 0;

		public abstract P disp(int t);

		@Override
		public int getType() {
			return TYPE_TIME;
		}

		@Override
		public void update(Dot d, int dt) {
			P p0 = disp(time);
			P p1 = disp(time + dt);
			d.tmp.plus(p0, -1).plus(p1);
			time += dt;
		}

	}

	public static final int TYPE_TIME = 1, TYPE_LINE = 2;

	public default Mover copy() {
		return null;
	}

	public int getType();

	public boolean out(P pos, double r);

	public void update(Dot d, int t);

}
