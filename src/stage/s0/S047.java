package stage.s0;

import battle.Engine;
import battle.Entity;
import battle.Sprite;
import battle.Updatable;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S047 extends SpellCard implements Emiter.Ticker {

	private static class TimeShift extends Engine.Time.Mask {

		private final int tot;

		public TimeShift(int l) {
			super(l);
			tot = l;
		}

		@Override
		public double slow(Updatable e) {
			if (e instanceof Entity && e != getPlayer())
				return cos(p2 * len / tot) * 2 - 1;
			return 1;
		}

	}

	private static final Sprite.SParam dr0 = Sprite.getDot(20401, 0, 1, 0);
	private static final Sprite.SParam dr1 = Sprite.getDot(20403, 0, 1, 0);
	private static final Sprite.SParam[] drs = { dr0, dr1 };

	private static final int f0 = 10000, t0 = 2720, t1 = 2500, f1 = 40, n1 = 60;
	private static final double v0 = 0.4;

	private static final int[] ns = { 3, 4, 5, 6 };
	private final int n;

	public S047(int diff) {
		super(60000, new P(400, 200));
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			add(new Emiter(it % 2 + 1, f1, f1 * n1, this), ex);
			add(new Emiter(3, t0, this), ex);
		}

		if (e.id == 1 || e.id == 2) {
			for (int i = 0; i < n; i++) {
				P pv = P.polar(v0, rand(p2 / 4 * 3) + p2 / 8 * 3);
				Mover mov = new Mover.RefMover(pv, 2, 7);
				add(new DotBullet(new Dot(pos.copy(), drs[e.id - 1], mov)), ex);
			}
		}

		if (e.id == 3) {
			Engine.RUNNING.time.slow(new TimeShift(t1));
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(0, f0, this, this));
		super.update(dt);
	}

}
