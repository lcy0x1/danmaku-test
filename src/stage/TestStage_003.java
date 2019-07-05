package stage;

import battle.Engine;
import battle.Sprite;
import battle.Control;
import battle.entity.Dot;
import battle.entity.DotBullet;
import battle.entity.Emiter;
import battle.special.Koishi;
import util.P;

public class TestStage_003 implements Control.UpdCtrl, Emiter.Ticker {

	private static final P o = Engine.BOUND;
	private int time = 0;

	@Override
	public boolean finished() {
		return time > 50000;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		int f = 120, n = 10, m = 3;
		int f1 = 180;
		double v0 = 0.1, w1 = Math.PI * 2 / n / f / 20;
		double a1 = it * f * w1;
		P p = new P(o.x / 2, o.y / 2);
		Sprite.DESParam sp0 = new Sprite.DESParam(10302, 0, 1);
		Sprite.DESParam sp1 = new Sprite.DESParam(30100, 0, 1);
		for (int i = 0; i < n; i++) {
			double a0 = Math.PI * 2 / n * i + a1 + Math.PI * 2 / n / m * (it % m);
			int xt = (it % m) * (f1 * 2 + f) + it / m * f1;
			Dot d = new Dot(p.copy(), P.polar(v0, a0), sp0);
			DotBullet db = new DotBullet(d);
			Koishi k = new Koishi(db, sp1, 8 * f1, xt, 7 * f1);
			Engine.RUNNING.add(db);
			Engine.RUNNING.add(k);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			Engine.RUNNING.add(new Emiter(0, 120, this, this));
		time += dt;

	}

}
