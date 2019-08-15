package stage.s0;

import battle.Sprite;
import battle.bullet.Mover;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S044 extends SpellCard implements Emiter.Ticker {

	private static class ColorBullet {

		private final Dot host;
		private final Dot eye;
		private final Dot[][] dots;

		private int dire, time, state = 0, ns = 0, nt = 0;

		private ColorBullet(int dir, Dot d) {
			dire = dir;
			host = d;
			eye = new Dot(host.pos.copy(), des[dir]);
			dots = new Dot[2][ne];
		}

		private void trigger(int st) {
			state = 1;
			if (st == 0) {
				ns = 2;
				time = t0;
				for (int i = 0; i < ne; i++) {
					double dis = rad * (i + 0.5) / ne - rad / 2;
					double va = dis * 2 / t0 / t0;
					P pv = P.polar(va * t0 + 1e-8, dire * p2 / 4);
					P pa = P.polar(-va, dire * p2 / 4);
					for (int j = 0; j < 2; j++) {
						dots[j][i] = new Dot(host.pos.copy(), pv, pa, t0, drs[dire]);
						add(new DotBullet(dots[j][i], 60000).setLv(K_FUNCTIONAL));
					}
				}
				add(new DotBullet(eye, 60000).setLv(K_FUNCTIONAL));
			}
			if (st == 1) {
				ns = 3;
				time = t1;
				for (int i = 0; i < ne; i++)
					for (int j = 0; j < 2; j++) {
						double x = rad * (i + 0.5) / ne - rad / 2;
						double dis = rad / 4 * cos(x * PI / rad);
						double va = dis * 2 / t1 / t1 * (j * 2 - 1);
						P pv = P.polar(va * t1 + 1e-8, (1 - dire) * p2 / 4);
						P pa = P.polar(-va, (1 - dire) * p2 / 4);
						dots[j][i].setMove(pv, pa, 0, t1);
					}
			}
			if (st == 2) {
				ns = 2;
				time = t1;
				for (int i = 0; i < ne; i++)
					for (int j = 0; j < 2; j++) {
						double x = rad * (i + 0.5) / ne - rad / 2;
						double dis = -rad / 4 * cos(x * PI / rad);
						double va = dis * 2 / t1 / t1 * (j * 2 - 1);
						P pv = P.polar(va * t1 + 1e-8, (1 - dire) * p2 / 4);
						P pa = P.polar(-va, (1 - dire) * p2 / 4);
						dots[j][i].setMove(pv, pa, 0, t1);
					}
			}
		}

	}

	private static final Sprite.SParam dr0 = Sprite.getDot(10302, 1, 1, 2);
	private static final Sprite.SParam dr1 = Sprite.getDot(10306, 1, 1, 2);
	private static final Sprite.SParam[] drs = { dr0, dr1 };

	private static final Sprite.SParam de0 = Sprite.getDot(11201, 0, 1, 1);
	private static final Sprite.SParam de1 = Sprite.getDot(11205, 0, 1, 1);
	private static final Sprite.SParam[] des = { de0, de1 };

	private static final Sprite.SParam db0 = Sprite.getDot(10902, 1, 1, 0);
	private static final Sprite.SParam db1 = Sprite.getDot(10906, 1, 1, 0);
	private static final Sprite.SParam[] dbs = { db0, db1 };

	private static final Sprite.SParam ds0 = Sprite.getDot(20201, 1, 1, 0);
	private static final Sprite.SParam ds1 = Sprite.getDot(20203, 1, 1, 0);
	private static final Sprite.SParam[] dss = { ds0, ds1 };

	private static final Sprite.SParam dl0 = Sprite.getDot(30000, 1, 1, 2);
	private static final Sprite.SParam dl1 = Sprite.getDot(30001, 1, 1, 2);
	private static final Sprite.SParam[] dls = { dl0, dl1 };

	private static final int f0 = 4000, f1 = 500, t0 = 2000, t1 = 200;
	private static final int l0 = 1000, l1 = 3000, l2 = 2000, l3 = 1000;
	private static final int ne = 10, nx = 10, ny = 12;
	private static final double rad = 120, touch = 100, red = 8, v0 = 0.1, v1 = 0.3;

	private static final int[] ns = { 3, 4, 5, 6 };
	private static final int[] ms = { 1, 2, 3, 4 };

	private final ColorBullet[] cb;
	private final int n, m;

	private DotBullet shot;

	public S044(int diff) {
		super(60000);
		cb = new ColorBullet[nx * ny];
		n = ns[diff];
		m = ms[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0)
			for (ColorBullet c : cb)
				c.trigger(0);
		if (e.id == 1) {
			for (int i = 0; i < n; i++) {
				int x = (int) rand(nx * ny);
				if (cb[x].state == 2) {
					cb[x].trigger(1);
					cb[x].nt = l1;
				}
			}
		}
		if (e.id == 2) {
			for (ColorBullet c : cb) {
				if (c.state == 3) {
					double a0 = rand(p2);
					for (int i = 0; i < m; i++) {
						double a1 = a0 + p2 / m * i;
						P pv = P.polar(v0, a1);
						add(new DotBullet(new Dot(c.host.pos.copy(), pv, dbs[c.dire])), ex);
					}
				}
			}
		}
		if (e.id == 3) {
			P pv = P.polar(v1, pos.atan2(getPlayer().pos));
			add(shot = new DotBullet(new Dot(pos.copy(), pv, dls[it % 2])), ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			for (int i = 0; i < nx; i++)
				for (int j = 0; j < ny; j++) {
					int x = (i + j) % 2;
					P targ = new P(o.x * (i - 0.5) / (nx - 2), o.y * (j - 0.5) / (ny - 2));
					double va = pos.dis(targ) * 2 / t0 / t0;
					P pv = P.polar(va * t0 + 1e-5, pos.atan2(targ));
					Dot d = new Dot(pos.copy(), dss[x]);
					cb[i * ny + j] = new ColorBullet(x, d);
					d.setMove(new Mover.LineMover(pv, -va, 0, t0));
					add(new DotBullet(d, 60000).setLv(K_FUNCTIONAL));
				}
			add(new Emiter(0, t0, this));
			add(new Emiter(1, f0, this, this).setDelay(t0 * 2));
			add(new Emiter(2, f1, this, this).setDelay(t0 * 2));
			add(new Emiter(3, f0, this, this).setDelay(t0 * 2));
		}
		for (ColorBullet c : cb) {
			boolean i0 = getPlayer().pos.dis(c.host.pos) < touch;
			boolean i1 = false;
			if (shot != null)
				if (shot.isDead())
					shot = null;
				else
					i1 = shot.getDot().pos.dis(c.host.pos) < touch;
			if (c.time > 0) {
				if (c.state == 3 && i0)
					c.time = max(c.time, l2);
				else if (c.state == 3 && i1)
					c.time = max(c.time, l3);
				else
					c.time -= dt;
				if (c.time <= 0) {
					c.time = 0;
					if (c.state == 1) {
						c.state = c.ns;
						if (c.state == 3)
							c.time = c.nt;
						c.nt = 0;
					} else if (c.state == 3) {
						c.trigger(2);
						c.nt = 0;
					} else if (c.state == 4) {
						c.trigger(1);
						c.nt = l2;
					}
				}
			}
			if (c.state == 2 && i0) {
				c.state = 4;
				c.time = l0;
			}
			if (c.state == 2 && i1) {
				c.trigger(1);
				c.nt = l3;
			}
			if (c.state == 1 && time > t0 * 2 && (c.time < 120 && c.ns == 3 || c.time > 80 && c.ns == 2)
					|| c.state == 3)
				c.eye.pos.setTo(P.polar(red, c.host.pos.atan2(getPlayer().pos)).plus(c.host.pos));
			else
				c.eye.pos.setTo(-100, -100);
		}
		super.update(dt);
	}

}
