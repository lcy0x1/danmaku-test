package stage;

import battle.Engine;
import battle.Sprite;
import battle.Control;
import battle.entity.Dot;
import battle.entity.DotBullet;
import battle.entity.Emiter;
import util.P;

public class TestStage_002 implements Control.UpdCtrl, Emiter.Ticker {

	private static class SubEmit implements Emiter.Ticker {

		private final P pos, pl;

		private SubEmit(P p, P l) {
			pos = p;
			pl = l;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {
			Sprite.DESParam sp = new Sprite.DESParam(10106, 0, 1);
			double a = pos.atan2(pl);
			for (int i = -1; i <= 1; i++)
				for (int j = 2; j <= 4; j++) {
					Dot d = new Dot(pos.copy(), P.polar(0.1 * j, a + i * Math.PI / 6), sp);
					Engine.RUNNING.add(new DotBullet(d));
				}
		}

	}

	private static final P o = Engine.BOUND;
	private static final int f0 = 8000, f1 = 100, f2 = 20, l0 = 300;

	private int time = 0, dire = 1;

	@Override
	public boolean finished() {
		return false;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			dire = it % 2 * 2 - 1;
			add1(it);
			Engine.RUNNING.time.slow(0.2, 4000);
		} else {
			add0(dire, (dire * it - 2) * Math.PI * 2 / 8);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 1000)
			Engine.RUNNING.add(new Emiter(0, f0, this, this));
		time += dt;

	}

	private void add0(int it, double a0) {
		Sprite.DESParam sp = new Sprite.DESParam(30000, 0, 1);
		double a1 = a0 + Math.PI / 2 * dire;
		P pv = P.polar(1, a1);
		P p = P.polar(400, a0).plus(o.x / 2, o.y / 2).plus(pv, -l0);
		P pl = Engine.RUNNING.pl.pos.copy();
		Dot d = new Dot(p, pv, sp);
		DotBullet b = new DotBullet(d, l0 * 2);
		Engine.RUNNING.add(b);
		Engine.RUNNING.add(new Emiter(2, f2, b.getCtrl(), new SubEmit(p, pl)));
	}

	private void add1(int it) {
		Engine.RUNNING.add(new Emiter(1, f1, f1 * 8, this));
	}

}
