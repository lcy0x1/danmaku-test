package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import battle.special.Koishi;
import util.P;

public class S003 extends SpellCard implements Emiter.Ticker {

	private static final int f = 80, f1 = 120, m = 3;
	private static final double v0 = 0.1;

	private static final Sprite.SParam sp0 = Sprite.getSprite(Sprite.P_D, 10302, 0, 1);
	private static final Sprite.SParam sp1 = Sprite.getSprite(Sprite.P_D, 10306, 0, 1);
	private static final Sprite.SParam sp2 = Sprite.getSprite(Sprite.P_D, 10311, 0, 1);
	private static final Sprite.SParam sx0 = Sprite.getSprite(Sprite.P_D, 30100, 0, 1);
	private static final Sprite.SParam sx1 = Sprite.getSprite(Sprite.P_D, 30101, 0, 1);
	private static final Sprite.SParam sx2 = Sprite.getSprite(Sprite.P_D, 30103, 0, 1);
	private static final Sprite.SParam[] sps = { sp0, sp1, sp2 };
	private static final Sprite.SParam[] sxs = { sx0, sx1, sx2 };

	private static final int[] ns = { 8, 10, 10, 12 };
	private static final int[] m2s = { 20, 17, 14, 11 };
	private static final int[] dts = { 2, 2, 1, 1 };
	private static final int[] dws = { 300, 250, 250, 200 };

	private final int m2, dt, n;
	private final double w1;

	public S003(int diff) {
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
			Koishi k = new Koishi(sps[it % m], sxs[it % m], (m2 + 1) * f1, xt, m2 * f1);
			Dot d = new Dot(pc.copy(), k).setMove(P.polar(v0, a0));
			add(new DotBullet(d), ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(0, f, this, this));
		super.update(dt);
	}

}
