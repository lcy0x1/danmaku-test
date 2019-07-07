package stage;

import battle.Engine;
import battle.Sprite;
import battle.bullet.AbCurve;
import battle.bullet.CurveLaser;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import util.P;

public class TestStage_004 extends SpellCard implements Emiter.Ticker {

	private static class Func implements AbCurve.Func {

		private final double r0, a0, w0;

		private Func(double r, double a, double w) {
			r0 = r;
			a0 = a;
			w0 = w;
		}

		@Override
		public boolean exist(int time, int i) {
			return (i - 1) * 200 < time;
		}

		@Override
		public P func(int time, int i) {
			double tot = time - i * 200 < 0 ? 0
					: ((19 - i) * 10. * (Math.min(4000, time) - i * 200) / (4000 - i * 200));
			return P.polar(tot, a0 + Math.sin(r0 + (time - i * 200) * w0) * p2 / 6).plus(pc);
		}

	}

	private static final Sprite.SParam sp0 = new Sprite.SParam(11402, 1, 1);
	private static final Sprite.SParam sp1 = new Sprite.SParam(11406, 1, 1);

	private final int f = 40, n = 8, l = 200;
	private final double v0 = 0.2, maxda = p2 / 12, w0 = p2 / 30000, w1 = p2 / 10000;
	private final double w = p2 / 10000;
	private final AbCurve.ListCurve[] list;

	public TestStage_004(int diff) {
		super(60000);
		list = new AbCurve.ListCurve[n];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		double a = it * f * w0;
		for (int i = 0; i < n; i++) {
			double a0 = p2 / n * i + a;
			P p = P.polar(l, a0).plus(pc);
			double a1 = it * f * w1;
			double da = maxda * Math.sin(a0 + a1);
			P pv = P.polar(-v0, a0 + da);
			DotBullet d = new DotBullet(new Dot(p, pv, sp1));
			list[i].addP(d);
			Engine.RUNNING.add(d);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			for (int i = 0; i < n; i++) {
				AbCurve c0 = new AbCurve.FuncCurve(new Func(0, p2 / n * i, w), 20, sp0);
				Engine.RUNNING.add(new CurveLaser(c0).setLv(K_FUNCTIONAL));
				list[i] = new AbCurve.ListCurve(sp1);
				Engine.RUNNING.add(new CurveLaser(list[i]).setLv(K_FINISH));
			}
			Engine.RUNNING.add(new Emiter(0, f, this, this));
		}
		super.update(dt);
	}

}
