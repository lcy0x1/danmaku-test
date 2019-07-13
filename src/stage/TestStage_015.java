package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import stage.bullet.Roting;
import util.P;

public class TestStage_015 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_D, 10404, 0, 1);

	private static final double l = 50;

	private static final int[] ns = { 6, 9, 9, 12 };
	private static final int[] ms = { 2, 2, 3, 3 };

	private final int n, m, f;
	private final double v0, w0, w1 = p2 / 23456;

	public TestStage_015(int diff) {
		super(60000);
		n = ns[diff];
		m = ms[diff];

		f = 300;
		v0 = 0.1;
		w0 = v0 / l;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {

		double a0 = e.id * it * f * w1;
		for (int i = 0; i < n; i++) {
			double a1 = a0 + p2 / n * i;
			double a2 = a1 + p2 / n * m * i;
			P pv = P.polar(v0, a1);
			for (int j = 0; j < m; j++)
				add(new DotBullet(new Dot(d0, new Roting(pc, pv, -w0, a2 + p2 / m * j, l))), ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(1, f, this, this));
		if (time == f)
			add(new Emiter(-1, f, this, this));
		super.update(dt);
	}
}
