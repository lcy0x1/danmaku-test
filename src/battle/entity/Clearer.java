package battle.entity;

import battle.Control;
import battle.Entity;
import battle.Shape;
import util.P;

public class Clearer extends Entity {

	private final Shape.Circle shape;
	private final double vr;
	private final int hard;

	private double nr;

	public Clearer(int atk, P pos, double r, double v, int t, int lv) {
		super(0, atk);
		shape = new Shape.Circle(pos, r);
		addCtrl(new Control.TimeCtrl(t));
		nr = r;
		vr = v;
		hard = lv;
	}

	public Clearer(P pos, double r, double v, int t, int lv) {
		this(C_BULLET | C_ENEMY, pos, r, v, t, lv);
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

	@Override
	public Shape getShape() {
		return shape;
	}

	@Override
	public void post() {
		super.post();
		shape.r += nr;
	}

	@Override
	public void update(int dt) {
		super.update(dt);
		nr = vr * dt;
	}

	@Override
	protected void attack(Entity e) {
		if (shape.dis(e.getShape()) < 0)
			e.getEntCtrl().killed(hard);
	}

}
