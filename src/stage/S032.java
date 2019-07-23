package stage;

import battle.Entity;
import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import util.P;

public class S032 extends SpellCard implements Emiter.Ticker {

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
			int x = (int) (x0 * 2 + 5);
			int time = it * f1;
			int t1 = (it / x * x) * f1;
			double a0 = ia0 + time * w0;
			double lt = l1 + v1 * t1;
			double a1 = ia1 + w1 * time;
			P p = P.polar(l0, a0).plus(pos);
			if (it % x < x0) {
				P pv = P.polar(-v2, a0 + da);
				P pv0 = P.polar(1e-5, a1);
				int dt = (int) ((Math.sqrt(v2 * v2 + 2 * ac0 * lt) - v2) / ac0) / 20 * 20;
				Entity b0 = new DotBullet(new Dot(p, pv, ac0, dt, dss[e.id]), dt);
				Entity b1 = new DotBullet(new Dot(p, pv0, ds[e.id]), t0 - time);
				Entity b2 = new DotBullet(new Dot(p, pv0, ac1, t2, ds[e.id]));
				add(b0.trail(b1.trail(b2)), ex);
			}

			P pv1 = P.polar(v0, pc.atan2(p));
			add(new DotBullet(new Dot(p.copy(), pv1, d3)), ex);
		}

	}

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_D, 10102, 0, 1);
	private static final Sprite.SParam d1 = Sprite.getSprite(Sprite.P_D, 10104, 0, 1);
	private static final Sprite.SParam d2 = Sprite.getSprite(Sprite.P_D, 10106, 0, 1);
	private static final Sprite.SParam[] ds = { d0, d1, d2 };

	private static final Sprite.SParam ds0 = Sprite.getSprite(Sprite.P_D, 10102, 2, 1);
	private static final Sprite.SParam ds1 = Sprite.getSprite(Sprite.P_D, 10104, 2, 1);
	private static final Sprite.SParam ds2 = Sprite.getSprite(Sprite.P_D, 10106, 2, 1);
	private static final Sprite.SParam[] dss = { ds0, ds1, ds2 };

	private static final Sprite.SParam d3 = Sprite.getSprite(Sprite.P_D, 10115, 0, 1);
	private static final Sprite.SParam d4 = Sprite.getSprite(Sprite.P_D, 30000, 1, 1);

	private static final int f0 = 6000, f1 = 20, t0 = 3000, l0 = 500, l1 = 200, m = 3, t2 = 3000, t3 = 1000;
	private static final double v0 = 0.2, v1 = (l0 - l1) * 2.0 / t0, v2 = 0.1, da = p2 / 6, ac0 = 2e-4, ac1 = 1e-4;
	private static final double m0 = 0.5, m1 = 1;

	private static final int[] ns = { 6, 8, 10, 12 };

	private final int n;

	public S032(int diff) {
		super(96000);
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a = rand(p2);
			int s = it % 2 * 2 - 1;
			double w1 = s * (p2 / t0 / 4 + rand(p2 / t0 / 4 * 3));
			for (int i = 0; i < m; i++) {
				double a0 = a + p2 / n / m * i;
				int nx = 3 + (int) rand(3);
				double dia = rand(da) - da / 2;
				for (int j = 0; j < n; j++) {
					double a1 = a0 + p2 / n * j;
					double w2 = s * (p2 / t0 * m0 + m1 * rand(p2 / t0));
					double ia = rand(p2);
					DotBullet b0 = new DotBullet(new Dot(pos, l0, a1, 0, w1, d4), t0);
					P pv0 = P.polar(w1 * l0, a1 + p2 / 4);
					P p0 = P.polar(l0, a1).plus(pos).plus(pv0, -t3);
					DotBullet b1 = new DotBullet(new Dot(p0, pv0, d4), t3);
					P p1 = P.polar(l0, a1 + w1 * t0).plus(pos);
					P pv1 = P.polar(w1 * l0, a1 + w1 * t0 + p2 / 4);
					DotBullet b2 = new DotBullet(new Dot(p1, pv1, d4));
					add(b1.trail(b0.trail(b2)), ex);
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
