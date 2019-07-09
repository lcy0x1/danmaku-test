package stage;

import battle.Sprite;
import battle.bullet.Curve;
import battle.bullet.Laser;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import util.P;

public class TestStage_004 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam sp1 = Sprite.getSprite(Sprite.P_C, 11406, 3, 2);

	private final int f = 40, n = 8, l = 200;
	private final double v0 = 0.2, maxda = p2 / 12, w0 = p2 / 30000, w1 = p2 / 10000;
	private final Curve.ListCurve[] list;

	public TestStage_004(int diff) {
		super(60000);
		list = new Curve.ListCurve[n];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		double a = it * f * w0;
		for (int i = 0; i < n; i++) {
			double a0 = p2 / n * i + a;
			P p = P.polar(l, a0).plus(pc);
			double a1 = it * f * w1;
			double da = maxda * Math.sin(a0 + a1);
			P pv = P.polar(-v0, a0 + da);
			DotBullet d = new DotBullet(new Dot(p, pv, sp1));
			list[i].addP(d);
			add(d, ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			for (int i = 0; i < n; i++) {
				list[i] = new Curve.ListCurve(sp1);
				add(new Laser(list[i]).setLv(K_FINISH));
			}
			add(new Emiter(0, f, this, this));
		}
		super.update(dt);
	}

}
