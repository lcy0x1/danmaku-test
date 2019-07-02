package battle.entity;

import battle.Entity;
import battle.Shape;

public class DotBullet extends Bullet {

	public Dot dot;

	public DotBullet(Dot d) {
		dot = d;
		ctrl.move = d;
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
	protected void post() {
		dot.post();
	}

	@Override
	protected void update(int t) {
		super.update(t);
		dot.update(t);
	}

}
