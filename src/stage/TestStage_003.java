package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import battle.special.Koishi;
import util.P;

public class TestStage_003 extends SpellCard implements Emiter.Ticker {

	private static final int f = 80, f1 = 120, m = 3;
	private static final double v0 = 0.1;

	private static final Sprite.DSP sp0 = new Sprite.DSP(10302, 0, 1);
	private static final Sprite.DSP sp1 = new Sprite.DSP(10306, 0, 1);
	private static final Sprite.DSP sp2 = new Sprite.DSP(10311, 0, 1);
	private static final Sprite.DSP sx0 = new Sprite.DSP(30100, 0, 1);
	private static final Sprite.DSP sx1 = new Sprite.DSP(30101, 0, 1);
	private static final Sprite.DSP sx2 = new Sprite.DSP(30103, 0, 1);
	private static final Sprite.DSP[] sps = { sp0, sp1, sp2 };
	private static final Sprite.DSP[] sxs = { sx0, sx1, sx2 };

	private static final int[] ns = { 8, 10, 10, 12 };
	private static final int[] m2s = { 20, 17, 14, 11 };
	private static final int[] dts = { 2, 2, 1, 1 };
	private static final int[] dws = { 300, 250, 250, 200 };

	private final int m2, dt, n;
	private final double w1;

	public TestStage_003(int diff) {
		super(50000);
		n = ns[diff];
		m2 = m2s[diff];
		dt = dts[diff];
		w1 = Math.PI * 2 / f / dws[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		double a1 = it * f * w1;
		for (int i = 0; i < n; i++) {
			double a0 = Math.PI * 2 / n * i + a1 + p2 / n / m * (it % m);
			int xt = (it % m) * dt * (f1 * 2 + f) + it / m * f1;
			Dot d = new Dot(pc.copy(), P.polar(v0, a0), sps[it % m]);
			DotBullet db = new DotBullet(d);
			Koishi k = new Koishi(db, sxs[it % m], (m2 + 1) * f1, xt, m2 * f1);
			add(db, ex);
			add(k, ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(0, f, this, this));
		super.update(dt);
	}

}
