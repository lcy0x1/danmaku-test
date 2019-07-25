package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import battle.special.InRange;
import util.P;

public class S034 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(11505, 0);
	private static final Sprite.SParam d1 = Sprite.getDot(11506, 0);
	private static final Sprite.SParam d2 = Sprite.getDot(11507, 0);
	private static final Sprite.SParam d3 = Sprite.getDot(11508, 0);
	private static final Sprite.SParam d4 = Sprite.getDot(10405, 0);
	private static final Sprite.SParam d5 = Sprite.getDot(10406, 0);
	private static final Sprite.SParam d6 = Sprite.getDot(10407, 0);
	private static final Sprite.SParam d7 = Sprite.getDot(10408, 0);

	private static final Sprite.SParam ds0 = Sprite.getDot(10305, 0);
	private static final Sprite.SParam ds1 = Sprite.getDot(10306, 0);
	private static final Sprite.SParam ds2 = Sprite.getDot(10307, 0);
	private static final Sprite.SParam ds3 = Sprite.getDot(10308, 0);
	private static final Sprite.SParam ds4 = Sprite.getDot(10205, 0);
	private static final Sprite.SParam ds5 = Sprite.getDot(10206, 0);
	private static final Sprite.SParam ds6 = Sprite.getDot(10207, 0);
	private static final Sprite.SParam ds7 = Sprite.getDot(10208, 0);

	private static final Sprite.SParam[] ds = { d0, d1, d2, d3, d4, d5, d6, d7 };
	private static final Sprite.SParam[] dss = { ds0, ds1, ds2, ds3, ds4, ds5, ds6, ds7 };

	private static final int t0 = 3000;
	private static final double rad = 150, da = p2 / 24, va = 4e-5, dva = 2e-7, dx = 50;

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
			int s = it % 2 * 2 - 1;
			double x0 = p.x + v0 * it * f0;
			double y0 = p.y + v0 * it * f0;
			for (int i = 0; i < n; i++) {
				double x1 = (x0 + s * (o.x + dx * 2) / n * i) % (o.x + dx * 2) - dx;
				double y1 = (y0 + o.y / n * i) % o.y;
				int x = (int) rand(8);
				P pv = P.polar(v0, p2 / 4 + rand(da / 2) - da / 2);
				P pa = new P(0, va + dva * Math.abs(o.x / 2 - x1));
				Dot dt0 = new Dot(new P(x1, y1 - o.y), new InRange(ds[x], dss[x], rad));
				add(new DotBullet(dt0.setMove(pv, pa, 0, t0)), ex);
				if (new P(x1, y1).dis(p) < rad)
					continue;
				Dot dt1 = new Dot(new P(x1, y1), new InRange(ds[x], dss[x], rad));
				add(new DotBullet(dt1.setMove(pv, pa, 0, t0)), ex);

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
