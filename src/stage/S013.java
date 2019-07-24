package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import battle.special.Roting;
import util.P;

public class S013 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(10402, 0);
	private static final Sprite.SParam d1 = Sprite.getDot(10406, 0);

	private static final double l = 50;

	private static final int[] ns0 = { 40, 45, 50, 55 };
	private static final int[] ms0 = { 3, 4, 5, 6 };
	private static final int[] ns1 = { 60, 90, 90, 110 };
	private static final int[] ms1 = { 2, 2, 3, 3 };
	private static final int[] fs = { 1600, 1400, 1200, 1000 };

	private final int n0, n1, m0, m1, f;
	private final double v0, w0;

	public S013(int diff) {
		super(60000);
		n0 = ns0[diff];
		m0 = ms0[diff];
		n1 = ns1[diff];
		m1 = ms1[diff];

		f = fs[diff];
		v0 = 2 * l / f;
		w0 = v0 / l;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (it % 2 == 0)
			adds(n0, m0, ex, it / 2 % 2 * 2 - 1, d0);
		else
			adds(n1, m1, ex, it / 2 % 2 * 2 - 1, d1);
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			add(new Emiter(0, f, this, this));
		}
		super.update(dt);
	}

	private void adds(int n, int m, int ex, int s0, Sprite.SParam d) {
		double a0 = rand(p2);
		for (int i = 0; i < n; i++) {
			double a1 = a0 + p2 / n * i;
			double a2 = a1 + p2 / n * m * i;
			P pv = P.polar(v0, a1);
			for (int j = 0; j < m; j++)
				add(new DotBullet(new Dot(d, new Roting(pc, pv, s0 * w0, a2 + p2 / m * j, l))), ex);
		}
	}

}
