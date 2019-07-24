package stage;

import battle.Sprite;
import battle.bullet.Curve.ListCurve;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Laser;
import battle.bullet.Mover;
import battle.entity.Emiter;
import util.P;

public class S010 extends SpellCard implements Emiter.Ticker {

	private static class Adder implements Emiter.Ticker {

		private final P pv;
		private final Dot[] pri;
		private final ListCurve lc0, lc1, lc2;

		private Adder(P v, ListCurve c0, ListCurve c1, ListCurve c2, Dot[] prim) {
			pv = v;
			pri = prim;
			lc0 = c0;
			lc1 = c1;
			lc2 = c2;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {
			P pv0 = pv.copy();
			P p = pc.copy();
			Dot d0 = new Dot(p.copy(), sp0, new PathPrim(p, pv0));
			if (it == 0)
				pri[e.id] = d0;
			double w = w0 * pv.abs() / 0.4;
			Dot d1 = new Dot(p.copy(), sp1, new PathFunc(p, pv0, 0, l0, w));
			Dot d2 = new Dot(p.copy(), sp2, new PathFunc(p, pv0, p2 / 2, l0, w));
			DotBullet b0 = new DotBullet(d0);
			DotBullet b1 = new DotBullet(d1);
			DotBullet b2 = new DotBullet(d2);
			lc0.addP(b0);
			lc1.addP(b1);
			lc2.addP(b2);
			add(b0.setLv(K_FUNCTIONAL), ex);
			add(b1.setLv(K_FUNCTIONAL), ex);
			add(b2.setLv(K_FUNCTIONAL), ex);
		}

	}

	private static class PathFunc implements Mover {

		private final P ori, pv;
		private final double it, amp, fre;

		private int time;

		private PathFunc(P o, P v, double ia, double a, double f) {
			ori = o;
			pv = v;
			amp = a;
			it = ia;
			fre = f;
		}

		@Override
		public boolean out(P pos, double r) {
			return false;
		}

		@Override
		public void update(Dot d, int t) {
			P p = P.polar(amp * Math.sin(it + fre * time), pv.atan2() + p2 / 4).plus(ori);
			d.tmp.setTo(p.flip(new P(0, 0), o));
			time += t;
		}

	}

	private static class PathPrim implements Mover {

		private final P ori, pv;

		private PathPrim(P o, P v) {
			ori = o;
			pv = v;
		}

		@Override
		public boolean out(P pos, double r) {
			return false;
		}

		@Override
		public void update(Dot d, int t) {
			ori.plus(pv, t);
			d.tmp.setTo(ori.copy().flip(new P(0, 0), o));
		}

	}

	private static final Sprite.SParam sp0 = Sprite.getSprite(Sprite.P_SR, 11410, 0, 1);
	private static final Sprite.SParam sp1 = Sprite.getCurve(11402, 0, 1, p2 / 30, 0, false);
	private static final Sprite.SParam sp2 = Sprite.getCurve(11413, 0, 1, p2 / 30, 0, false);
	private static final Sprite.SParam sp3 = Sprite.getDot(10206, 0);

	private static final double l0 = 50, w0 = p2 / 1000, da = p2 / 4;

	private static final int[] ns = { 4, 5, 6, 7 };
	private static final int[] nxs = { 10, 12, 14, 16 };
	private static final double[] vs = { 0.1, 0.14, 0.17, 0.2 };

	private final int n, nx;
	private final double v0;
	private final Dot[] pvs;
	private final double[] pre, ppp;

	public S010(int diff) {
		super(60000);
		n = ns[diff];
		nx = nxs[diff];
		v0 = vs[diff];
		pvs = new Dot[n];
		pre = new double[n];
		ppp = new double[n];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			for (int i = 0; i < n; i++) {
				double dir = pvs[i].getDire();
				double dif0 = Math.abs(dir - pre[i]);
				double dif1 = Math.abs(pre[i] - ppp[i]);
				if (dif0 > 1e-5 && dif1 < 1e-5 && it > 5)
					adds(ex);
				ppp[i] = pre[i];
				pre[i] = dir;
			}
		}
	}

	@Override
	public void update(int dtx) {
		if (time == 0) {
			int m = 48, dt = 20;
			double v1 = v0 * 2;
			double a = p2 / 4 + (rand(p2 / 16) + p2 / 16) * (rand(2) > 1 ? 1 : -1);
			for (int i = 0; i < n; i++) {
				double a0 = a + p2 / n * i;
				P pv = P.polar(v1, a0);
				pre[i] = a0;
				ListCurve lc0 = new ListCurve(sp0);
				ListCurve lc1 = new ListCurve(sp1);
				ListCurve lc2 = new ListCurve(sp2);
				add(new Emiter(i, dt, dt * m, new Adder(pv, lc0, lc1, lc2, pvs)));
				add(new Laser(lc0).setLv(K_FINISH));
				add(new Laser(lc1).setLv(K_FINISH));
				add(new Laser(lc2).setLv(K_FINISH));
			}
			add(new Emiter(0, dt, this, this));
		}
		super.update(dtx);
	}

	private void adds(int ex) {
		for (int i = 0; i < 4; i++) {
			P p0 = P.polar(1, p2 / 4 * i);
			P pa = P.polar(1, p2 / 4 * (i - 1)).plus(p0);
			P pb = P.polar(1, p2 / 4 * (i + 1)).plus(p0);
			pa.times(pc).plus(pc);
			pb.times(pc).plus(pc);
			double d = rand(1) / nx;
			for (int j = 0; j < nx; j++) {
				P pos = pa.middle(pb, 1.0 * j / nx + d);
				double a = (rand(0.5) + 0.5) * (rand(2) > 1 ? 1 : -1);
				P pv = P.polar(v0, p2 / 2 + p2 / 4 * i + a * da);
				add(new DotBullet(new Dot(pos, pv, sp3)), ex);
			}
		}
	}

}
