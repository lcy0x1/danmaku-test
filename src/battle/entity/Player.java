package battle.entity;

import battle.Control;
import battle.Engine;
import battle.Entity;
import battle.Shape;
import battle.Sprite;
import battle.Updatable;
import battle.bullet.Dot;
import battle.special.FollowMover;
import util.Data;
import util.P;

public class Player extends Entity implements Sprite.Dire, Shape {

	public static abstract class PlAtker implements Updatable {

		private static class LinearAtker extends PlAtker {

			private static final Sprite.SParam sp = Sprite.getDot(10701, 0, 1, Sprite.L_PLATK, 128);
			private static final double v0 = 0.5, w = Math.PI / 3000;

			private static final int DEF_ATK = 100;

			private LinearAtker(Player p) {
				super(p, 200);
			}

			@Override
			protected void attack(int id, int it, int ex) {
				P pos = super.pl.pos;
				P d0 = new P(-10, 0).plus(pos);
				Dot dt0 = new Dot(d0, new P(0, -v0), sp);
				Engine.RUNNING.add(new PlAtk.DotNPA(DEF_ATK, dt0));

				P d1 = new P(10, 0).plus(pos);
				Dot dt1 = new Dot(d1, new P(0, -v0), sp);
				Engine.RUNNING.add(new PlAtk.DotNPA(DEF_ATK, dt1));

				P d2 = new P(-10, 0).plus(pos);
				P pv2 = P.polar(v0, -Math.PI / 4);
				Dot dt2 = new Dot(d2, sp, FollowMover.getMover(pv2, w, C_ENEMY));
				Engine.RUNNING.add(new PlAtk.DotNPA(DEF_ATK, dt2));

				P d3 = new P(10, 0).plus(pos);
				P pv3 = P.polar(v0, -3 * Math.PI / 4);
				Dot dt3 = new Dot(d3, sp, FollowMover.getMover(pv3, w, C_ENEMY));
				Engine.RUNNING.add(new PlAtk.DotNPA(DEF_ATK, dt3));
			}

		}

		private static PlAtker getAtker(Player p, int t) {

			return new LinearAtker(p);

		}

		public boolean attack = false;

		private int time;
		private final Player pl;
		private final int[] fs;

		private PlAtker(Player p, int... is) {
			pl = p;
			fs = is;
		}

		@Override
		public void update(int dt) {
			if (attack) {
				if (time >= 0)
					for (int i = 0; i < fs.length; i++) {
						int f = fs[i];
						int rem = f - (time + f - 1) % f - 1;
						if (rem < dt) {
							int it = (time + rem) / f;
							attack(i, it, rem);
						}
					}
				time += dt;
			}
		}

		protected abstract void attack(int id, int it, int ex);

	}

	private static final int DEADTIME = 3000;

	public final P ext, pos;
	public int deadCount;

	private final Shape.Circle shape;
	private final Sprite.ESprite img;

	public PlAtker atker;

	private int time, deadTime, dt;

	public Player(P plp, int t0) {
		super(C_PLAYER, 0);
		pos = plp.copy();
		ext = pos.copy();
		shape = new Shape.Circle(pos.copy(), 2);
		img = Sprite.getDot(0, 0, Sprite.L_TOP).getEntity(this);
		atker = PlAtker.getAtker(this, t0);
		addUpdt(atker);
	}

	public void attacked(Entity e) {
		if (deadTime > 0 || Engine.RUNNING.stage.finished())
			return;
		deadTime = DEADTIME;
		deadCount++;
		if (Data.CLEARBL)
			Engine.RUNNING.add(new Clearer(pos.copy(), 0, 0.4, 1000, Control.K_BULLET));
	}

	@Override
	public double dis(Shape s) {
		double dis = pos.dis(ext);
		int n = (int) (1 + dis / shape.r / 2);
		double ans = Double.MAX_VALUE;
		for (int i = 0; i <= n; i++) {
			shape.pos.setTo(pos.middle(ext, 1.0 / n * i));
			ans = Math.min(ans, s.dis(shape));
		}
		return ans;
	}

	@Override
	public void draw() {
		if (deadTime <= 0 || deadTime / 100 % 2 == 0)
			img.draw();
	}

	@Override
	public double getDire() {
		return 0;
	}

	@Override
	public P getPos() {
		return pos;
	}

	@Override
	public Shape getShape() {
		return this;
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public void post() {
		super.post();
		pos.setTo(pos.middle(ext, dt / 20.0));
		ext.setTo(pos);
	}

	@Override
	public void update(int t) {
		super.update(t);
		dt = t;
		time += t;
		if (deadTime > 0)
			deadTime -= t;
	}

	@Override
	protected void attack(Entity e) {
	}

}
