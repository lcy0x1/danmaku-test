package battle.bullet;

import battle.Entity;
import battle.Shape;
import battle.entity.Bullet;

public class DotBullet extends Bullet {

	public Dot dot;

	public DotBullet(Dot d) {
		addCtrl(dot = d);
	}

	public DotBullet(Dot d, int t) {
		super(t);
		addUpdt(dot = d);
	}

	@Override
	public void attack(Entity e) {
		if (!dot.spr.active())
			return;
		super.attack(e);
	}

	@Override
	public Shape getShape() {
		return dot.spr.getShape();
	}

	@Override
	protected void draw() {
		if (dot.finished())
			return;
		if (dot.spr.getSprite() != null)
			dot.spr.getSprite().draw();
	}

}
