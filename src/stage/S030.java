package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import util.P;

public class S030 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_D, 10506, 0, 1);

	private static final int f0 = 40, m = 5, t0 = 2000;
	private static final double w0 = p2 / 3456789;

	private static final int[] ns = { 3, 4, 5, 6 };
	private static final double[] vs = { 0.1, 0.15, 0.2, 0.25, 0.3 };
	private static final double[] as = { 3e-4, 2e-4, 1e-4, 0, -1e-4 };

	private final int n;

	public S030(int diff) {
		super(60000);
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a0 = w0 * it * f0 * it * f0 / 2;
			for (int i = 0; i < n; i++) {
				double a1 = a0 + p2 / n * i;
				for (int j = 0; j < m; j++) {
					P pv = P.polar(vs[j], a1);
					add(new DotBullet(new Dot(pos.copy(), pv, as[j], t0, d0)), ex);
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
