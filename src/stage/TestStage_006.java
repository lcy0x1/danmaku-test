package stage;

import battle.Sprite;
import battle.bullet.Curve.ListCurve;
import battle.bullet.Curve;
import battle.bullet.Laser;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Func;
import battle.bullet.Func.VeloListFunc;
import battle.bullet.Mover.FuncMover;
import battle.entity.Emiter;
import util.P;

public class TestStage_006 extends SpellCard implements Emiter.Ticker {

	private static class Adder implements Emiter.Ticker {

		private final ListCurve c;
		private final VeloListFunc vlf;
		private final double a;
		private final int i;

		private Adder(ListCurve lc, VeloListFunc func, double a0, int s) {
			c = lc;
			vlf = func;
			a = a0;
			i = s;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {
			Dot d = new Dot(pc.copy(), sps[i], new FuncMover(vlf, 0, 0, a));
			DotBullet b = new DotBullet(d, lt);
			c.addP(b);
			add(b, ex);
		}

	}

	private static class PathFunc implements Func {

		private final P ori;
		private final double r0, a0, w0, l0;

		private PathFunc(P p, double a, double t0, double w, double l) {
			ori = p;
			r0 = t0;
			a0 = a;
			w0 = w;
			l0 = l / m;
		}

		@Override
		public boolean exist(int time, int i) {
			return (i - 1) * 200 < time;
		}

		@Override
		public P func(int time, int i) {
			double tot = time - i * xt < 0 ? 0
					: ((m - 1 - i) * l0 * (Math.min(xt * m, time) - i * xt) / (xt * m - i * xt));
			return P.polar(tot, a0 + Math.sin(r0 + (-time + i * vt) * w0) * ma).plus(ori);
		}

	}

	private static final Sprite.SParam spr = new Sprite.SParam(11410, 3, 1);
	private static final Sprite.SParam sp0 = new Sprite.SParam(11402, 1, 2);
	private static final Sprite.SParam sp1 = new Sprite.SParam(11406, 1, 2);

	private static final Sprite.SParam[] sps = { sp0, sp1 };

	private static final int dt = 60, fac = dt, dlt = dt;
	private static final int f = 25 * fac, mx = 4, x = 3;
	private static final int lt = 2 * f * x + 60 * fac;
	private static final double v0 = 0.3;

	private static final int m = 10, xt = 200, vt = 250, dl = 50, rr = 200;
	private static final double w = p2 / 6000, ma = p2 / 12;

	private static final int[] nrs = { 4, 5, 6, 7 };
	private static final int[] ns = { 3, 4, 5, 6 };

	private final int nr, n;
	private final int nx = 16, ny = 20;

	private VeloListFunc[] vlfs = new VeloListFunc[2];

	public TestStage_006(int diff) {
		super(60000);
		nr = nrs[diff];
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0)
			add(new Emiter(1, f, this, this), ex);
		if (e.id == 1) {
			double a0 = rand(p2);
			for (int i = 0; i < n; i++) {
				double a = a0 + p2 / n * i;
				ListCurve c = new ListCurve(sps[it % 2]);
				Adder ad = new Adder(c, vlfs[it % 2], a, it % 2);
				add(new Emiter(0, dt, dt * mx, ad), ex);
				add(new Laser(c, lt), ex);
			}
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			for (int i = 0; i < 2; i++) {
				double a = 0;
				int s = i * 2 - 1;
				VeloListFunc vlf = vlfs[i] = new VeloListFunc();
				vlf.add(P.polar(v0, a), 20 * fac);
				for (int j = 0; j < x; j++) {
					vlf.add(P.polar(v0, a), 10 * fac);
					vlf.add(P.polar(v0, a + s * p2 / 8), 3 * fac);
					vlf.add(P.polar(v0, a + s * p2 / 4), 3 * fac);
					vlf.add(P.polar(v0, a + s * 3 * p2 / 8), 3 * fac);
					vlf.add(P.polar(v0, a + s * p2 / 2), 3 * fac);
					vlf.add(P.polar(v0, a - s * p2 * 3 / 8), 3 * fac);
					vlf.add(P.polar(v0, a - s * p2 * 3 / 8), 25 * fac);
					a = a - s * p2 * 3 / 8;
				}
			}
			for (int i = 0; i < nx; i++)
				adds(new P(o.x / nx * i, 0));
			for (int i = 1; i <= nx; i++)
				adds(new P(o.x / nx * i, o.y));
			for (int i = 1; i <= ny; i++)
				adds(new P(0, o.y / ny * i));
			for (int i = 0; i < ny; i++)
				adds(new P(o.x, o.y / ny * i));

			add(new Emiter(0, dlt, dlt * nr, this));
		}
		super.update(dt);
	}

	private void adds(P p) {
		double a = p.atan2(pc);
		if (p.dis(pc) - rr < dl)
			return;
		P p0 = P.polar(dl, p2 / 2 + a).plus(p);
		PathFunc pf = new PathFunc(p0, a, rand(p2), w, (p0.dis(pc) - rr) / 2);
		Curve c0 = new Curve.FuncCurve(pf, m, spr);
		add(new Laser(c0));
	}

}
