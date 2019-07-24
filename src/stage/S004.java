package stage;

import battle.Sprite;
import battle.bullet.Curve;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Laser;
import battle.entity.Emiter;
import util.P;

public class S004 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam spc = Sprite.getCurve(Sprite.P_C, 11406, 3, 2);
	private static final Sprite.SParam sp0 = Sprite.getDot(10102, 0);
	private static final Sprite.SParam sp1 = Sprite.getDot(10106, 0);

	private static final int f0 = 40, l = 200;
	private static final double maxda = p2 / 12, w0 = p2 / 30000, w1 = p2 / 10000, w2 = p2 / 8000;
	private static final double v0 = 0.2, v1 = 0.25, v2 = 0.15;

	private static final int[] ns = { 4, 5, 6, 7 };
	private static final int[] f1s = { 200, 180, 160, 140 };

	private final int n, f1;
	private final Curve.ListCurve[] list;

	public S004(int diff) {
		super(60000);
		n = ns[diff];
		f1 = f1s[diff];
		list = new Curve.ListCurve[n];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a = it * f0 * w0;
			for (int i = 0; i < n; i++) {
				double a0 = p2 / n * i + a;
				P p = P.polar(l, a0).plus(pc);
				double a1 = it * f0 * w1;
				double da = maxda * Math.sin(a0 + a1);
				P pv = P.polar(-v0, a0 + da);
				DotBullet d = new DotBullet(new Dot(p, pv, spc));
				list[i].addP(d);
				add(d, ex);
			}
		}
		if (e.id == 1) {
			double a = it * f1 * w0;
			for (int i = 0; i < n; i++) {
				double a0 = p2 / n * i + a;
				P p = P.polar(l, a0).plus(pc);
				double a1 = it * f1 * w1;
				double da = maxda * Math.sin(a0 + a1);
				double a2 = a0 + da + it * f1 * w2;
				add(new DotBullet(new Dot(p.copy(), P.polar(v1, a2), sp0)), ex);
				add(new DotBullet(new Dot(p.copy(), P.polar(-v2, a2), sp1)), ex);

			}
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			for (int i = 0; i < n; i++) {
				list[i] = new Curve.ListCurve(spc);
				add(new Laser(list[i]).setLv(K_FINISH));
			}
			add(new Emiter(0, f0, this, this));
			add(new Emiter(1, f1, this, this));
		}
		super.update(dt);
	}

}
