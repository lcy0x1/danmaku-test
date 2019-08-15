package stage.s0;

import battle.Shape;
import battle.Sprite;
import battle.Shape.PosShape;
import battle.Sprite.ESprite;
import battle.bullet.Mover;
import battle.bullet.Dot;
import battle.bullet.DotBullet;
import battle.entity.Emiter;
import stage.SpellCard;
import util.P;

public class S043 extends SpellCard implements Emiter.Ticker {

	private static class ColorBullet implements Dot.PosSprite {

		private Dot host;

		/** 0: setup, 1: receptive, 2: releasing */
		private int n, state;

		private Shape.PosShape s0;
		private Sprite.ESprite dc, d0, d1;

		private ColorBullet(int num) {
			n = num;
		}

		@Override
		public boolean active() {
			return true;
		}

		@Override
		public PosShape getShape() {
			return s0;
		}

		@Override
		public ESprite getSprite() {
			return dc;
		}

		@Override
		public void load(Dot d) {
			host = d;
			dc = d0 = ds0.getEntity(d);
			d1 = ds1.getEntity(d);
			s0 = ds0.getShape(d.pos);
		}

		@Override
		public double radius() {
			return d0.radius();
		}

		@Override
		public void update(int dt) {
			if (state == 1 && getPlayer().pos.dis(host.pos) < rad)
				dc = d1;
		}

		private void trigger(int st) {
			if (st == 1)
				dc = d0;
			if (st == 2) {
				int x = dc == d0 ? 0 : 1;
				double a0 = rand(p2);
				for (int i = 0; i < n; i++) {
					double a1 = a0 + p2 / n * i;
					P pv = P.polar(v0 + 1e-5, a1);
					add(new DotBullet(new Dot(host.pos.copy(), pv, x == 0 ? sa : -sa, x == 0 ? t1 : t2, drs[x])));
				}
			}
			state = st;
		}

	}

	private static final Sprite.SParam dr0 = Sprite.getDot(10402, 0);
	private static final Sprite.SParam dr1 = Sprite.getDot(10406, 0);
	private static final Sprite.SParam[] drs = { dr0, dr1 };

	private static final Sprite.SParam ds0 = Sprite.getDot(20201, 0, 1, 1);
	private static final Sprite.SParam ds1 = Sprite.getDot(20203, 0, 1, 1);

	private static final int f0 = 8000, t0 = 2000, t1 = 1000, t2 = 3000, nx = 8, ny = 10;
	private static final double rad = 100, v0 = 0.1, sa = 1e-4;

	private static final int[] ns = { 4, 5, 6, 8 };

	private final int n;
	private final ColorBullet[] cb;

	public S043(int diff) {
		super(60000);
		n = ns[diff];
		cb = new ColorBullet[nx * ny];
	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		for (ColorBullet d : cb)
			d.trigger(e.id);
	}

	@Override
	public void update(int dt) {
		if (time == 0) {
			for (int i = 0; i < nx; i++)
				for (int j = 0; j < ny; j++) {
					P targ = new P(o.x * (i + 0.5) / nx, o.y * (j + 0.5) / ny);
					double va = pos.dis(targ) * 2 / t0 / t0;
					P pv = P.polar(va * t0 + 1e-5, pos.atan2(targ));
					Dot d = new Dot(pos.copy(), cb[i * ny + j] = new ColorBullet(n));
					d.setMove(new Mover.LineMover(pv, -va, 0, t0));
					add(new DotBullet(d).setLv(K_FUNCTIONAL));
				}
			add(new Emiter(1, f0, this, this).setDelay(t0));
			add(new Emiter(2, f0, this, this).setDelay(t0 + f0 / 2));
		}
		super.update(dt);
	}

}
