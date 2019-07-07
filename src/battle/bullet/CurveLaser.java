package battle.bullet;

import battle.Shape;
import battle.bullet.AbCurve.ListCurve;
import battle.entity.Bullet;

public class CurveLaser extends Bullet {

	private final AbCurve curve;

	public CurveLaser(AbCurve c) {
		addCtrl(curve = c);

	}

	public CurveLaser(ListCurve c, int lt) {
		super(lt);
		addCtrl(curve = c);
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
