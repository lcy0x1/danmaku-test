package stage.s0;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S037 extends SpellCard implements Emiter.Ticker {

	private static class Mov implements Mover {

		private static final double err = 0.9;

		private final P v, p0, p1;

		private final double a;

		private Mov(P pv, double va, P pos, P pl) {
			v = pv;
			p0 = pos;
			p1 = pl;
			a = va;
		}

		@Override
		public double getDire() {
			return v.atan2();
		}

		@Override
		public boolean out(P pos, double r) {
			return pos.moveOut(v, o, r) && cos(v.atan2() - p0.atan2(p1)) > err;
		}

		@Override
		public void update(Dot d, int t) {
			d.tmp.plus(v, t);
			v.plus(P.polar(a, p0.atan2(p1)), t);
		}

	}

	private static class Sub implements Emiter.Ticker {

		private final P pos, pl;
		private final double va, a1, w0;

		private Sub(P p, P px, double a0, double a, double d) {
			pos = p;
			a1 = a0;
			va = a;
			w0 = d;
			pl = px;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {
			P pv = P.polar(v0 * va, a1 + it * dx * w0 * fac);
			add(new DotBullet(new Dot(pos.copy(), d0, new Mov(pv, va, pos, pl))), ex);
		}

	}

	private static final Sprite.SParam d0 = Sprite.getDot(10408, 0);

	private static final int f0 = 60, dx = 40, m = 6;
	private static final double v0 = 2e3, w0 = p2 / 12345, w1 = p2 / 1234, fac = 2.0 / dx;

	private static final int[] ns = { 3, 4, 5, 6 };
	private static final int[] as = { 10, 12, 14, 16 };

	private final int n;
	private final double va;

	public S037(int diff) {
		super(60000, new P(400, 200));
		n = ns[diff];
		va = as[diff] * 1e-5;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a0 = w1 / w0 * cos(w0 * it * f0);
			for (int i = 0; i < n; i++) {
				double a1 = a0 + p2 / n * i;
				add(new Emiter(0, dx, dx * m, new Sub(pos, getPlayer().pos, a1, va, w1 * sin(w0 * it * f0))), ex);
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
