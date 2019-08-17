package stage.s0;

import battle.Sprite;
import battle.bullet.BulletRing;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S046 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam dr0 = Sprite.getDot(10702, 0, 1, 0);
	private static final Sprite.SParam dr1 = Sprite.getDot(10706, 0, 1, 0);
	private static final Sprite.SParam[] drs = { dr0, dr1 };

	private static final Sprite.SParam ds0 = Sprite.getDot(20201, 0, 1, 1);
	private static final Sprite.SParam ds1 = Sprite.getDot(20203, 0, 1, 1);
	private static final Sprite.SParam[] dss = { ds0, ds1 };

	private static final Sprite.SParam dl0 = Sprite.getDot(20201, 1, 1, 2);
	private static final Sprite.SParam dl1 = Sprite.getDot(20203, 1, 1, 2);
	private static final Sprite.SParam[] dls = { dl0, dl1 };

	private static final int f0 = 100, n0 = 16, l0 = 400, t0 = 2000;
	private static final double v0 = 0.4, v1 = 0.3;

	private static final int[] ns = { 6, 7, 8, 9 };
	private static final int[] f1s = { 1000, 900, 800, 700 };
	private static final int[] f2s = { 550, 500, 450, 400 };
	private final int n, f1, f2;

	private final double[] as = new double[n0];

	public S046(int diff) {
		super(60000, new P(400, 200));
		n = ns[diff];
		f1 = f1s[diff];
		f2 = f2s[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			for (int i = 0; i < n0; i++) {
				for (int j = 0; j < 2; j++) {
					P p0 = P.polar(l0, p2 / n0 * i - p2 / 4).plus(pc);
					P pv = P.polar(v0, p2 / n0 * i - p2 / 4 + p2 / 8 * 3 * (j * 2 - 1));
					add(new DotBullet(new Dot(p0, pv, drs[i % 2])), ex);
				}
				if (Double.isFinite(as[i])) {
					P p0 = P.polar(l0, p2 / n0 * i - p2 / 4).plus(pc);
					P pv = P.polar(v0, as[i] + p2 / n / 2 * (i % 2));
					Mover mov = new Mover.LineMover(pv);
					add(new BulletRing(p0, drs[i % 2], n, mov), ex);
				}
			}
		}
		if (e.id == 1) {
			as[it] = P.polar(l0, p2 / n0 * it - p2 / 4).plus(pc).atan2(getPlayer().getPos());
		}
		if (e.id == 2) {
			adds(it, ex);
			if (it > n0)
				adds(it + n0 / 2, ex);
			if (it > n0 * 2)
				adds(it + n0 / 4 * 3, ex);
			if (it > n0 * 3)
				adds(it + n0 / 4, ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			for (int i = 0; i < n0; i++) {
				as[i] = Double.NaN;
				P p0 = P.polar(l0, p2 / n0 * i - p2 / 4).plus(pc);
				double va = pos.dis(p0) * 2 / t0 / t0;
				P pv = P.polar(va * t0 + 1e-5, pos.atan2(p0));
				add(new DotBullet(new Dot(pos.copy(), pv, -va, t0, dss[i % 2])).setLv(K_FUNCTIONAL));
			}
			add(new Emiter(0, f0, this, this).setDelay(t0));
			add(new Emiter(1, f1, f1 * n0 + t0, this).setDelay(t0));
			add(new Emiter(2, f2, this, this).setDelay(f1 * n0 + t0));
		}
		super.update(dt);
	}

	private void adds(int it, int ex) {
		P p0 = P.polar(l0, p2 / n0 * it - p2 / 4).plus(pc);
		P pv = P.polar(v1, p0.atan2(getPlayer().getPos()));
		add(new DotBullet(new Dot(p0, pv, dls[(it + 2) % 2])), ex);
	}

}
