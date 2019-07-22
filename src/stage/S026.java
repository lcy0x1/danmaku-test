package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import util.P;

public class S026 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_D, 10702, 0, 1);
	private static final Sprite.SParam d1 = Sprite.getSprite(Sprite.P_D, 30000, 0, 1);

	private static final int f0 = 5000, t0 = 4000, t1 = 2000, t2 = 1000;
	private static final double v0 = 0.6, va = -3e-4, v1 = 0.6, da = p2 / 24;

	private static final int[] ns = { 6, 8, 10, 12 };
	private static final int[] ms = { 24, 32, 40, 48 };
	private static final int[] fs = { 140, 120, 100, 80 };

	private final int n, m, f1;

	public S026(int diff) {
		super(60000, new P(400, 200));
		n = ns[diff];
		m = ms[diff];
		f1 = fs[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			add(new Emiter(1, f1, t0, this), ex);
			add(new Emiter(2, f1, t0, this).setDelay(t1), ex);
		}
		if (e.id == 1) {
			double a0 = pos.atan2(getPlayer().pos);
			for (int i = 0; i < m; i++) {
				P pv = P.polar(v0, p2 / m * i + p2 / m / 2 + a0);
				add(new DotBullet(new Dot(pos.copy(), pv, va, t2, d0)), ex);
			}
		}
		if (e.id == 2) {
			double a0 = pos.atan2(getPlayer().pos) + rand(da) - da / 2;
			for (int i = 0; i < n; i++) {
				P pv = P.polar(v1, p2 / n * i + a0);
				add(new DotBullet(new Dot(pos.copy(), pv, d1)), ex);
			}
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(0, f0, this, this));
		super.update(dt);
	}

}
