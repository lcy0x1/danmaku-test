package battle.entity;

import battle.Entity;
import battle.LifeControl;

public abstract class Bullet extends Entity {

	public LifeControl ctrl;

	public Bullet() {
		super(C_BULLET, C_PLAYER);
		ctrl = new LifeControl();
	}

	@Override
	protected boolean isDead() {
		return ctrl.isDead();
	}

	@Override
	protected void update(int t) {
		ctrl.update(t);
	}

}
