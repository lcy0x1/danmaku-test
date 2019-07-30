package battle.bullet;

import battle.Entity;
import battle.Shape;
import battle.bullet.Curve.DotCont;
import battle.entity.Bullet;

public class DotBullet extends Bullet implements DotCont {

	public Dot dot;

	public DotBullet(Dot d) {
		addCtrl(dot = d);
	}

	public DotBullet(Dot d, int t) {
		super(t);
		addUpdt(dot = d);
	}

	public DotBullet(Dot d, int t, boolean add) {
		super(t);
		dot = d;
		if (add)
			addCtrl(d);
		else
			addUpdt(d);
	}

	public DotBullet(int base, int atk, Dot d) {
		super(base, atk);
		addCtrl(dot = d);
	}

	@Override
	public void attack(Entity e) {
		if (!dot.spr.active())
			return;
		super.attack(e);
	}

	@Override
	public void draw() {
		dot.draw();
	}

	@Override
	public Dot getDot() {
		return dot;
	}

	@Override
	public Shape getShape() {
		return dot.spr.getShape();
	}

}
