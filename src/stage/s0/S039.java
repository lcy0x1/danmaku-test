package stage.s0;

import battle.Sprite;
import battle.bullet.BulletRing;
import battle.bullet.Curve.ListCurve;
import battle.bullet.Func;
import battle.bullet.Func.VeloListFunc;
import battle.bullet.Func.QuadVLF;
import battle.bullet.Func.Adder;
import battle.bullet.Laser;
import battle.bullet.Mover;
import battle.bullet.TimeCurve;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S039 extends SpellCard implements Emiter.Ticker {

	private static class TimeFunc implements Func {

		private static final double w0 = p2 / 2000;

		private final double x;

		private TimeFunc(double a) {
			x = a;
		}

		@Override
		public boolean exist(P pos, double r, int time, int i) {
			return time < lt1;
		}

		@Override
		public P func(int time, int i) {
			double t = time * (1 - cos(PI * time / lt1 / 2));
			return new P(x * sin(t * w0), t * v0);
		}

	}

	private static final Sprite.SParam d0 = Sprite.getCurve(Sprite.P_C, 11404, 1, 1);
	private static final Sprite.SParam d1 = Sprite.getCurve(Sprite.P_C, 11410, 1, 1);

	private static final Sprite.SParam dl0 = Sprite.getCurve(Sprite.P_SR, 10404, 1, 1);
	private static final Sprite.SParam dl1 = Sprite.getCurve(Sprite.P_SR, 10410, 1, 1);

	private static final Sprite.SParam ds0 = Sprite.getDot(10704, 1, 1);
	private static final Sprite.SParam ds1 = Sprite.getDot(10710, 1, 1);

	private static final Sprite.SParam[] ds = { d0, d1 };
	private static final Sprite.SParam[] dls = { dl0, dl1 };
	private static final Sprite.SParam[] dss = { ds0, ds1 };

	private static final int f0 = 5000, f1 = 100, f3 = 40, t0 = 160, t1 = 1500, t3 = 500;
	private static final int m0 = 20, m1 = 50, lt = t0 * 60, lt1 = 3000, ra = 50, at = 2000, lt2 = 4000;
	private static final double v0 = 0.4, v1 = 0.3, va = 2e-4;
	private static final P[] ps = new P[] { new P(250, 350), new P(550, 350) };

	private static final int[] ns = { 3, 4, 5, 6 };
	private static final int[] ms = { 6, 7, 8, 9 };
	private static final int[] fs = { 9, 8, 7, 6 };

	private VeloListFunc vlfl[] = new VeloListFunc[2];
	private Func vlfs[] = new Func[2];

	private final int n0, n1, f2, m2;

	public S039(int diff) {
		super(60000, new P(400, 200));
		n0 = ns[diff] * 3;
		n1 = ns[diff] * 8;
		m2 = ms[diff] * 2;
		f2 = fs[diff] * 20;
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		if (e.id / 2 == 0) {
			int x = e.id;
			double a0 = rand(p2);
			for (int i = 0; i < n0; i++) {
				double a = a0 + p2 / n0 * i;

				for (int j = 0; j < 2; j++) {
					ListCurve c = new ListCurve(ds[x]);
					Adder ad = new Adder(ps[x], c, vlfl[j], a, ds[x], lt);
					add(new Emiter(0, f1, f1 * m0 + t3 * j, ad).setDelay(t3 * j), ex);
					add(new Laser(c, lt), ex);
					Mover mov = new Mover.LineMover(P.polar(v1, a + p2 / n0 / 2 * j), va, 0, at);
					int dt = t3 * (j + 1);
					add(new Laser(new TimeCurve(dls[x], m1, f3, ps[x], mov, dt), lt2 + dt), ex);
				}

			}
		}
		if (e.id / 2 == 1) {
			add(new Emiter(e.id + 4, f2, f2 * m2, this), ex);
		}
		if (e.id / 2 == 2) {
			int x = e.id % 2;

			for (int j = 0; j < 2; j++) {
				double a0 = rand(p2);
				for (int i = 0; i < n0; i++) {
					double a = a0 + p2 / n0 * i;
					ListCurve c = new ListCurve(ds[x]);
					Adder ad = new Adder(pos, c, vlfs[j], a, ds[x], lt1);
					add(new Emiter(0, f1, f1 * m0 + t3 * j, ad).setDelay(t3 * j), ex);
					add(new Laser(c, lt), ex);
				}
			}

		}
		if (e.id / 2 == 3) {
			int x = e.id % 2;
			double a0 = rand(p2);
			add(new BulletRing(pos, dss[x], n1, new Mover.LineMover(P.polar(v1, a0), va, 0, at)), ex);
		}
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			for (int i = 0; i < 2; i++) {
				vlfl[i] = new QuadVLF();
				double a = i * 2 - 1;
				vlfl[i].add(P.polar(v0, -a * p2 / 12), 1 * t0);
				vlfl[i].add(P.polar(v0, 0), 5 * t0);
				vlfl[i].add(P.polar(v0, a * p2 / 8), 3 * t0);
				vlfl[i].add(P.polar(v0, 0), 3 * t0);
				vlfl[i].add(P.polar(v0, -a * p2 / 8), 2 * t0);
				vlfl[i].add(P.polar(v0, -a * p2 / 4), 2 * t0);
				vlfl[i].add(P.polar(v0, -a * 3 * p2 / 8), 2 * t0);
				vlfl[i].add(P.polar(v0, -a * p2 / 2), 2 * t0);
				vlfl[i].add(P.polar(v0, a * 3 * p2 / 8), 6 * t0);
				vlfl[i].add(P.polar(v0, a * 3 * p2 / 8), 6 * t0);
				vlfl[i].add(P.polar(v0, a * p2 / 4), 6 * t0);
				vlfl[i].add(P.polar(v0, a * p2 / 8), 6 * t0);
			}
			vlfs[0] = new TimeFunc(ra);
			vlfs[1] = new TimeFunc(-ra);
			add(new Emiter(0, f0 * 2, this, this));
			add(new Emiter(1, f0 * 2, this, this).setDelay(f0));
			add(new Emiter(2, f0 * 2, this, this).setDelay(f0));
			add(new Emiter(3, f0 * 2, this, this));
			add(new Emiter(4, f0 * 2, this, this).setDelay(f0 + t1));
			add(new Emiter(5, f0 * 2, this, this).setDelay(t1));
		}
		super.update(dt);
	}

}
