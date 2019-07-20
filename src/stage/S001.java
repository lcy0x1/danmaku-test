package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import util.P;

public class S001 extends SpellCard implements Emiter.Ticker {

	private static class Orbit extends Mover.TimeMover {

		private final int i, n;

		private Orbit(int x, int m) {
			i = x;
			n = m;
		}

		@Override
		public P disp(int t) {
			double a0 = t * w0 + p2 / n * i * 2;
			double a1 = t * w1 + p2 / n * i;
			P p0 = P.polar(l, a1).plus(pc);
			return P.polar(l, a0).plus(p0);
		}

		@Override
		public boolean out(P pos, double r) {
			return false;
		}

	}

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_D, 10702, 0, 1);
	private static final Sprite.SParam d1 = Sprite.getSprite(Sprite.P_D, 30000, 1, 1);

	private static final int f = 20, l = 200, m = 80;
	private static final double v0 = 0.1, w0 = p2 / 4690, w1 = p2 / 19752;

	private static final int[] ns = { 5, 6, 7, 8 };

	private final int n;

	public S001(int diff) {
		super(50000);
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		for (int i = 0; i < n; i++) {
			if ((it + 2 * m * i / n) / m % 2 == 0)
				continue;
			double a0 = it * f * w0 + p2 / n * i * 2;
			double a1 = it * f * w1 + p2 / n * i;
			P p0 = P.polar(l, a1).plus(pc);
			P p1 = P.polar(l, a0).plus(p0);
			P pv = P.polar(-v0, a0);
			DotBullet b = new DotBullet(new Dot(p1, pv, d0));
			add(b, ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			for (int i = 0; i < n; i++)
				add(new DotBullet(new Dot(d1, new Orbit(i, n))).setLv(K_FUNCTIONAL));
			add(new Emiter(0, f, this, this));
		}
		super.update(dt);
	}

}
