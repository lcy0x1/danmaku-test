package stage.s0;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import battle.special.Roting;
import stage.SpellCard;
import util.P;

public class S028 extends SpellCard implements Emiter.Ticker {

	private static class Rot2 extends Roting {

		private final double a, w;

		private Rot2(P cen, P v0, double w0, double w1, double a0, double a1, double r0) {
			super(cen, v0, w0, a0, r0);
			w = w1;
			a = a1;
		}

		@Override
		protected double getR(int t) {
			return super.maxR() * (1 + Math.sin(t * w + a)) / 2;
		}

	}

	private static final Sprite.SParam d0 = Sprite.getDot(10114, 0);
	private static final Sprite.SParam d1 = Sprite.getDot(10113, 0);

	private static final Sprite.SParam[] ds = { d0, d1 };

	private static final int n = 12, m0 = 2, m1 = 4;
	private static final double l = 200, v0 = 0.3, w0 = p2 / 6000, w1 = p2 / 3000, da = p2 / 4;

	private static final int[] fs = { 600, 540, 480, 420 };

	private final int f0;

	public S028(int diff) {
		super(60000, new P(400, 200));
		f0 = fs[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			int s = it % 2 * 2 - 1;
			double b0 = rand(p2);
			P p0 = new P(o.x / 2 * (it % 2) + rand(o.x / 2), -l);
			for (int i = 0; i < m0; i++)
				for (int j = 0; j < m1; j++) {
					double b1 = b0 + da * i;
					double a0 = rand(p2);
					double l0 = l / m1 * (j + 1);
					for (int k = 0; k < n; k++) {
						double a1 = a0 + p2 / n * k;
						Rot2 mov = new Rot2(p0, new P(0, v0), s * w0, w1, a1, b1, l0);
						add(new DotBullet(new Dot(ds[i], mov)), ex);
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
