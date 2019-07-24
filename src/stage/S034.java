package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import util.P;

public class S034 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(10702, 0);

	private static final double rad = 100, dr = 50;

	private static final int[] ns = { 12, 14, 16, 18 };
	private static final int[] fs = { 360, 320, 280, 240 };
	private static final double[] vs = { 0.2, 0.22, 0.24, 0.26 };

	private final int n, f0;
	private final double v0;

	public S034(int diff) {
		super(60000, new P(400, 200));
		n = ns[diff];
		f0 = fs[diff];
		v0 = vs[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			P p = getPlayer().pos;
			double x0 = p.x + v0 * it * f0;
			double y0 = p.y + v0 * it * f0;
			for (int i = 0; i < n; i++) {
				double x1 = (x0 + o.x / n * i) % o.x;
				double y1 = (y0 + o.y / n * i) % o.y;
				add(new DotBullet(new Dot(new P(x1, y1 - o.y), new P(0, v0), d0)), ex);
				if (new P(x1, y1 + dr).dis(p) < rad)
					continue;
				add(new DotBullet(new Dot(new P(x1, y1), new P(0, v0), d0)), ex);

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
