package stage;

import battle.Engine;
import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import util.P;

public class TestStage_000 extends SpellCard {

	private static class TrigMover extends Dot.TimeMover {

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

	private static final int[] ds = { 75000, 80000, 85000, 90000 };
	private static final int[] ns = { 6, 7, 8, 9 };

	// private static final int[] ds= {81000,81000,81000,81000};
	// private static final int[] ns = { 9, 10, 11, 12 };

	private final int n, d;
	private final boolean extra = true;

	public TestStage_000(int diff) {
		super(ds[diff]);
		d = ds[diff];
		n = ns[diff];
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			double w = p2 / 2 / d;
			Sprite.DESParam s0 = new Sprite.DESParam(20301, 1, 1);
			Sprite.DESParam s1 = new Sprite.DESParam(20303, 1, 1);
			Sprite.DESParam s2 = new Sprite.DESParam(20305, 1, 1);
			Sprite.DESParam s3 = new Sprite.DESParam(20306, 1, 1);
			for (int i = -n; i <= n; i++)
				for (int j = -n; j <= n; j++) {
					Dot d0 = new Dot(s0, new TrigMover(w * i, w * j, 0, p2 / 4));
					Engine.RUNNING.add(new DotBullet(d0, d).setLv(K_FUNCTIONAL));
					Dot d1 = new Dot(s1, new TrigMover(w * i, w * j, 0, -p2 / 4));
					Engine.RUNNING.add(new DotBullet(d1, d).setLv(K_FUNCTIONAL));
					if (!extra)
						continue;
					Dot d2 = new Dot(s2, new TrigMover(w * i, w * j, p2 / 4, 0));
					Engine.RUNNING.add(new DotBullet(d2, d).setLv(K_FUNCTIONAL));
					Dot d3 = new Dot(s3, new TrigMover(w * i, w * j, -p2 / 4, 0));
					Engine.RUNNING.add(new DotBullet(d3, d).setLv(K_FUNCTIONAL));
				}
		}
		super.update(dt);
	}

}
