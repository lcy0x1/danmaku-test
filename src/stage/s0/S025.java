package stage.s0;

import java.util.ArrayList;
import java.util.List;

import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import battle.special.InRange;
import stage.SpellCard;
import util.P;

public class S025 extends SpellCard implements Emiter.Ticker {

	private static class Interact implements Mover {

		private final P pos, pv, pa;

		private final int val, mass;

		private DotBullet src;

		private Interact(P pp, P vp, int v, int mas) {
			pos = pp;
			pv = vp;
			pa = new P(0, 0);
			val = v;
			mass = mas;
		}

		private Interact(P pp, P vp, int v, int mas, DotBullet d) {
			this(pp, vp, v, mas);
			src = d;
		}

		@Override
		public double getDire() {
			return pv.atan2();
		}

		@Override
		public boolean out(P pos, double r) {
			return false;
		}

		@Override
		public void update(Dot d, int t) {
			d.tmp.setTo(pos);
		}

		private void calc(Interact p) {
			double dis = pos.dis(p.pos);
			if (dis < 1)
				return;
			double a = c0 * val * p.val / dis / dis;
			P act = P.polar(a, p.pos.atan2(pos));
			pa.plus(act);
			p.pa.plus(act, -1);
		}

		private void finish(int t) {
			if (mass <= 0)
				return;
			pa.plus(wall(), c0 * val * val1);
			pa.times(1 / mass);
			if (pa.abs() > a2)
				pa.times(a2 / pa.abs());
			pos.plus(pv, t);
			pos.limit(lim0, lim1);
			pv.times(Math.pow(Math.E, -t * c1));
			pv.plus(pa, t);
			if (pv.abs() > v2)
				pv.times(v2 / pv.abs());
			pa.setTo(0, 0);
		}

		private P wall() {
			double px = Math.pow(lim0.x - pos.x, -2) - Math.pow(lim1.x - pos.x, -2);
			double py = Math.pow(lim0.y - pos.y, -2) - Math.pow(lim1.y - pos.y, -2);
			if (!Double.isFinite(px))
				px = 0;
			if (!Double.isFinite(py))
				py = 0;
			return new P(px, py);
		}

	}

	private static final Sprite.SParam d0 = Sprite.getDot(20100, 0);
	private static final Sprite.SParam d1 = Sprite.getDot(30001, 1, 1);
	private static final Sprite.SParam d2 = Sprite.getDot(10406, 0);
	private static final Sprite.SParam d3 = Sprite.getDot(20101, 0);
	private static final Sprite.SParam d4 = Sprite.getDot(11002, 0);

	private static final int f1 = 20, f2 = 40, f3 = 300, t3 = 2000, t4 = 4000;

	private static final double v0 = 0.1, v1 = 0.4, v2 = 0.3, a2 = 5e-3, v3 = 1e-5, a3 = 2e-4, a4 = 5e-5;
	private static final int val0 = 1, val1 = 3, val2 = 5, mass = 1;
	private static final double c0 = 5e0, c1 = 5e-3, da = p2 / 24;

	private static final P lim0 = new P(-50, -50), lim1 = new P(850, 1050);

	private static final int[] ns = { 45, 60, 75, 90 };
	private static final int[] ms = { 8, 10, 12, 14 };
	private static final int[] rs = { 100, 120, 140, 160 };
	private static final int[] fs = { 2000, 1700, 1400, 1100 };

	private final List<Interact> list = new ArrayList<Interact>();
	private final List<DotBullet> path = new ArrayList<DotBullet>();
	private final List<Dot> actv = new ArrayList<Dot>();

	private final int n, m, rad, f0;

	public S025(int diff) {
		super(60000, new P(400, 500));
		n = ns[diff];
		m = ms[diff];
		rad = rs[diff];
		f0 = fs[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a0 = pos.atan2(getPlayer().pos);
			P pv = P.polar(v1, a0);
			Dot d = new Dot(pos.copy(), pv, d1);
			DotBullet b0 = new DotBullet(d);
			add(b0.setLv(K_FUNCTIONAL), ex);
			list.add(new Interact(d.pos, pv, val2, -1, b0));
			path.add(b0);

		}
		if (e.id == 1) {
			P pv = P.polar(v0, rand(p2));
			Interact ita = new Interact(pos.copy(), pv, val0, mass);
			list.add(ita);
			Dot d = new Dot(pos.copy(), new InRange(d0, d3, rad)).setMove(ita);
			actv.add(d);
			add(new DotBullet(d).setLv(K_FUNCTIONAL), ex);
		}
		if (e.id == 2) {
			list.removeIf(x -> x.src != null && x.src.isDead());
			for (int i = 0; i < list.size(); i++)
				for (int j = 0; j < i; j++)
					list.get(i).calc(list.get(j));
			for (Interact ita : list)
				ita.finish(f1);
		}
		if (e.id == 3) {
			path.removeIf(x -> x.isDead());
			for (DotBullet b0 : path) {
				P pos = b0.dot.pos.copy();
				double a0 = b0.dot.getDire();
				P pv = P.polar(v3, a0);
				P pa = P.polar(a3, a0 + rand(da) - da / 2);
				add(new DotBullet(new Dot(pos, pv, pa, f0 * m, f0 * m + t3, d2)), ex);
			}
		}
		if (e.id == 4) {
			for (Dot d : actv)
				if (d.pos.dis(getPlayer().pos) < rad) {
					P pv = P.polar(v3, d.pos.atan2(getPlayer().pos));
					add(new DotBullet(new Dot(d.pos.copy(), pv, a4, t4, d4)), ex);
				}
		}

	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			add(new Emiter(0, f0, this, this).setDelay(f1 * n + f0));
			add(new Emiter(1, f1, f1 * n, this));
			add(new Emiter(2, f1, this, this));
			add(new Emiter(3, f2, this, this).setDelay(f1 * n + f0));
			add(new Emiter(4, f3, this, this).setDelay(f1 * n + f0));
		}
		super.update(dt);
	}

}
