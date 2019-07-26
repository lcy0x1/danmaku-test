package stage.s0;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S031 extends SpellCard implements Emiter.Ticker {

	private static class Sub implements Emiter.Ticker {

		private final Sprite.SParam spe;
		private final double a0, da, v0;
		private final int n, ref;
		private final P pos;

		private Sub(double a, int n0, P p, double v, double d, int r, Sprite.SParam sp) {
			a0 = a;
			n = n0;
			pos = p;
			ref = r;
			da = d;
			spe = sp;
			v0 = v;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {

			double a1 = a0 + da * it;
			for (int i = 0; i < n; i++) {
				double a2 = a1 + p2 / n * i;
				Mover mov = new Mover.RefMover(P.polar(v0, a2), ref, 7);
				add(new DotBullet(new Dot(pos.copy(), spe, mov)), ex);
			}

		}

	}

	private static final Sprite.SParam d0 = Sprite.getDot(20403, 0);
	private static final Sprite.SParam d1 = Sprite.getDot(20401, 0);

	private static final int f0 = 400, f1 = 100, ref = 2;
	private static final double v0 = 0.4, v1 = 0.2, da = p2 / 256;

	private static final int[] ns = { 3, 4, 4, 5 };
	private static final int[] ms = { 3, 3, 4, 4 };

	private final int n, m;

	public S031(int diff) {
		super(60000, new P(400, 200));
		n = ns[diff];
		m = ms[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			add(new Emiter(1, f1, f1 * m, new Sub(rand(p2), n, pos, v0, da, ref, d0)));
			add(new Emiter(2, f1, f1 * m * 2, new Sub(rand(p2), n, pos, v1, 0, 0, d1)));
		}

	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(0, f0, this, this));
		super.update(dt);
	}

}
