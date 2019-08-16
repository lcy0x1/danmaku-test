package stage.s0;

import battle.Entity;
import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S033 extends SpellCard implements Emiter.Ticker {

	private static class Sub implements Emiter.Ticker {

		private final S033 parent;
		private final P pos;
		private final int lv;

		private Sub(S033 par, P p, int v) {
			parent = par;
			pos = p;
			lv = v;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {
			for (Entity ent : parent.adder(lv, pos))
				add(ent, ex);
		}

	}

	private static final Sprite.SParam d00 = Sprite.getDot(30000, 1);
	private static final Sprite.SParam d01 = Sprite.getDot(20201, 0);
	private static final Sprite.SParam d02 = Sprite.getDot(10302, 0);
	private static final Sprite.SParam d03 = Sprite.getDot(11302, 0);

	private static final Sprite.SParam d10 = Sprite.getDot(30001, 1);
	private static final Sprite.SParam d11 = Sprite.getDot(20203, 0);
	private static final Sprite.SParam d12 = Sprite.getDot(10306, 0);
	private static final Sprite.SParam d13 = Sprite.getDot(11306, 0);

	private static final Sprite.SParam[] d0s = { d00, d01, d02, d03 };
	private static final Sprite.SParam[] d1s = { d10, d11, d12, d13 };

	private static final int f0 = 15000, m = 4, t0 = 1000, t1 = 200, t2 = 60;

	private static final int[] ns = { 6, 8, 10, 12 };
	private static final double[] vs = { 1, 1.05, 1.1, 1.15 };

	private final int n;
	private final double v0, v1, v2;

	public S033(int diff) {
		super(60000, new P(400, 400));
		n = ns[diff];
		v0 = 0.25 * vs[diff];
		v1 = 0.2 * vs[diff];
		v2 = 0.4 * vs[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			for (Entity ent : adder(0, pos))
				add(ent, ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(0, f0, this, this));
		super.update(dt);
	}

	private Entity[] adder(int lv, P p0) {
		Entity[] ans = new Entity[n];
		double a0 = rand(p2);
		for (int i = 0; i < n; i++) {
			double a1 = a0 + p2 / n * i;
			if (lv + 1 == m) {
				ans[i] = new DotBullet(new Dot(p0.copy(), P.polar(v1, a1), d0s[lv]));
				continue;
			}
			P pv = P.polar(v0 + 1e-5, a1);
			P p1 = p0.copy().plus(pv, t0 / 2);
			Dot dot = new Dot(p0.copy(), pv, -v0 / t0, t0, i % 2 == 0 ? d0s[lv] : d1s[lv]);
			ans[i] = new DotBullet(dot, t0 + (t1 + t2 * lv) * i);
			if (i % 2 == 0)
				ans[i].trail(adder(lv + 1, p1));
			else {
				DotBullet b0 = new DotBullet(new Dot(dot.pos, d1s[lv], new Mover.HomingLM(v2)));
				if (lv + 3 < m)
					b0.trail(new Emiter(0, new Sub(this, dot.pos, lv + 1)));
				ans[i].trail(b0);
			}
		}
		return ans;
	}

}
