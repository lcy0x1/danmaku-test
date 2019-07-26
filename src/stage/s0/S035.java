package stage.s0;

import battle.Entity;
import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S035 extends SpellCard implements Emiter.Ticker {

	private static class Sub implements Emiter.Ticker {

		// th vt l v a w x w2 time
		private final double ia0, da, w0, ia1, x0, w1;
		private final P pos;

		private Sub(double th, double dia, double vt, double a, double x, double wx1, P p) {
			pos = p;
			ia0 = th;
			w0 = vt;
			ia1 = a;
			x0 = x;
			w1 = wx1;
			da = dia;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {
			int x = (int) (x0 * 2 + 2);
			int time = it * f1;
			int t1 = (it / x * x) * f1;
			double a0 = ia0 + time * w0;
			double lt = l1 + v1 * t1;
			double dlt = l1 + v1 * time;
			double a1 = ia1 + w1 * (time + t1) / 2;
			P p = P.polar(l0, a0).plus(pos);
			if (it % x < x0) {
				P pv = P.polar(-v2, a0 + da);
				P pv0 = P.polar(1e-5, a1);
				int dt = (int) ((Math.sqrt(v2 * v2 + 2 * ac0 * lt) - v2) / ac0) / 20 * 20;
				double xt = dlt - dt * dt * ac0 / 2 - dt * v2;
				P p0 = P.polar(xt, pv.atan2()).plus(p);
				Entity b0 = new DotBullet(new Dot(p0, pv, ac0, dt, dss[e.id]), dt);
				Entity b1 = new DotBullet(new Dot(p0, pv0, ds[e.id]), tl - time);
				Entity b2 = new DotBullet(new Dot(p0, pv0, ac1, t2, ds[e.id]));
				add(b0.trail(b1.trail(b2)), ex);
			}
		}

	}

	private static final Sprite.SParam d0 = Sprite.getDot(10102, 0);
	private static final Sprite.SParam d1 = Sprite.getDot(10104, 0);
	private static final Sprite.SParam d2 = Sprite.getDot(10106, 0);
	private static final Sprite.SParam[] ds = { d0, d1, d2 };

	private static final Sprite.SParam ds0 = Sprite.getDot(10102, 2);
	private static final Sprite.SParam ds1 = Sprite.getDot(10104, 2);
	private static final Sprite.SParam ds2 = Sprite.getDot(10106, 2);
	private static final Sprite.SParam[] dss = { ds0, ds1, ds2 };

	private static final int f0 = 6000, f1 = 20, t0 = 3000, tl = 3000, l0 = 700, l1 = 200, m = 3, t2 = 3000, t3 = 1000;
	private static final double v1 = (l0 - l1) * 2.0 / t0, v2 = 0.1, da = p2 / 12, ac0 = 2e-4, ac1 = 1e-4;
	private static final double w10 = p2 / 6000, w11 = p2 / 2000, w00 = p2 / 8000, w01 = p2 / 6000;

	private static final int[] ns = { 3, 4, 6, 8 };

	private final int n;

	public S035(int diff) {
		super(96000);
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a = rand(p2);
			int s = it % 2 * 2 - 1;
			double w1 = s * (w00 + rand(w01 - w00));
			for (int i = 0; i < m; i++) {
				double a0 = a + p2 / n / m * i;
				int nx = 3 + (int) rand(3);
				double dia = da * Math.pow(rand(1), 2) - da / 2;
				double w2 = s * (w10 + rand(w11 - w10));
				double ia = rand(p2);
				for (int j = 0; j < n; j++) {
					double a1 = a0 + p2 / n * j;
					Sub sub = new Sub(a1, dia, w1, ia + a1, nx, w2, pos);
					add(new Emiter(i, f1, t0 + t3, sub).setDelay(t3), ex);
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
