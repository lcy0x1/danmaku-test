package stage;

import battle.Sprite;
import battle.bullet.Laser;
import battle.bullet.Mover;
import battle.bullet.TimeCurve;
import battle.entity.Emiter;
import util.P;

public class S008 extends SpellCard implements Emiter.Ticker {

	private static final Sprite.SParam sp0 = Sprite.getCurve(Sprite.P_SR, 10402, 0, 1);
	private static final Sprite.SParam sp1 = Sprite.getCurve(Sprite.P_SR, 10406, 0, 1);
	private static final Sprite.SParam[] sps = { sp0, sp1 };
	private static final int f0 = 4000, l = 100, ref = 1;
	private static final double v0 = 0.6;
	private static final int[] ns = { 14, 21, 28, 35 };
	private final int n;

	public S008(int diff) {
		super(60000);
		n = ns[diff];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id == 0) {
			double a0 = rand(p2);
			for (int i = 0; i < 5; i++) {
				P p0 = P.polar(l, a0 + p2 / 5 * i);
				P p1 = P.polar(l, a0 + p2 / 5 * (i + 2));
				for (int j = 0; j < n; j++) {
					P pos = p0.middle(p1, 1.0 / n * j).plus(pc);
					Mover m = new Mover.RefMover(P.polar(v0 / l * pc.dis(pos), pc.atan2(pos)), ref);
					add(new Laser(new TimeCurve(sps[it % 2], 10, 40, pc, m), f0 * 3), ex);
				}
			}
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			add(new Emiter(0, f0, this, this));
		}
		super.update(dt);
	}

}
