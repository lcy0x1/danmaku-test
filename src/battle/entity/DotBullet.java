package battle.entity;

import battle.Entity;
import battle.Shape;

public class DotBullet extends Bullet {

	public Dot dot;

	public DotBullet(Dot d) {
		dot = d;
	}

	@Override
	public Shape getShape() {
		return dot.shape;
	}

	@Override
	protected void collide(Entity e) {
		if (getShape() == null || e.getShape() == null)
			return;
		if (getShape().dis(e.getShape()) < 0) {
			// TODO
		}
	}

	@Override
	protected void draw() {
		dot.sprite.draw();
	}

	@Override
	protected boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void post() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void update(int t) {
		// TODO Auto-generated method stub

	}

}
