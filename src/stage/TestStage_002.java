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
		private final double n;
		private final int diff;

		private SubEmit(P p, P l, int x, int df) {
			pos = p;
			pl = l;
			n = x;
			diff = df;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {

			double da = n == 3 ? Math.PI / d0s[diff] : Math.PI / d1s[diff];
			double a = pos.atan2(pl);
			for (int i = 0; i < n; i++)
				for (int j = 2; j <= 4; j++) {
					Sprite.DESParam sp = n == 2 ? spr : sp0;
					Dot d = new Dot(pos.copy(), P.polar(0.1 * j, a + (i - (n - 1) / 2) * da), sp);
					Engine.RUNNING.add(new DotBullet(d));
				}
		}

	}

	private static final P o = Engine.BOUND;
	private static final int f0 = 8000, f1 = 100, f2 = 20, l0 = 400;
	private static final double[] sls = { 0.2, 0.22, 0.25, 0.29 };
	private static final int[] d0s = { 4, 5, 6, 7 };
	private static final int[] d1s = { 12, 16, 20, 24 };
	private static final Sprite.DESParam spr = new Sprite.DESParam(10102, 0, 1);
	private static final Sprite.DESParam sp0 = new Sprite.DESParam(10106, 0, 1);
	private int time = 0, dire = 1;

	private final int diff;

	public TestStage_002(int df) {
		diff = df;
	}

	@Override
	public boolean finished() {
		return time > 50000;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			dire = it % 2 * 2 - 1;
			Engine.RUNNING.add(new Emiter(1, f1, f1 * 8, this));
			Engine.RUNNING.time.slow(sls[diff], (int) (f1 * 8 / sls[diff]), null);
		} else if (e.id == 1)
			adds(dire, (dire * it - 2) * Math.PI * 2 / 8, 3);
		else if (e.id == 3)
			Engine.RUNNING.add(new Emiter(4, f1, f1 * 8, this));
		else if (e.id == 4)
			adds(dire, (dire * it - 2) * Math.PI * 2 / 8, 2);
	}

	@Override
	public void update(int dt) {
		if (time == 6000)
			Engine.RUNNING.add(new Emiter(0, f0, this, this));
		if (time == 1000)
			Engine.RUNNING.add(new Emiter(3, f0, this, this));
		time += dt;

	}

	private void adds(int it, double a0, int x) {
		Sprite.DESParam sp = new Sprite.DESParam(x == 2 ? 30000 : 30001, 0, 1);
		double a1 = a0 + Math.PI / 2 * dire;
		P pv = P.polar(1, a1);
		P p = P.polar(400, a0).plus(o.x / 2, o.y / 2).plus(pv, -l0);
		P pl = Engine.RUNNING.pl.pos.copy();
		Dot d = new Dot(p, pv, sp);
		Engine.RUNNING.add(new DotBullet(d, l0 * 3));
		Engine.RUNNING.add(new Emiter(2, f2, l0 * 2, new SubEmit(p, pl, x, diff)));
	}

}
