package stage.s0;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S024 extends SpellCard implements Emiter.Ticker {

	private static class Back implements Mover {

		private final P pv;
		private final double a;

		private boolean hit = false;

		private int time, at;

		private Back(P v, double a0, int st) {
			pv = v;
			a = a0;
			at = t0;
			time = st;
		}

		@Override
		public boolean out(P pos, double r) {
			return time < 0 && hit && pos.moveOut(pv, o, r);
		}

		@Override
		public void update(Dot d, int t) {
			d.tmp.plus(pv, t);
			if (!hit)
				if (hit = d.tmp.limit(o)) {
					d.tmp.setTo(d.tmp.onLimit(pv, new P(0, 0), o));
					pv.times(1e-5);
				}
			if (at > 0 && (time < 0 || !hit))
				pv.times(1 + t * a / pv.abs());

			if (time >= 0 && time < t) {
				at = t0;
				pv.setTo(P.polar(1e-5, d.tmp.atan2(getPlayer().pos)));
			}
			time -= t;
			at -= t;
		}

	}

	private static final Sprite.SParam d0 = Sprite.getDot(10702, 0);

	private static final int f0 = 5000, f1 = 20, t0 = 2000, t1 = 2000, t2 = 500, t3 = 1000;
	private static final double v0 = 0.5, av = 3e-4;

	private static final P lim0 = new P(200, 200), lim1 = new P(600, 800);

	private static final int[] ns = { 20, 20, 20, 20 };
	private static final int[] ms = { 21, 28, 35, 42 };

	private final int n, m;

	private double dire;
	private P ppos, npos;

	public S024(int diff) {
		super(60000, new P(400, 200));
		n = ns[diff];
		m = ms[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			add(new Emiter(1, f1, t1 + f1 * n, this).setDelay(t1), ex);
			add(new Emiter(2, f1, t2 + t3, this).setDelay(t2), ex);
		}
		if (e.id == 1) {
			if (it == 0)
				dire = pos.atan2(getPlayer().pos);
			for (int j = 0; j < m; j++) {
				P pv = P.polar(v0, p2 / n / m * (it + j * n) + dire);
				add(new DotBullet(new Dot(pos.copy(), d0, new Back(pv, av, t0 - it * f1))), ex);
			}
		}
		if (e.id == 2) {
			if (it == 0) {
				ppos = pos.copy();
				npos = getPlayer().pos.copy();
				npos.limit(lim0, lim1);
			}
			pos.setTo(ppos.middleC(npos, 1.0 * it * f1 / t3));
		}

	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(0, f0, this, this));
		super.update(dt);
	}

}
