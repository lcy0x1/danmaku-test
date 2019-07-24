package stage;

import battle.Sprite;
import battle.bullet.Curve;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Func;
import battle.bullet.Laser;
import battle.entity.Emiter;
import util.P;

public class S005 extends SpellCard implements Emiter.Ticker {

	private static class PathFunc implements Func {

		private final double r0, a0, w0;

		private PathFunc(double r, double a, double w) {
			r0 = r;
			a0 = a;
			w0 = w;
		}

		@Override
		public boolean exist(int time, int i) {
			return (i - 1) * 200 < time;
		}

		@Override
		public P func(int time, int i) {
			double tot;
			if (time - i * xt < 0)
				tot = 0;
			else {
				double max = (m - 1 - i) * dr;
				double mt = 1.0 * (Math.min(xt * m, time) - i * xt) / (xt * m - i * xt);
				tot = max * Math.sin(mt * Math.PI / 2);
			}
			double a = a0 + Math.sin(r0 + (-time + i * w0 / vt) * p2 / w0) * ma;
			return P.polar(tot, a).plus(pc);
		}

	}

	private static final Sprite.SParam sp0 = Sprite.getSprite(Sprite.P_C, 11402, 3, 2);
	private static final Sprite.SParam sp1 = Sprite.getSprite(Sprite.P_C, 11406, 3, 2);
	private static final Sprite.SParam sb0 = Sprite.getDot(10210, 0);
	private static final Sprite.SParam sb1 = Sprite.getDot(10213, 0);

	private static final int n0 = 8, m = 40, xt = 200, vt = 75;
	private static final double dr = 10, ma = p2 / 12;

	private static final double w0 = p2 / 36000, w1 = p2 / 12000, v0 = 0.1;

	private static final int[] n1s = { 6, 8, 10, 12 };
	private static final int[] ws = { 6000, 5500, 5000, 4500 };
	private static final int[] fs = { 400, 360, 320, 280 };

	private final int n1, f;
	private final double w;

	public S005(int diff) {
		super(60000);
		w = ws[diff];
		f = fs[diff];
		n1 = n1s[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		double a0 = it * f * w0;
		for (int i = 0; i < n1; i++) {
			double a1 = a0 + p2 / n1 * i;
			add(new DotBullet(new Dot(pc, 0, a1, v0, w1, sb0)), ex);
			add(new DotBullet(new Dot(pc, 0, -a1, v0, -w1, sb1)), ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			for (int i = 0; i < n0; i++) {
				Curve c0 = new Curve.FuncCurve(new PathFunc(0, p2 / n0 * i, w), m, sp0);
				add(new Laser(c0));
				Curve c1 = new Curve.FuncCurve(new PathFunc(p2 / 2, p2 / n0 * i, w), m, sp1);
				add(new Laser(c1));
			}
			add(new Emiter(0, f, this, this));
		}
		super.update(dt);
	}

}
