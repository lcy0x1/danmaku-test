package stage;

import battle.Engine;
import battle.Sprite;
import battle.Control;
import battle.entity.Dot;
import battle.entity.DotBullet;
import battle.entity.Emiter;
import util.P;

public class TestStage_001 implements Control.UpdCtrl, Emiter.Ticker {

	private static class Orbit extends Dot.TimeMover {

		private final int i, n;

		private Orbit(int x, int m) {
			i = x;
			n = m;
		}

		@Override
		public P disp(int t) {
			double a0 = t * w0 + Math.PI * 2 / n * i * 2;
			double a1 = t * w1 + Math.PI * 2 / n * i;
			P p0 = P.polar(l, a1).plus(o.x / 2, o.y / 2);
			return P.polar(l, a0).plus(p0);
		}

		@Override
		public boolean out(P pos, double r) {
			return false;
		}

	}

	private static final int f = 20, l = 200, m = 80;
	private static final double v0 = 0.1, w0 = Math.PI / 2345, w1 = Math.PI / 9876;
	private static final P o = Engine.BOUND;

	private static final int[] ns = { 5, 6, 7, 8 };

	private final int n;
	private int time = 0;

	public TestStage_001(int diff) {
		n = ns[diff];
	}

	@Override
	public boolean finished() {
		return time > 50000;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		Sprite.DESParam d0 = new Sprite.DESParam(10702, 0, 1);
		for (int i = 0; i < n; i++) {
			if ((it + 2 * m * i / n) / m % 2 == 0)
				continue;
			double a0 = it * f * w0 + Math.PI * 2 / n * i * 2;
			double a1 = it * f * w1 + Math.PI * 2 / n * i;
			P p0 = P.polar(l, a1).plus(o.x / 2, o.y / 2);
			P p1 = P.polar(l, a0).plus(p0);
			P pv = P.polar(-v0, a0);
			DotBullet b = new DotBullet(new Dot(p1, pv, d0));
			b.update(ex);
			Engine.RUNNING.add(b);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			Sprite.DESParam d = new Sprite.DESParam(30000, 1, 1);
			for (int i = 0; i < n; i++)
				Engine.RUNNING.add(new DotBullet(new Dot(d, new Orbit(i, n))));
			Engine.RUNNING.add(new Emiter(0, f, this, this));
		}
		time += dt;

	}

}
