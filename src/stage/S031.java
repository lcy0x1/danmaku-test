package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import util.P;

public class S031 extends SpellCard implements Emiter.Ticker {

	private static class Sub implements Emiter.Ticker {

		private final double a0;
		private final int n;
		private final P pos;

		private Sub(double a, int n0, P p) {
			a0 = a;
			n = n0;
			pos = p;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {
			if (e.id == 1) {
				double a1 = a0 + da * it;
				for (int i = 0; i < n; i++) {
					double a2 = a1 + p2 / n * i;
					Mover mov = new Mover.RefMover(P.polar(v0, a2), ref, 7);
					add(new DotBullet(new Dot(pos.copy(), d0, mov)), ex);
				}
			}
		}

	}

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_D, 20403, 0, 1);

	private static final int f0 = 400, f1 = 100, ref = 1;
	private static final double v0 = 0.3, da = p2 / 128;

	private static final int[] ns = { 3, 4, 5, 6 };
	private static final int[] ms = { 6, 8, 10, 12 };

	private final int n, m;

	public S031(int diff) {
		super(60000, new P(400, 200));
		n = ns[diff];
		m = ms[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			add(new Emiter(1, f1, f1 * m, new Sub(rand(p2), n, pos)));
		}

	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(0, f0, this, this));
		super.update(dt);
	}

}
