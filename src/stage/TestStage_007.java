package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import util.P;

public class TestStage_007 extends SpellCard implements Emiter.Ticker {

	private static class Adder implements Emiter.Ticker {

		private static class Trail implements Emiter.Ticker {

			private final Dot d;
			private final P pv;

			private Trail(Dot dot, P vp) {
				d = dot;
				pv = vp;
			}

			@Override
			public void tick(Emiter e, int it, int ex) {
				pv.rotate(rand(da) - da / 2);
				d.setMove(new Mover.LineMover(pv));
			}

		}

		private final double a, v;
		private final int dt;

		private Adder(double a0, double v0, int t) {
			a = a0;
			dt = t;
			v = v0;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {
			P pv = P.polar(v, a);
			Mover mov = new Mover.RefMover(pv, -1);
			Dot d = new Dot(pc.copy(), e.id > 0 ? sp0 : sp1, mov);
			DotBullet b = new DotBullet(d);
			add(new Emiter(lt - dt, new Trail(d, pv)));
			add(b, ex);
		}

	}

	private static final Sprite.SParam sp0 = Sprite.getSprite(Sprite.P_D, 10402, 0, 1);
	private static final Sprite.SParam sp1 = Sprite.getSprite(Sprite.P_D, 10406, 0, 1);
	private static final int f0 = 3000, f1 = 20, f2 = 40, lt = 4500;
	private static final double da = p2 / 24;

	private static final int[] ns = { 25, 30, 35, 40 };
	private static final int[] ms = { 14, 16, 18, 20 };
	private static final double[] vs = { 0.24, 0.26, 0.28, 0.30 };

	private final int n, m;
	private final double v;

	private double a;

	public TestStage_007(int diff) {
		super(60000);
		n = ns[diff];
		m = ms[diff];
		v = vs[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id != 0) {
			double a0 = a + e.id * it * p2 / n;
			add(new Emiter(e.id, f2, f2 * m, new Adder(a0, v, it * f1)), ex);
		} else {
			a = rand(p2);
			add(new Emiter(it % 2 * 2 - 1, f1, f1 * n, this));
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			add(new Emiter(0, f0, this, this));
		}
		super.update(dt);
	}

}
