package stage.s0;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S029 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(10702, 0);
	private static final Sprite.SParam d1 = Sprite.getDot(10706, 0);

	private static final int f0 = 40, l = 1000, m = 4, nx = 100;
	private static final double v0 = 0.2, w1 = p2 / 2500, w2 = p2 / 2000;

	private static final int[] ns = { 9, 12, 15, 18 };

	private final int n;
	private double w0;

	public S029(int diff) {
		super(60000);
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			if (it % nx == 0)
				w0 = w1 + rand(1) * (w2 - w1);
			add(it, ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(0, f0, this, this));
		super.update(dt);
	}

	private void add(int it, int ex) {
		double a0 = w0 * f0 * it;
		for (int i = 0; i < n; i++) {
			double a1 = a0 + p2 / n * i;
			double a2 = it / m * m * -w0 * f0 + p2 / n * i;
			P pv0 = P.polar(v0, a1);
			P pv1 = P.polar(v0, a2);
			P b = pos.copy();
			DotBullet bs0 = new DotBullet(new Dot(b, pv0, d1), l);
			DotBullet bs1 = new DotBullet(new Dot(b, pv1, d0));
			add(bs0.trail(bs1).setLv(K_FUNCTIONAL), ex);
		}
	}

}
