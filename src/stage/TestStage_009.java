package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import util.P;

public class TestStage_009 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam sp0 = new Sprite.SParam(10102, 0, 1);
	private static final Sprite.SParam sp1 = new Sprite.SParam(10106, 0, 1);
	private static final Sprite.SParam[] sps = { sp0, sp1 };
	private static final int l = 200, m = 5;
	private static final double v0 = 0.3;
	private static final int[] ns = { 15, 25, 30, 35 };
	private static final int[] fs = { 2000, 2000, 1800, 1500 };
	private final int n, f0;

	public TestStage_009(int diff) {
		super(60000);
		n = ns[diff];
		f0 = fs[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a0 = rand(p2);
			for (int i = 0; i < 5; i++) {
				P p0 = P.polar(l, a0 + p2 / 5 * i);
				P p1 = P.polar(l, a0 + p2 / 5 * (i + 2));
				for (int j = 0; j < n; j++) {
					P pos = p0.middle(p1, 1.0 / n * j).plus(pc);
					double a1 = pc.atan2(pos);
					double dx = pc.dis(pos);
					for (int k = 0; k < m; k++) {
						P pv = P.polar(v0 / l * dx, a1 + p2 / m * k);
						add(new DotBullet(new Dot(pos.copy(), pv, sps[it % 2])), ex);
					}
				}
			}
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			add(new Emiter(0, f0, this, this));
		}
		super.update(dt);
	}

}
