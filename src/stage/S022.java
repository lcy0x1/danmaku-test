package stage;

import java.util.ArrayList;
import java.util.List;

import battle.Engine;
import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import util.P;

public class S022 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(20401, 0);
	private static final Sprite.SParam d1 = Sprite.getDot(20403, 0);

	private static final int f0 = 3000, f1 = 40, f2 = 20, f3 = 180;
	private static final int t0 = 2000, t1 = 800, t2 = 2200;
	private static final double v0 = 0.2, v1 = 0.3;
	private static final double rad = 600, RAD = 1.2011224;

	private static final P lim0 = new P(100, 100), lim1 = new P(700, 300);

	private static final int[] ns = { 4, 5, 6, 7 };
	private static final int[] ms = { 8, 10, 12, 14 };
	private static final int[] w0s = { 2500, 2700, 2900, 3100 };
	private static final int[] w1s = { 5000, 5500, 6000, 6500 };
	private static final double[] facs = { 0.66, 0.69, 0.72, 0.75 };

	private final int n, m;
	private final double w0, w1, fac;
	private final List<DotBullet> list = new ArrayList<DotBullet>();

	private Emiter prim;
	private P[] ps, ts;
	private P tmpp, ppos, npos;
	private double ta0, tb0;

	public S022(int diff) {
		super(60000, new P(400, 200));
		n = ns[diff];
		m = ms[diff];
		w0 = p2 / w0s[diff];
		w1 = -p2 / w1s[diff];
		fac = facs[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {

			add(new Emiter(1, f1, t0, this));
			add(new Emiter(2, f2, t0 + t1, this).setDelay(t2));
			add(new Emiter(3, f3, t0, this));

		}
		if (e.id == 1) {
			if (it == 0)
				ta0 = rand(p2);
			double a0 = ta0 + it * f1 * w0;
			for (int i = 0; i < n; i++) {
				double a1 = a0 + p2 / n * i;
				DotBullet d = new DotBullet(new Dot(pos.copy(), P.polar(v0, a1), d1));
				add(d);
				list.add(d);
			}
		}
		if (e.id == 3) {
			if (it == 0)
				tb0 = rand(p2);
			double b0 = tb0 + it * f1 * w1;
			for (int i = 0; i < m; i++) {
				double b1 = b0 + p2 / m * i;
				DotBullet d = new DotBullet(new Dot(pos.copy(), P.polar(v1, b1), d0));
				add(d);
				list.add(d);
			}
		}
		if (e.id == 2) {
			if (it == 0) {
				ppos = pos.copy();
				npos = P.polar(rand(200), rand(p2)).plus(pos);
				npos.limit(lim0, lim1);
				tmpp = getPlayer().pos.copy();
				list.removeIf(d -> d.isDead());
				ps = new P[list.size()];
				ts = new P[list.size()];
				for (int i = 0; i < ps.length; i++) {
					ps[i] = list.get(i).dot.pos;
					ts[i] = ps[i].copy();
				}
				Engine.RUNNING.time.slowExc(0, t1, e, prim, this);
			}

			double tim = 1.0 * f2 * it / t1;
			for (int i = 0; i < ps.length; i++)
				ps[i].setTo(move(ts[i], tim));

			pos.setTo(ppos.middleC(npos, tim));
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(prim = new Emiter(0, f0, this, this));
		super.update(dt);
	}

	private P move(P tsi, double tim) {
		double dsi = tsi.dis(tmpp);
		double c0 = Math.pow(dsi / rad / RAD, 2);
		double c1 = fac * tim * Math.pow(Math.E, -c0);
		double c2 = dsi * P.middleC(c1);
		return P.polar(c2, tsi.atan2(tmpp)).plus(tsi);
	}

}
