package stage.s0;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S040 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam sp0 = Sprite.getDot(10102, 0);
	private static final Sprite.SParam sp1 = Sprite.getDot(10106, 0);
	private static final Sprite.SParam st0 = Sprite.getDot(20101, 0);
	private static final Sprite.SParam st1 = Sprite.getDot(20103, 0);
	private static final Sprite.SParam[] sps = { sp0, sp1 };
	private static final Sprite.SParam[] sts = { st0, st1 };
	private static final int l = 200, df = 500;
	private static final double v0 = 0.3;
	private static final int[] ns = { 9, 10, 11, 12 };
	private static final int[] fs = { 6, 5, 4, 3 };
	private final int n, f0;

	public S040(int diff) {
		super(60000, new P(400, 200));
		n = ns[diff];
		f0 = fs[diff] * 200;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a0 = rand(p2);
			double da = rand(p2 / 2) - p2 / 4;
			for (int i = 0; i < 5; i++) {
				P p0 = P.polar(l, a0 + p2 / 5 * i);
				P p1 = P.polar(l, a0 + p2 / 5 * (i + 2));
				double dire = p0.copy().plus(p1).atan2();
				P pv1 = P.polar(v0, dire + da);
				for (int j = 0; j < n; j++) {
					P pv = p0.middle(p1, 1.0 / n * j).times(1.0 / df);
					P pb = pos.copy();
					Mover mov = new Mover.RefMover(pv1.copy(), 1, 7);
					DotBullet db = new DotBullet(new Dot(pb, pv, sts[it % 2]), df);
					db.trail(new DotBullet(new Dot(pb, sps[it % 2], mov)));
					add(db.setLv(K_FUNCTIONAL), ex);

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
