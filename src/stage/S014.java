package stage;

import battle.Sprite;
import battle.bullet.Curve;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Laser;
import battle.entity.Emiter;
import stage.bullet.Roting;
import util.P;

public class S014 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_SR, 10402, 0, 1);
	private static final Sprite.SParam d1 = Sprite.getSprite(Sprite.P_SR, 10406, 0, 1);

	private static final double l = 50;

	private static final int[] ns = { 30, 38, 46, 54 };
	private static final int[] ms = { 1, 2, 3, 4 };
	private static final double[] vs = { 0.1, 0.11, 0.12, 0.13 };
	private static final int[] ws = { 4000, 3700, 3400, 3100 };
	private static final int[] fs = { 2000, 1800, 1600, 1400 };

	private final int n, m, f;
	private final double v0, w0;

	public S014(int diff) {
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
		Sprite.SParam d = it % 2 == 0 ? d0 : d1;
		double w = (it % 2 * 2 - 1) * w0;
		for (int i = 0; i < n; i++) {
			double a1 = a0 + p2 / n * i;
			double a2 = a1 + p2 / n * m * i / 2;
			P pv = P.polar(v0, a1);
			DotBullet b0 = new DotBullet(new Dot(d, new Roting(pc, pv, w, a2, l)));
			DotBullet b1 = new DotBullet(new Dot(d, new Roting(pc, pv, w, a2 + p2 / 2, l)));
			Curve.ListCurve lc = new Curve.ListCurve(d);
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
