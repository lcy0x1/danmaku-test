package battle.entity;

import battle.Entity;
import battle.Shape;

public class DotBullet extends Bullet {

	public Dot dot;

	public DotBullet(Dot d) {
		dot = d;
		ctrl.move = d;
	}

	public DotBullet(Dot d, int t) {
		super(t);
		dot = d;
	}

	@Override
	public Shape getShape() {
		return dot.shape;
	}

	@Override
	public void update(int t) {
		super.update(t);
		dot.update(t);
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

	@Override
	protected void post() {
		super.post();
		dot.post();
	}

}
