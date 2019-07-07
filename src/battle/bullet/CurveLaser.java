package battle.bullet;

import battle.Shape;
import battle.entity.Bullet;

public class CurveLaser extends Bullet {

	private final AbCurve curve;

	public CurveLaser(AbCurve c) {
		curve = c;
		addCtrl(curve);

	}

	@Override
	public Shape getShape() {
		return curve;
	}

	@Override
	protected void draw() {
		curve.sprite.draw();
	}

}
