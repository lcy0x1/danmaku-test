package stage.s0;

import java.util.ArrayList;
import java.util.List;

import battle.Engine;
import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S023 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(20401, 0);
	private static final Sprite.SParam d1 = Sprite.getDot(20403, 0);
	private static final Sprite.SParam d2 = Sprite.getDot(20400, 0);

	private static final int f0 = 3000, f2 = 20, f4 = 200;
	private static final int t0 = 2000, t1 = 800, t2 = 2200, t4 = 1000;
	private static final double v1 = 0.2, v2 = 0.8, dv = 1e-5, v3 = 0.4, v4 = 2;
	private static final P lim0 = new P(100, 100), lim1 = new P(700, 300);

	private static final int[] ns = { 4, 5, 6, 7 };
	private static final int[] ms = { 8, 10, 12, 14 };

	private final int n, m;
	private final List<DotBullet> home = new ArrayList<DotBullet>();

	private Emiter prim;
	private P ppos, npos;

	public S023(int diff) {
		super(60000, new P(400, 200));
		n = ns[diff];
		m = ms[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			for (DotBullet db : home)
				db.getEntCtrl().killed(K_FUNCTIONAL);
			home.clear();

			add(new Emiter(2, f2, t0 + t1, this).setDelay(t2));
			add(new Emiter(4, f4, t4, this));

		}

		if (e.id == 2) {
			if (it == 0) {
				ppos = pos.copy();
				npos = P.polar(rand(200), rand(p2)).plus(pos);
				npos.limit(lim0, lim1);

				Engine.RUNNING.time.slowExc(0, t1, e, prim, this);
			}

			double tim = 1.0 * f2 * it / t1;

			for (DotBullet db : home) {
				double a0 = db.dot.getDire();
				P pv = P.polar(v3, a0);
				P p0 = P.polar(v4 * it * f2, a0).plus(db.dot.pos);
				add(new DotBullet(new Dot(p0, pv, d2)));
			}

			pos.setTo(ppos.middleC(npos, tim));
		}
		if (e.id == 4) {
			double a0 = pos.atan2(getPlayer().pos);
			for (int i = 0; i < n; i++) {
				double a1 = a0 + p2 / n * i;
				P p0 = pos.copy();
				P pv = P.polar(v2, a1);
				DotBullet b0 = new DotBullet(new Dot(p0, pv, d0));
				DotBullet b1 = new DotBullet(new Dot(p0, d2, new Mover.HomingLM(dv)));
				home.add(b1);
				add(b0.trail(b1.setLv(K_FUNCTIONAL)).setLv(K_FUNCTIONAL), ex);
			}
			a0 = rand(p2);
			for (int i = 0; i < m; i++) {
				double a1 = a0 + p2 / m * i;
				DotBullet b0 = new DotBullet(new Dot(pos.copy(), P.polar(v1, a1), d1));
				add(b0.setLv(K_FUNCTIONAL), ex);
				home.add(b0);
			}
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(prim = new Emiter(0, f0, this, this));
		super.update(dt);
	}

}
