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

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_D, 20401, 0, 1);
	private static final Sprite.SParam d1 = Sprite.getSprite(Sprite.P_D, 20403, 0, 1);

	private static final int f0 = 3000, t0 = 2000, t2 = 2200, t1 = 800, f1 = 60, f3 = 240, f2 = 20, t3 = 6000;
	private static final double v0 = 0.2, v1 = 0.3;
	private static final double rad = 600, RAD = 1.2011224;

	private static final int[] ns = { 4, 5, 6, 7 };
	private static final int[] ms = { 8, 10, 12, 14 };
	private static final int[] w0s = { 2500, 2700, 2900, 3100 };
	private static final int[] w1s = { 5000, 5500, 6000, 6500 };
	private static final double[] facs = { 0.55, 0.6, 0.65, 0.7 };

	private final int n, m;
	private final double w0, w1, fac;
	private final List<DotBullet> list = new ArrayList<DotBullet>();

	private double[] ds;
	private P[] ps, ts;
	private P tmpp;
	private double ta0, tb0;

	public S022(int diff) {
		super(60000);
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
			add(new Emiter(3, f3, t0, this));
			add(new Emiter(2, f2, t0 + t1, this).setDelay(t2));
		}
		if (e.id == 1) {
			if (it == 0)
				ta0 = rand(p2);
			double a0 = ta0 + it * f1 * w0;
			for (int i = 0; i < n; i++) {
				double a1 = a0 + p2 / n * i;
				DotBullet d = new DotBullet(new Dot(pc.copy(), P.polar(v0, a1), d1), t3);
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
				DotBullet d = new DotBullet(new Dot(pc.copy(), P.polar(v1, b1), d0), t3);
				add(d);
				list.add(d);
			}
		}
		if (e.id == 2) {
			if (it == 0) {
				tmpp = getPlayer().pos.copy();
				list.removeIf(d -> d.isDead());
				ds = new double[list.size()];
				ps = new P[list.size()];
				ts = new P[list.size()];
				for (int i = 0; i < ps.length; i++) {
					ps[i] = list.get(i).dot.pos;
					ts[i] = ps[i].copy();
					ds[i] = ps[i].dis(tmpp);
				}
				Engine.RUNNING.time.slowInc(0, t1, null);
			}
			for (int i = 0; i < ps.length; i++) {
				double c0 = Math.pow(ds[i] / rad / RAD, 2);
				double c1 = fac / t1 * f2 * it * Math.pow(Math.E, -c0);
				double c2 = ds[i] * P.middleC(c1);
				ps[i].setTo(P.polar(c2, ts[i].atan2(tmpp)).plus(ts[i]));
			}
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(0, f0, this, this));
		super.update(dt);
	}

}
