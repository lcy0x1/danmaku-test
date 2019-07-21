package battle.entity;

import battle.Control;
import battle.Entity;
import battle.Shape;
import battle.bullet.Curve.DotCont;
import battle.bullet.Dot;

public abstract class PlAtk extends Entity {

	public static class DotNPA extends PlAtk implements DotCont {

		private final Dot dot;

		public DotNPA(int atk, Dot d) {
			super(T_N, atk);
			addCtrl(dot = d);
		}

		@Override
		protected void doAttack(Life l) {
			if (isDead() || !dot.spr.active())
				return;
			l.attacked(T_N, atk);
			getEntCtrl().killed(Control.K_FUNCTIONAL);
		}

		@Override
		public Shape getShape() {
			return dot.spr.getShape();
		}

		public void draw() {
			if (dot.finished())
				return;
			if (dot.spr.getSprite() != null)
				dot.spr.getSprite().draw();
		}

		@Override
		public Dot getDot() {
			return dot;
		}

	}

	public static final int T_N = 0, T_S = 1;

	public final int type, atk;

	public PlAtk(int t, int a) {
		super(C_PATK, C_ENEMY);
		type = t;
		atk = a;
	}

	@Override
	protected void attack(Entity e) {
		if (getShape() == null || e.getShape() == null)
			return;
		if (e instanceof Life)
			if (getShape().dis(e.getShape()) < 0)
				doAttack((Life) e);
	}

	protected abstract void doAttack(Life l);

}
