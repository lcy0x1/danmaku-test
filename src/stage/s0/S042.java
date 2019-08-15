package stage.s0;

import battle.Entity;
import battle.Sprite;
import battle.bullet.Laser;
import battle.bullet.Mover;
import battle.bullet.TimeCurve;
import battle.bullet.Curve.ListCurve;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S042 extends SpellCard implements Emiter.Ticker {

	public static class Adder implements Emiter.Ticker {

		private final P pos;
		private final double a;
		private final ListCurve lc;

		public Adder(P p, ListCurve c, double a0) {
			pos = p;
			a = a0;
			lc = c;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {
			double a0 = a + (e.id * 2 - 1) * w0 * it;
			double a1 = a0 + it * w1;
			P p0 = pos.copy();
			DotBullet e0 = new DotBullet(new Dot(p0, P.polar(v0, a0), dls[e.id]), lts[e.id]);
			Entity e1 = new DotBullet(new Dot(p0, P.polar(v0, a1), dss[e.id]));
			lc.addP(e0);
			add(e0.trail(e1), ex);
		}

	}

	private static final Sprite.SParam dl0 = Sprite.getCurve(Sprite.P_C, 11402, 1, 1);
	private static final Sprite.SParam dl1 = Sprite.getCurve(Sprite.P_C, 11406, 1, 1);
	private static final Sprite.SParam[] dls = { dl0, dl1 };

	private static final Sprite.SParam dr0 = Sprite.getCurve(Sprite.P_SR, 10402, 1, 1);
	private static final Sprite.SParam dr1 = Sprite.getCurve(Sprite.P_SR, 10406, 1, 1);
	private static final Sprite.SParam[] drs = { dr0, dr1 };

	private static final Sprite.SParam ds0 = Sprite.getDot(10202, 0, 1);
	private static final Sprite.SParam ds1 = Sprite.getDot(10206, 0, 1);
	private static final Sprite.SParam[] dss = { ds0, ds1 };

	private static final int f0 = 4000, t0 = 500, f1 = 20, f2 = 40, at = 2000;
	private static final int n0 = 9, m1 = 12;
	private static final double v0 = 0.2, va = 2e-4, w0 = p2 / 9 / 30, w1 = p2 / 36;

	private static final int[] lts = { 2000, 1000 };

	private static final int[] ns = { 6, 7, 8, 9 };
	private static final int[] ms = { 3, 4, 5, 6 };

	private final int m0, n1;

	public S042(int diff) {
		super(60000);
		n1 = ns[diff] * 4;
		m0 = ms[diff] * 6;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id / 2 == 0) {
			int x = e.id % 2;
			double a0 = rand(p2);
			for (int i = 0; i < n0; i++) {
				ListCurve lc = new ListCurve(dls[x]);
				Adder ad = new Adder(pos, lc, a0 + p2 / n0 * i);
				add(new Emiter(x, f1, f1 * m0, ad), ex);
				add(new Laser(lc));
			}
		}
		if (e.id == 2) {
			double a0 = rand(p2);
			for (int i = 0; i < n1; i++) {
				P pv = P.polar(v0, a0 + p2 / n1 * i);
				Mover mov = new Mover.LineMover(pv, va, 0, at);
				TimeCurve lc = new TimeCurve(drs[i % 2], m1, f2, pos, mov, 0, at);
				add(new Laser(lc));
			}
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			add(new Emiter(0, f0, this, this));
			add(new Emiter(1, f0, this, this).setDelay(t0));
			add(new Emiter(2, f0, this, this).setDelay(t0 * 2));
		}
		super.update(dt);
	}

}
