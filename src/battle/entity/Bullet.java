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

}
