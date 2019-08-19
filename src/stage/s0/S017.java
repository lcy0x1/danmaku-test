package stage.s0;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S017 extends SpellCard.LifeSpell implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(20403, 0);

	private static final int f0 = 8000, f1 = 100, t1 = 30;
	private static final int ref = 1;

	private static final double v0 = 0.4;

	private static final int[] ns = { 4, 5, 6, 7 };

	private final int n;

	public S017(int diff) {
		super(60000, new P(400, 200), Sprite.BS_SAKUYA);
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			add(new Emiter(1, f1, f1 * t1, this), ex);
		}
		if (e.id == 1) {
			double a0 = pos.atan2(getPlayer().pos);
			for (int i = 0; i < n; i++) {
				double a1 = a0 + p2 / n * i;
				P p0 = pos.copy();
				P pv = P.polar(v0, a1);
				Mover mov = new Mover.RefMover(pv, ref, 7);
				DotBullet d1 = new DotBullet(new Dot(p0, d0, new Mover.HomingLM(v0)));
				add(new DotBullet(new Dot(p0, d0, mov)).trail(d1), ex);
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
