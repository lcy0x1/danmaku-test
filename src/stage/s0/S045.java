package stage.s0;

import battle.Sprite;
import battle.bullet.BulletRing;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S045 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam dr0 = Sprite.getDot(10502, 0);
	private static final Sprite.SParam dr1 = Sprite.getDot(10506, 0);

	private static final int f0 = 100;
	private static final double v0 = 0.2, v1 = 0.3, w1 = p2 / 1234, w2 = p2 / 12000, da = p2 / 12;

	private static final int[] ns = { 3, 4, 5, 6 };

	private final int n;

	public S045(int diff) {
		super(60000);
		n = ns[diff] * 4;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			int t = it * f0;

			double a0 = da * sin(w1 / 2 * (t + sin(w2 * t) / w2));
			double a1 = -da * sin(w1 / 2 * (t - sin(w2 * t) / w2));

			add(new BulletRing(pos, dr0, n, P.polar(v1, a0)), ex);
			add(new BulletRing(pos, dr1, n, P.polar(v0, a1)), ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			add(new Emiter(0, f0, this, this));
		}
		super.update(dt);
	}

}
