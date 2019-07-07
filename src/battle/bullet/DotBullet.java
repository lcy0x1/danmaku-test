package battle.bullet;

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
	public Shape getShape() {
		return dot.shape;
	}

	@Override
	protected void draw() {
		if (dot.sprite != null)
			dot.sprite.draw();
	}

}
