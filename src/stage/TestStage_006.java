package stage;

import battle.Engine;
import battle.Sprite;
import battle.bullet.AbCurve.ListCurve;
import battle.bullet.CurveLaser;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
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
			add(b, 0);
		}

	}

	private static final Sprite.SParam sp0 = new Sprite.SParam(11402, 1, 1);
	private static final Sprite.SParam sp1 = new Sprite.SParam(11406, 1, 1);

	private static final Sprite.SParam[] sps = { sp0, sp1 };

	private static final int dt = 60, fac = dt, dlt = dt;
	private static final int f = 25 * fac, m = 4, x = 3;
	private static final int lt = 2 * f * x + 60 * fac;
	private static final double v0 = 0.3;

	private static final int[] nxs = { 4, 5, 6, 7 };
	private static final int[] ns = { 3, 4, 5, 6 };

	private final int nx, n;

	private VeloListFunc[] vlfs = new VeloListFunc[2];

	public TestStage_006(int diff) {
		super(60000);
		nx = nxs[diff];
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		double a0 = rand(p2);
		for (int i = 0; i < n; i++) {
			double a = a0 + p2 / n * i;
			ListCurve c = new ListCurve(sps[it % 2]);
			Adder ad = new Adder(c, vlfs[it % 2], a, it % 2);
			Engine.RUNNING.add(new Emiter(0, dt, dt * m, ad));
			add(new CurveLaser(c, lt), 0);
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
			// add(new DotBullet(new Dot(pc.copy(),sptest,new
			// FuncMover(vlfs[0],0,0,0)),lt),0);

		}
		for (int i = 0; i < nx; i++)
			if (time == f - 1000 - dlt * (i + 1)) {
				Engine.RUNNING.add(new Emiter(0, f, this, this));
			}
		super.update(dt);
	}

}
