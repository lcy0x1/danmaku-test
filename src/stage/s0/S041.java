package stage.s0;

import battle.Sprite;
import battle.bullet.Func;
import battle.bullet.BulletRing;
import battle.bullet.Mover.FuncMover;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S041 extends SpellCard implements Emiter.Ticker {

	public static class Adder implements Emiter.Ticker {

		private final P pos;
		private final Func vlf;
		private final double a;
		private final Sprite.SParam sp;
		private final int n;

		public Adder(P p, Func vlfs, double a0, Sprite.SParam spr, int t) {
			pos = p;
			vlf = vlfs;
			a = a0;
			sp = spr;
			n = t;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {
			add(new BulletRing(pos, sp, n, new FuncMover(vlf, 0, it, a)), ex);
		}

	}

	private static class TimeFunc implements Func {

		private static final double w0 = p2 / 4000, tx = 1000;

		private final double x;

		private TimeFunc(double a) {
			x = a;
		}

		@Override
		public boolean exist(P pos, double r, int time, int i) {
			return !pos.out(o, r);
		}

		@Override
		public P func(int time, int i) {
			double dx = x * (1 - pow(E, -time / tx));
			return new P(dx * sin((time + i * f1) * w0), time * v0);
		}

	}

	private static final Sprite.SParam dl0 = Sprite.getDot(10404, 0, 1);
	private static final Sprite.SParam dl1 = Sprite.getDot(10410, 0, 1);
	private static final Sprite.SParam[] dls = { dl0, dl1 };

	private static final int f0 = 500, f1 = 100;
	private static final int m0 = 5, ra = 50;
	private static final double v0 = 0.2;

	private static final int[] ns = { 3, 4, 5, 6 };

	private Func vlfs[] = new Func[2];

	private final int n0;

	public S041(int diff) {
		super(60000, new P(400, 200));
		n0 = ns[diff] * 12;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id / 2 == 2) {
			int x = e.id % 2;
			double a0 = rand(p2);
			for (int j = 0; j < 2; j++) {
				Adder ad = new Adder(pos, vlfs[j], a0, dls[x], n0);
				add(new Emiter(0, f1, f1 * m0, ad), ex);
			}
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			vlfs[0] = new TimeFunc(ra);
			vlfs[1] = new TimeFunc(-ra);
			add(new Emiter(4, f0 * 2, this, this).setDelay(f0));
			add(new Emiter(5, f0 * 2, this, this));
		}
		super.update(dt);
	}

}
