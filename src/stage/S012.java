package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import util.P;

public class S012 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(10213, 0);

	private static final double l = 800;
	private static final double w0 = 0, w1 = p2 / 30000;

	private static final int[] ns = { 12, 14, 16, 18 };
	private static final int[] ms = { 3, 3, 3, 3 };
	private static final double[] vs = { 0.1, 0.11, 0.12, 0.13 };

	private final int n, m, f;
	private final double v0;

	public S012(int diff) {
		super(60000);
		n = ns[diff];
		m = ms[diff];
		v0 = vs[diff];
		f = 20 * (int) (l * 3 / n / v0 / 20);
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		for (int i = 0; i < 4; i++) {
			double a0 = p2 / 4 * i + it * f * w0;
			P p0 = P.polar(l, a0 - p2 / 8).plus(pc);
			P p1 = P.polar(l, a0 + p2 / 8).plus(pc);
			for (int j = 0; j < n; j++) {
				if ((j + it) % 2 == 0)
					continue;
				P bc = p0.middle(p1, (0.5 + j) / n);
				double dl = l / n / m;
				double a1 = a0 + it * f * w1;
				for (int k = -m; k < m; k++) {
					P bp = P.polar(dl * (k + 0.5), a1).plus(bc);
					P pv = P.polar(-v0, a0);
					add(new DotBullet(new Dot(bp, pv, d0)), ex);
				}
			}
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			add(new Emiter(0, f, this, this));
		}
		super.update(dt);
	}

}
