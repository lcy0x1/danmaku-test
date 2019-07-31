package stage.s0;

import battle.Sprite;
import battle.bullet.BulletRing;
import battle.bullet.Curve.ListCurve;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.bullet.Laser;
import battle.bullet.Mover;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S038 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam d0 = Sprite.getDot(20304, 0);
	private static final Sprite.SParam d1 = Sprite.getDot(20302, 0);
	private static final Sprite.SParam d2 = Sprite.getDot(20204, 0);
	private static final Sprite.SParam d3 = Sprite.getCurve(Sprite.P_SR, 10404, 1, 1);

	private static final int r0 = 400, r1 = 400, m = 3, lt = 17000;
	private static final double v0 = 0.1, v1 = 0.2, dv = 0.02, v2 = 0.25, dv2 = 0.2;

	private static final double wa0 = -1.0 / 8, wa1 = -1.0 / 6;

	private static final int[] n1s = { 6, 7, 8, 9 };
	private static final int[] f1s = { 10, 9, 8, 7 };

	private final int n0, f0, n1, f1, n2, f2;
	private final double w0, w1;

	public S038(int diff) {
		super(60000, new P(400, 200));
		n1 = n1s[diff] * 2;
		n0 = n2 = n1 * 2;
		f1 = f1s[diff] * 40;
		f0 = f1 * 3;
		f2 = f1 * 6;
		w0 = w1 = p2 / n0 / f0 - p2 / 19200 - p2 / f0 / n0 / 6;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a0 = it * f0 * w0;
			double wa = p2 / r0 * v0 * wa0;
			Mover mov = new Mover.CurveMover(-r0, a0, v0, wa);
			add(new BulletRing(pos, d0, n0, mov, lt));
		}
		if (e.id == 1) {
			double a0 = -it * f1 * w1;
			double wa = -p2 / r1 * v0 * wa1;
			Mover mov = new Mover.CurveMover(-r1, a0, v0, wa);
			add(new BulletRing(pos, d1, n1, mov, lt));
		}
		if (e.id == 2) {
			double a0 = pos.atan2(getPlayer().pos);
			for (int j = 0; j < m; j++)
				add(new BulletRing(pos, d2, n2, new Mover.LineMover(P.polar(v1 + dv * j, a0))), ex);
			for (int i = 0; i < n2; i++) {
				double a1 = a0 + p2 / n2 * i + p2 / n2 / 2;
				ListCurve lc = new ListCurve(d3);
				for (int j = 0; j < 2; j++) {
					DotBullet db = new DotBullet(new Dot(pos.copy(), P.polar(v2 + dv2 * j, a1), d3));
					add(db, ex);
					lc.addP(db);
				}
				add(new Laser(lc), ex);
			}
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			add(new Emiter(0, f0, this, this));
			add(new Emiter(1, f1, this, this));
			add(new Emiter(2, f2, this, this));
		}
		super.update(dt);
	}

}
