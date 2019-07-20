package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import util.P;

public class S011 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_D, 10702, 0, 1);
	private static final Sprite.SParam d2 = Sprite.getSprite(Sprite.P_D, 10702, 2, 1);

	private static final int f = 20, l = 200;
	private static final double v0 = 0.2, w0 = p2 / 2000;

	private static final int[] ns = { 5, 6, 7, 8 };

	private final int n;

	public S011(int diff) {
		super(50000);
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		double w1 = w0 / n;
		for (int i = 0; i < n; i++) {
			double a0 = it * f * w0 + p2 / n * i * 2;
			double a1 = it * f * w1 + p2 / n * i;
			P p0 = P.polar(l, a1).plus(pc);
			P p1 = P.polar(l, a0).plus(p0);
			P pv = P.polar(-v0, a0);
			DotBullet b = new DotBullet(new Dot(p1, 2000, d0, d2).setMove(pv, 2e-4, 2000, 5000));
			add(b, ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			// for (int i = 0; i < n; i++)
			// add(new DotBullet(new Dot(d1, new Orbit(i, n))).setLv(K_FUNCTIONAL));
			add(new Emiter(0, f, this, this));
		}
		super.update(dt);
	}

}
