package stage.s0;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S020 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(10202, 0);

	private static final int n = 20;

	private static final int[] fs = { 5000, 4700, 4400, 4100 };
	private static final int[] ls = { 250, 260, 270, 280 };

	private final int f0, l0;
	private final double v0, v1;

	public S020(int diff) {
		super(60000);
		f0 = fs[diff];
		l0 = ls[diff];
		v0 = 500.0 / f0;
		v1 = v0 * 2;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			adds(new P(-350, 250), new P(v0, 0), ex);
			adds(new P(1150, 750), new P(-v0, 0), ex);
			adds(new P(150, -500), new P(0, v0), ex);
			adds(new P(650, 1500), new P(0, -v0), ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(0, f0, this, this));
		super.update(dt);
	}

	private void adds(P cen, P pv, int ex) {
		for (int i = 0; i < 4; i++) {
			P p0 = P.polar(l0, p2 / 4 * i);
			P p1 = P.polar(l0, p2 / 4 * (i + 1));
			for (int j = 0; j < n; j++) {
				double d = 1.0 / n * j;
				P b0 = p0.middle(p1, d).plus(cen);
				if (b0.moveOut(pv, o, d0.getRadius()))
					continue;
				P dv = P.polar(v1, rand(p2));
				DotBullet tr = new DotBullet(new Dot(b0, dv, d0));
				add(new DotBullet(new Dot(b0, pv, d0)).trail(tr), ex);
			}
		}

	}

}
