package stage.s0;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S036 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(10506, 0);

	private static final int f0 = 40, t0 = 3000;
	private static final double v0 = 0.2, w0 = p2 / 3456789, dva = 18e-6;

	private static final int[] ns = { 3, 4, 5, 6 };
	private static final int[] ms = { 3, 4, 5, 6 };

	private final int n, m;

	public S036(int diff) {
		super(60000);
		n = ns[diff];
		m = ms[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a0 = w0 * it * f0 * it * f0 / 2;
			for (int i = 0; i < n; i++) {
				double a1 = a0 + p2 / n * i;
				for (int j = 0; j < m; j++) {
					double va = dva * j;
					add(new DotBullet(new Dot(pos.copy(), P.polar(v0, a1), va, t0, d0)), ex);
				}
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
