package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import util.P;

public class S000 extends SpellCard {

	private static class TrigMover extends Mover.TimeMover {

		private final double wx, wy, ix, iy;

		public TrigMover(double vx, double vy, double px, double py) {
			wx = vx;
			wy = vy;
			ix = px;
			iy = py;
		}

		@Override
		public P disp(int t) {
			return new P(Math.sin(t * wx + ix) + 1, Math.sin(t * wy + iy) + 1).times(0.5).times(o);
		}

		@Override
		public boolean out(P pos, double r) {
			return false;
		}

	}

	private static final Sprite.SParam s0 = Sprite.getDot(20301, 1);
	private static final Sprite.SParam s1 = Sprite.getDot(20303, 1);
	private static final Sprite.SParam s2 = Sprite.getDot(20305, 1);
	private static final Sprite.SParam s3 = Sprite.getDot(20306, 1);

	private static final int[] ds = { 75000, 80000, 85000, 90000 };
	private static final int[] ns = { 6, 7, 8, 9 };

	// private static final int[] ds= {81000,81000,81000,81000};
	// private static final int[] ns = { 9, 10, 11, 12 };

	private final int n, d;
	private final boolean extra = true;

	public S000(int diff) {
		super(ds[diff]);
		d = ds[diff];
		n = ns[diff];
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			double w = p2 / 2 / d;
			for (int i = -n; i <= n; i++)
				for (int j = -n; j <= n; j++) {
					Dot d0 = new Dot(s0, new TrigMover(w * i, w * j, 0, p2 / 4));
					add(new DotBullet(d0, d).setLv(K_FUNCTIONAL));
					Dot d1 = new Dot(s1, new TrigMover(w * i, w * j, 0, -p2 / 4));
					add(new DotBullet(d1, d).setLv(K_FUNCTIONAL));
					if (!extra)
						continue;
					Dot d2 = new Dot(s2, new TrigMover(w * i, w * j, p2 / 4, 0));
					add(new DotBullet(d2, d).setLv(K_FUNCTIONAL));
					Dot d3 = new Dot(s3, new TrigMover(w * i, w * j, -p2 / 4, 0));
					add(new DotBullet(d3, d).setLv(K_FUNCTIONAL));
				}
		}
		super.update(dt);
	}

}
