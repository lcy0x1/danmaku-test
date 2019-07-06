package battle.entity;

import battle.Control;
import battle.Entity;

public abstract class Bullet extends Entity {

	public Bullet() {
		super(C_BULLET, C_PLAYER);
	}

	public Bullet(int t) {
		super(C_BULLET, C_PLAYER);
		addCtrl(new Control.TimeCtrl(t));
	}

	@Override
	protected void attack(Entity e) {
		if (getShape() == null || e.getShape() == null)
			return;
		if (getShape().dis(e.getShape()) < 0) {
			if (e instanceof Player)
				((Player) e).attacked(this);
		}
	}

}
