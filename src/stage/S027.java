package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import util.P;

public class S027 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(10702, 0);

	private static final int f0 = 60;
	private static final double v0 = 0.2, w0 = p2 / 9876, da = p2 / 24;

	private static final int[] ns = { 3, 4, 5, 6 };
	private static final int[] ms = { 6, 7, 8, 9 };

	private final int n, m;

	public S027(int diff) {
		super(60000);
		n = ns[diff];
		m = ms[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a0 = pos.atan2(getPlayer().pos) + it * f0 * w0;
			for (int i = 0; i < n; i++) {
				double a1 = p2 / n * i + a0;
				for (int j = 0; j < m; j++) {
					P pv = P.polar(v0, a1 + rand(da) - da / 2);
					add(new DotBullet(new Dot(pos.copy(), pv, d0)), ex);
				}
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
