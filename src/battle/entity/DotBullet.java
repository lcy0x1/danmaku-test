package battle.entity;

import battle.Entity;
import battle.Shape;

public class DotBullet extends Bullet {

	public Dot dot;

	public DotBullet(Dot d) {
		addCtrl(dot = d);
	}

	public DotBullet(Dot d, int t) {
		super(t);
		addCtrl(dot = d.setCtrl(false));
	}

	@Override
	public Shape getShape() {
		return dot.shape;
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

	@Override
	protected void draw() {
		if (dot.sprite != null)
			dot.sprite.draw();
	}

}
