package stage;

import battle.Engine;
import battle.Sprite;
import battle.bullet.AbCurve;
import battle.bullet.CurveLaser;
import battle.bullet.Func;
import battle.entity.Emiter;
import util.P;

public class TestStage_005 extends SpellCard implements Emiter.Ticker {

	private static class PathFunc implements Func {

		private final double r0, a0, w0;

		private PathFunc(double r, double a, double w) {
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
			double tot = time - i * xt < 0 ? 0
					: ((m - 1 - i) * dr * (Math.min(xt * m, time) - i * xt) / (xt * m - i * xt));
			return P.polar(tot, a0 + Math.sin(r0 + (-time + i * vt) * w0) * ma).plus(pc);
		}

	}

	private static final Sprite.SParam sp0 = new Sprite.SParam(11402, 3, 1);
	private static final Sprite.SParam sp1 = new Sprite.SParam(11406, 3, 1);
	private static final int f = 40, n = 8, m = 40, xt = 200, vt = 80;
	private static final double w = p2 / 6000, dr = 10, ma = p2 / 12;

	public TestStage_005(int diff) {
		super(60000);
	}

	@Override
	public void tick(Emiter e, int it, int ex) {

	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			for (int i = 0; i < n; i++) {
				AbCurve c0 = new AbCurve.FuncCurve(new PathFunc(0, p2 / n * i, w), m, sp0);
				Engine.RUNNING.add(new CurveLaser(c0).setLv(K_FUNCTIONAL));
				AbCurve c1 = new AbCurve.FuncCurve(new PathFunc(p2 / 2, p2 / n * i, w), m, sp1);
				Engine.RUNNING.add(new CurveLaser(c1).setLv(K_FUNCTIONAL));
			}
			Engine.RUNNING.add(new Emiter(0, f, this, this));
		}
		super.update(dt);
	}

}
