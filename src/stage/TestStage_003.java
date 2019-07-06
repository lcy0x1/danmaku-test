package stage;

import battle.Engine;
import battle.Sprite;
import battle.Control;
import battle.entity.Dot;
import battle.entity.DotBullet;
import battle.entity.Emiter;
import battle.special.Koishi;
import util.P;

public class TestStage_003 implements Control.UpdCtrl, Emiter.Ticker {

	private static final P o = Engine.BOUND;
	private static final int f = 80, f1 = 120, m = 3;
	private static final double v0 = 0.1;

	private static final Sprite.DESParam sp0 = new Sprite.DESParam(10302, 0, 1);
	private static final Sprite.DESParam sp1 = new Sprite.DESParam(10306, 0, 1);
	private static final Sprite.DESParam sp2 = new Sprite.DESParam(10311, 0, 1);
	private static final Sprite.DESParam sx0 = new Sprite.DESParam(30100, 0, 1);
	private static final Sprite.DESParam sx1 = new Sprite.DESParam(30101, 0, 1);
	private static final Sprite.DESParam sx2 = new Sprite.DESParam(30103, 0, 1);
	private static final Sprite.DESParam[] sps = { sp0, sp1, sp2 };
	private static final Sprite.DESParam[] sxs = { sx0, sx1, sx2 };

	private static final int[] ns = { 8, 10, 10, 12 };
	private static final int[] m2s = { 20, 17, 14, 11 };
	private static final int[] dts = { 2, 2, 1, 1 };
	private static final int[] dws = { 300, 250, 250, 200 };

	private final int m2, dt, n;
	private final double w1;

	private int time = 0;

	public TestStage_003(int diff) {
		n = ns[diff];
		m2 = m2s[diff];
		dt = dts[diff];
		w1 = Math.PI * 2 / f / dws[diff];
	}

	@Override
	public boolean finished() {
		return time > 50000;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		double a1 = it * f * w1;
		P p = new P(o.x / 2, o.y / 2);
		for (int i = 0; i < n; i++) {
			double a0 = Math.PI * 2 / n * i + a1 + Math.PI * 2 / n / m * (it % m);
			int xt = (it % m) * dt * (f1 * 2 + f) + it / m * f1;
			Dot d = new Dot(p.copy(), P.polar(v0, a0), sps[it % m]);
			DotBullet db = new DotBullet(d);
			Koishi k = new Koishi(db, sxs[it % m], (m2 + 1) * f1, xt, m2 * f1);
			Engine.RUNNING.add(db);
			Engine.RUNNING.add(k);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			Engine.RUNNING.add(new Emiter(0, f, this, this));
		time += dt;

	}

}
