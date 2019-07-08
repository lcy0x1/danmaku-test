package battle.bullet;

import battle.Shape;
import battle.bullet.Curve.ListCurve;
import battle.entity.Bullet;

public class Laser extends Bullet {

	private final Curve curve;

	public Laser(Curve c) {
		addCtrl(curve = c);

	}

	public Laser(ListCurve c, int lt) {
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
