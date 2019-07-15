package stage;

import java.util.ArrayList;
import java.util.List;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import util.P;

public class TestStage_019 extends SpellCard implements Emiter.Ticker {

	private static class CosCurve extends Mover.TimeMover {

		private final P cen;
		private final double r, a, w, t0;

		private CosCurve(P p, double r0, double a0, double w0, double dt) {
			cen = p;
			r = r0;
			a = a0;
			w = w0;
			t0 = dt;
		}

		@Override
		public P disp(int t) {
			return P.polar(r, a + w * t0 * P.middleC(1.0 * t / t0)).plus(cen);
		}

		@Override
		public double getDire() {
			return time * (p2 / t0 - p2 / 6000);
		}

		@Override
		public boolean out(P pos, double r) {
			return false;
		}

	}

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_D, 11003, 0, 1);
	private static final Sprite.SParam d1 = Sprite.getSprite(Sprite.P_D, 11013, 0, 1);
	private static final Sprite.SParam d2 = Sprite.getSprite(Sprite.P_D, 11010, 0, 1);
	private static final Sprite.SParam d3 = Sprite.getSprite(Sprite.P_D, 11006, 0, 1);

	private static final int f = 20, l = 250;

	private static final int[] ns = { 16, 20, 24, 28 };
	private static final int[] ts = { 6000, 5200, 4400, 3600 };

	private final P[][] ps = new P[5][5];
	private final int n, tx;

	private final List<P> lp = new ArrayList<P>();
	private final List<DotBullet> lb = new ArrayList<DotBullet>();

	public TestStage_019(int diff) {
		super(60000);
		n = ns[diff];
		tx = ts[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			int bt = 3 * n * f;
			if (it < n * 6)
				for (int i = 0; i < 5; i++) {
					int ai = it / n;
					int at = it % n;
					if (ai == 1) {
						P p0 = ps[i][3].middle(ps[i][2], 1.0 / n * (at + 0.5));
						lp.add(p0);
						P pv = P.polar(ps[i][0].dis(p0) * 2 * Math.cos(p2 / 5) / bt, ps[i][0].atan2(p0));
						Dot b0 = new Dot(p0, d2).delay(bt);
						Dot b1 = new Dot(p0, d3);
						Dot b2 = new Dot(p0, pv, d3).delay(bt);
						DotBullet o0 = new DotBullet(b0);
						DotBullet o1 = new DotBullet(b1, 2 * n * f);
						DotBullet o2 = new DotBullet(b2, bt * 2);
						lb.add(o0);
						add(o2.trail(o1.trail(o0.setLv(K_FUNCTIONAL)).setLv(K_FUNCTIONAL)).setLv(K_FUNCTIONAL), ex);
					} else if (ai < 3) {
						int aj = ai == 0 ? 0 : 3;
						P p0 = ps[i][aj].middle(ps[i][(aj + 2) % 5], 1.0 / n * (at + 0.5));
						lp.add(p0);
						Dot b0 = new Dot(p0, d2).delay(bt);
						Dot b1 = new Dot(p0, d0);
						DotBullet o0 = new DotBullet(b0);
						DotBullet o1 = new DotBullet(b1, bt + (6 - ai) * n * f);
						lb.add(o0);
						add(o1.trail(o0.setLv(K_FUNCTIONAL)).setLv(K_FUNCTIONAL), ex);
					} else {
						int aj = ai * 2 - 4;
						P p1 = ps[i][aj % 5].middle(ps[i][(aj + 2) % 5], 1.0 / n * (at + 0.5));
						lp.add(p1);
						Dot b0 = new Dot(p1, d2).delay(bt);
						Dot b1 = new Dot(p1, d1);
						DotBullet o0 = new DotBullet(b0);
						DotBullet o1 = new DotBullet(b1, bt + (6 - ai) * n * f);
						lb.add(o0);
						add(o1.trail(o0.setLv(K_FUNCTIONAL)).setLv(K_FUNCTIONAL), ex);
					}
				}
		}

		if (e.id == 1)
			reset(ex);
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			for (int i = 0; i < 5; i++) {
				double a = p2 / 5 * i + p2 / 4;
				P p0 = P.polar(l, a + p2 / 5).plus(P.polar(l, a)).plus(pc);
				for (int j = 0; j < 5; j++)
					ps[i][j] = P.polar(l, a + p2 / 10 + p2 / 5 * j).plus(p0);
			}
			add(new Emiter(0, f, f * n * 12, this));
			add(new Emiter(1, tx, this, this).setDelay(f * n * 12 + 500));
		}
		super.update(dt);
	}

	private void reset(int ex) {
		for (DotBullet b : lb)
			b.getEntCtrl().killed(K_FINISH);
		List<P> t0 = new ArrayList<P>();
		List<P> t1 = new ArrayList<P>();
		t0.addAll(lp);
		double a = rand(p2);
		for (P p : lp)
			t1.add(P.polar(pc.dis(p), pc.atan2(p) + a).plus(pc));
		lp.clear();
		lp.addAll(t1);
		for (int i = 0; i < lp.size(); i++) {
			double dis = 0;
			P p0 = null, p1 = null;
			int count = 0;
			while (dis <= 400 || dis >= 600) {
				if (count > 200)
					break;
				p0 = t0.get((int) (Math.random() * t0.size()));
				p1 = t1.get((int) (Math.random() * t1.size()));
				dis = p0.dis(p1);
				count++;
			}
			if (count > 200)
				break;
			t0.remove(p0);
			t1.remove(p1);
			setup(p0, p1, ex);
		}
		for (P p : t0) {
			Dot d = new Dot(p.copy(), P.polar(1e-5, rand(p2)), 2e-4, 1000, d3);
			add(new DotBullet(d), ex);
		}
		for (P p : t1)
			setup(pc, p, ex);
	}

	private void setup(P p0, P p1, int ex) {
		double dis = p0.dis(p1);
		double h0 = 1e-5, h1 = p2 - 1e-5, h = 1.5;
		while (Math.abs(dis / (2 * Math.sin(h / 2)) * h - 600) < 1) {
			h = (h0 + h1) / 2;
			if (dis * h < Math.sin(h / 2) * 1200)
				h0 = h;
			else
				h1 = h;
		}
		double r = dis / (2 * Math.sin(h / 2));
		int s = rand(1) < 0.5 ? 1 : -1;
		P cen = P.polar(r, p0.atan2(p1) + s * (p2 / 4 - h / 2)).plus(p0);
		CosCurve cc = new CosCurve(cen, r, cen.atan2(p0), s * h / tx, tx);
		Dot b0 = new Dot(p0.copy(), d2, cc);
		add(new DotBullet(b0, tx).setLv(K_FUNCTIONAL), ex);
	}

}
