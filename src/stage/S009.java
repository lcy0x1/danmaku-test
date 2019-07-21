package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import util.P;

public class S009 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam sp0 = Sprite.getSprite(Sprite.P_D, 10102, 0, 1);
	private static final Sprite.SParam sp1 = Sprite.getSprite(Sprite.P_D, 10106, 0, 1);
	private static final Sprite.SParam st0 = Sprite.getSprite(Sprite.P_D, 20101, 0, 1);
	private static final Sprite.SParam st1 = Sprite.getSprite(Sprite.P_D, 20103, 0, 1);
	private static final Sprite.SParam[] sps = { sp0, sp1 };
	private static final Sprite.SParam[] sts = { st0, st1 };
	private static final int l = 200, m = 5, df = 500;
	private static final double v0 = 0.3;
	private static final int[] ns = { 14, 21, 28, 35 };
	private static final int[] fs = { 2400, 2100, 1800, 1500 };
	private final int n, f0;

	private double a0;

	public S009(int diff) {
		super(60000);
		n = ns[diff];
		f0 = fs[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			a0 = rand(p2);
			for (int i = 0; i < 5; i++) {
				P p0 = P.polar(l, a0 + p2 / 5 * i);
				P p1 = P.polar(l, a0 + p2 / 5 * (i + 2));
				for (int j = 0; j < n; j++) {
					P pv = p0.middle(p1, 1.0 / n * j).times(1.0 / df);
					add(new DotBullet(new Dot(pc.copy(), pv, sts[it % 2]), df + 20).setLv(K_FUNCTIONAL), ex);

				}
			}
		}
		if (e.id == 1) {
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
		if (time == 0)
			add(new Emiter(0, f0, this, this));
		if (time == df)
			add(new Emiter(1, f0, this, this));
		super.update(dt);
	}

}
