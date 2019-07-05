package battle.entity;

import battle.Control;
import battle.Entity;

public abstract class Life extends Entity implements Control.EntCtrl {

	public Life() {
		super(C_ENEMY, C_PLAYER);
	}

}
