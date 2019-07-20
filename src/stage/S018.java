package stage;

import battle.Engine;
import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Mover;
import battle.entity.Emiter;
import util.P;

public class S018 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getSprite(Sprite.P_D, 20403, 0, 1);
	private static final Sprite.SParam d1 = Sprite.getSprite(Sprite.P_D, 30000, 1, 1);

	private static final int f0 = 8000, f1 = 20;
	private static final int ref = 2, n3 = 20;
	private static final double v0 = 0.8, v1 = 6e-2, av = 5e-4, dr = 15, minr = 300;
	private static final P pos = new P(400, 200);

	private static final int[] ns = { 24, 32, 40, 48 };
	private static final int[] ts = { 150, 175, 200, 225 };
	private final int n, t1;

	public S018(int diff) {
		super(60000);
		t1 = ts[diff];
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			add(new Emiter(1, f1, f1 * t1, this), ex);
		}
		if (e.id == 1) {
			P[] avi = getAvi();
			for (int i = 0; i < n3; i++) {
				P pv = P.polar(v0, getRand(avi));
				Mover mov = new Mover.RefMover(pv, ref, 15);
				add(new DotBullet(new Dot(pos.copy(), d0, mov)), ex);
			}
			if (it == t1 - 1) {
				double a0 = rand(p2);
				for (int i = 0; i < n; i++) {
					Dot d = new Dot(pos.copy(), P.polar(v1, a0 + p2 / n * i), av, 3000, 6000, d1);
					add(new DotBullet(d), ex);
				}
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
		P[] avi = new P[13];
		avi[0] = Engine.RUNNING.pl.pos.copy();
		avi[1] = new P(-avi[0].x, avi[0].y);
		avi[2] = new P(avi[0].x, -avi[0].y);
		avi[3] = new P(o.x * 2 - avi[0].x, avi[0].y);
		avi[4] = new P(avi[0].x, o.y * 2 - avi[0].y);

		avi[5] = new P(-avi[0].x, -avi[0].y);
		avi[6] = new P(-avi[0].x, o.y * 2 - avi[0].y);
		avi[7] = new P(o.x * 2 - avi[0].x, -avi[0].y);
		avi[8] = new P(o.x * 2 - avi[0].x, o.y * 2 - avi[0].y);
		avi[9] = new P(avi[0].x, o.y * 2 + avi[0].y);
		avi[10] = new P(o.x * 2 + avi[0].x, avi[0].y);
		avi[11] = new P(avi[0].x, -o.y * 2 + avi[0].y);
		avi[12] = new P(-o.x * 2 + avi[0].x, avi[0].y);
		return avi;
	}

	private double getRand(P[] avi) {
		double ans = 0;
		boolean pass = false;
		while (!pass) {
			ans = rand(p2);
			pass = true;
			for (P p : avi) {
				double dis = pos.dis(p);
				if (dis < minr)
					dis = minr;
				if (Math.cos(ans - pos.atan2(p)) > Math.cos(dr / dis))
					pass = false;
			}
		}
		return ans;
	}

}
