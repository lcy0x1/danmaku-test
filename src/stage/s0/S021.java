package stage.s0;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S021 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(20203, 0, 1);
	private static final Sprite.SParam d1 = Sprite.getDot(10506, 0);
	private static final Sprite.SParam d2 = Sprite.getDot(30001, 1, 2);

	private static final int f0 = 8000, t0 = 1000, t1 = 6000, f1 = 60, t2 = 1000, f2 = 160, t3 = 500, m = 4;

	private static final double v0 = 0.4, dt = 1e-4, va = -v0 / t0, v1 = 0.4;

	private static final int[] ns = { 24, 32, 40, 48 };

	private final int n;

	private P tmpp;
	private double tmpa;

	public S021(int diff) {
		super(60000);
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a0 = pc.atan2(getPlayer().pos);
			for (int i = 0; i < n; i++) {
				double a1 = a0 + p2 / n * i;
				P p0 = pc.copy();
				if (i == 0)
					tmpp = p0;
				P pv = P.polar(v0 + dt, a1);
				add(new DotBullet(new Dot(p0, pv, va, t0, d0), t1).setLv(K_FUNCTIONAL), ex);
			}
			add(new Emiter(1, f1, t1, this).setDelay(t0));
			add(new Emiter(2, f2, t1, this).setDelay(t0 + t2));
		}
		if (e.id == 1) {
			if (it == 0)
				tmpa = tmpp.atan2(getPlayer().pos);
			for (int i = 0; i < n; i++) {
				double a1 = tmpa + p2 / n * i;
				P p0 = P.polar(pc.dis(tmpp), pc.atan2(tmpp) + p2 / n * i).plus(pc);
				add(new DotBullet(new Dot(p0.copy(), P.polar(v1, a1), d1)), ex);
				add(new DotBullet(new Dot(p0, P.polar(v1, p0.atan2(pc)), d1), t3), ex);
			}
		}
		if (e.id == 2) {
			double a0 = pc.atan2(getPlayer().pos);
			for (int i = 0; i < n / m; i++) {
				double a1 = a0 + p2 / n * m * i;
				add(new DotBullet(new Dot(pc.copy(), P.polar(v1, a1), d2)), ex);
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
