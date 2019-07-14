package stage;

import battle.Engine;
import battle.Entity;
import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Clearer;
import battle.entity.Emiter;
import util.P;

public class TestStage_016 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_D, 20403, 0, 1);
	private static final Sprite.SParam d1 = Sprite.getSprite(Sprite.P_D, 11206, 0, 1);

	private static final int CHARA = 1 << 8;

	private static final int f0 = 8000, f1 = 20, f2 = 160, t2 = 30, t3 = 120;
	private static final int del3 = 4000, ref = 1, n3 = 20, x = 10;
	private static final double v1 = 0.05, v2 = 0.8, vc = 1.2, dr = 0.9998;
	private static final P pos = new P(400, 200);

	private static final int[] ns = { 2, 2, 2, 2 };
	private static final int[] ts = { 40, 50, 60, 70 };
	private static final double[] vs = { 0.35, 0.4, 0.45, 0.5 };

	private final int n, t1;
	private final double v0;

	private double[] tmpd;
	private Dot[] tmpp;

	public TestStage_016(int diff) {
		super(60000);
		n = ns[diff];
		t1 = ts[diff];
		v0 = vs[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			tmpd = new double[n * t1];
			tmpp = new Dot[n * t1];
			add(new Emiter(1, f1, f1 * t1, this), ex);
			add(new Emiter(2, f2, f2 * t2, this), ex);
			add(new Emiter(3, f1, f1 * t3 + del3, this).setDelay(del3), ex);
		}
		if (e.id == 1) {
			for (int i = 0; i < n; i++) {
				int ii = it * n + i;
				tmpd[ii] = rand(p2);
				P pv = P.polar(v0, tmpd[ii]);
				Mover mov = new Mover.RefMover(pv, ref, 7);
				tmpp[ii] = new Dot(pos.copy(), d0, mov);
				add(new DotBullet(tmpp[ii]).setLv(K_FUNCTIONAL), ex);
			}
		}
		if (e.id == 2) {
			for (Dot dot : tmpp)
				if (dot != null && !dot.finished()) {
					P pv = P.polar(v1, dot.getDire());
					Dot d = new Dot(dot.pos.copy(), pv, d1);
					int base = Entity.C_BULLET | CHARA;
					int atk = Entity.C_PLAYER;
					add(new DotBullet(base, atk, d), ex);
				}
		}
		if (e.id == 3) {
			if (it == 0)
				add(new Clearer(CHARA, pos.copy(), 0, vc, 1000, K_BULLET), ex);
			for (int i = 0; i < n * t1 / x; i++) {
				int ii = it % x * n * t1 / x + i;
				P pv = P.polar(v2, tmpd[ii]);
				Mover mov = new Mover.RefMover(pv, ref, 7);
				Dot d = new Dot(pos.copy(), d0, mov);
				add(new DotBullet(d), ex);
			}
			P[] avi = getAvi();
			for (int i = 0; i < n3; i++) {
				P pv = P.polar(v2, getRand(avi));
				Mover mov = new Mover.RefMover(pv, ref, 7);
				add(new DotBullet(new Dot(pos.copy(), d0, mov)), ex);
			}
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			add(new Emiter(0, f0, this, this));
		super.update(dt);
	}

	private P[] getAvi() {
		P[] avi = new P[4];
		avi[0] = Engine.RUNNING.pl.pos.copy();
		avi[1] = new P(-avi[0].x, avi[0].y);
		avi[2] = new P(avi[0].x, -avi[0].y);
		avi[3] = new P(o.x * 2 - avi[0].x, avi[0].y);
		return avi;
	}

	private double getRand(P[] avi) {
		double ans = 0;
		boolean pass = false;
		while (!pass) {
			ans = rand(p2);
			pass = true;
			for (P p : avi) {
				if (Math.cos(ans - pos.atan2(p)) > dr)
					pass = false;
			}
		}
		return ans;
	}

}
