package stage.s0;

import battle.Engine;
import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S002 extends SpellCard implements Emiter.Ticker {

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

			double da = n == 3 ? p2 / d0s[diff] : p2 / d1s[diff];
			double a = pos.atan2(pl);
			for (int i = 0; i < n; i++)
				for (int j = 2; j <= 4; j++) {
					P pv = P.polar(0.1 * j, a + (i - (n - 1) / 2) * da);
					Dot d = new Dot(pos.copy(), pv, n == 2 ? spr : sp0);
					add(new DotBullet(d), ex);
				}
		}

	}

	private static final int f0 = 8000, f1 = 100, f2 = 20, l0 = 400;
	private static final double[] sls = { 0.2, 0.22, 0.25, 0.29 };
	private static final int[] d0s = { 8, 10, 12, 14 };
	private static final int[] d1s = { 24, 32, 40, 48 };
	private static final Sprite.SParam spr = Sprite.getDot(10102, 0);
	private static final Sprite.SParam sp0 = Sprite.getDot(10106, 0);

	private static final Sprite.SParam sp2 = Sprite.getDot(30000, 1, 1);
	private static final Sprite.SParam sp3 = Sprite.getDot(30001, 1, 1);
	private int dire = 1;

	private final int diff;

	public S002(int df) {
		super(50000);
		diff = df;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			dire = it % 2 * 2 - 1;
			add(new Emiter(1, f1, f1 * 8, this), ex);
			Engine.RUNNING.time.slowExc(sls[diff], (int) (f1 * 8 / sls[diff]), getPlayer());
		} else if (e.id == 1)
			adds(dire, (dire * it - 2) * PI * 2 / 8, 3, ex);
		else if (e.id == 3)
			add(new Emiter(4, f1, f1 * 8, this), ex);
		else if (e.id == 4)
			adds(dire, (dire * it - 2) * PI * 2 / 8, 2, ex);
	}

	@Override
	public void update(int dt) {
		if (time == 6000)
			add(new Emiter(0, f0, this, this));
		if (time == 1000)
			add(new Emiter(3, f0, this, this));
		super.update(dt);
	}

	private void adds(int it, double a0, int x, int ex) {
		double a1 = a0 + p2 / 4 * dire;
		P pv = P.polar(1, a1);
		P p = P.polar(400, a0).plus(pc).plus(pv, -l0);
		P pl = Engine.RUNNING.pl.pos.copy();
		Dot d = new Dot(p, pv, x == 2 ? sp2 : sp3);
		add(new DotBullet(d, l0 * 3).setLv(K_FUNCTIONAL), ex);
		add(new Emiter(2, f2, l0 * 2, new SubEmit(p, pl, x, diff)), ex);
	}

}
