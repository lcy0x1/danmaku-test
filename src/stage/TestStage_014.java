package stage;

import battle.Sprite;
import battle.bullet.Curve;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Laser;
import battle.entity.Emiter;
import stage.bullet.Roting;
import util.P;

public class TestStage_014 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_SR, 10402, 0, 1);

	private static final double l = 50;

	private static final int[] ns = { 30, 40, 50, 60 };
	private static final int[] ms = { 3, 4, 5, 6 };
	private static final double[] vs = { 0.1, 0.1, 0.1, 0.1 };
	private static final int[] ws = { 3000, 2800, 2600, 2400 };
	private static final int[] fs = { 2000, 1800, 1600, 1400 };

	private final int n, m, f;
	private final double v0, w0;

	public TestStage_014(int diff) {
		super(60000);
		n = ns[diff];
		m = ms[diff];
		v0 = vs[diff];
		w0 = p2 / ws[diff];
		f = fs[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		double a0 = rand(p2);
		for (int i = 0; i < n; i++) {
			double a1 = a0 + p2 / n * i;
			double a2 = a1 + p2 / n * m * i / 2;
			P pv = P.polar(v0, a1);
			DotBullet b0 = new DotBullet(new Dot(d0, new Roting(pc, pv, w0, a2, l)));
			DotBullet b1 = new DotBullet(new Dot(d0, new Roting(pc, pv, w0, a2 + p2 / 2, l)));
			Curve.ListCurve lc = new Curve.ListCurve(d0);
			lc.addP(b0);
			lc.addP(b1);
			add(new Laser(lc), ex);
			add(b0, ex);
			add(b1, ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			add(new Emiter(0, f, this, this));
		}
		super.update(dt);
	}

}
