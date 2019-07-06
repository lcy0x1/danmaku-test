package stage;

import battle.Engine;
import battle.Sprite;
import battle.entity.Dot;
import battle.entity.DotBullet;
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

	private static final int[] ns = { 9, 10, 11, 12 };

	private final int n;

	public TestStage_000(int diff) {
		super(81000);
		n = ns[diff];
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			double w = p2 / 192000;
			Sprite.DESParam s0 = new Sprite.DESParam(20301, 1, 1);
			Sprite.DESParam s1 = new Sprite.DESParam(20303, 1, 1);
			P o = Engine.BOUND;
			for (int i = -n; i <= n; i++)
				for (int j = -n; j <= n; j++) {
					Dot d0 = new Dot(new P(o.x / 2, o.y), s0, new TrigMover(w * i, w * j, 0, p2 / 4));
					Engine.RUNNING.add(new DotBullet(d0).setLv(K_FUNCTIONAL));
					Dot d1 = new Dot(new P(o.x / 2, 0), s1, new TrigMover(w * i, w * j, 0, -p2 / 4));
					Engine.RUNNING.add(new DotBullet(d1).setLv(K_FUNCTIONAL));
				}
		}
		super.update(dt);
	}

}
