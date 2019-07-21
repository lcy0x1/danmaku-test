package battle.entity;

import battle.Control;
import battle.Entity;
import battle.Shape;
import battle.bullet.Dot;

public class Life extends Entity implements Control {

	private final int ndef, sdef;
	private final Dot dot;
	public double health = 1;

	public Life(int nd, int sd, Dot d) {
		super(C_ENEMY, C_PLAYER);
		ndef = nd;
		sdef = sd;
		dot = d;
		addCtrl(this);
		addCtrl(d);
	}

	@Override
	public void draw() {
		if (dot.finished())
			return;
		if (dot.spr.getSprite() != null)
			dot.spr.getSprite().draw();
	}

	@Override
	public boolean finished() {
		return health <= 0;
	}

	@Override
	public Shape getShape() {
		return dot.spr.getShape();
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

	protected void attacked(int type, double atk) {
		if (type == PlAtk.T_N)
			health -= atk / ndef;
		if (type == PlAtk.T_S)
			health -= atk / sdef;
	}

}
