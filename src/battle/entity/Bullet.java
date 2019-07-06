package battle.entity;

import battle.Control;
import battle.Entity;

public abstract class Bullet extends Entity {

	protected final Control.TimeCtrl ctrl;

	public Bullet() {
		super(C_BULLET, C_PLAYER);
		ctrl = new Control.TimeCtrl();
	}

	public Bullet(int t) {
		super(C_BULLET, C_PLAYER);
		ctrl = new Control.TimeCtrl(t);
	}

	@Override
	public Control.TimeCtrl getCtrl() {
		return ctrl;
	}

	public Bullet setLv(int lv) {
		ctrl.setLv(lv);
		return this;
	}

}
