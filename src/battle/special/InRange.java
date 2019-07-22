package battle.special;

import battle.Engine;
import battle.Shape;
import battle.Sprite;
import battle.Sprite.ESprite;
import battle.bullet.Dot;
import util.P;

public class InRange implements Dot.PosSprite {

	private final Sprite.SParam sp0, sp1;
	private final double rad;

	private Shape.PosShape s0, s1;
	private Sprite.ESprite es0, es1;
	private Dot dot;
	private final P pl;

	public InRange(Sprite.SParam x0, Sprite.SParam x1, double r) {
		sp0 = x0;
		sp1 = x1;
		rad = r;
		pl = Engine.RUNNING.pl.pos;
	}

	@Override
	public boolean active() {
		return true;
	}

	@Override
	public Shape getShape() {
		return inRange() ? s1 : s0;
	}

	@Override
	public ESprite getSprite() {
		return inRange() ? es1 : es0;
	}

	@Override
	public void load(Dot d) {
		dot = d;
		s0 = sp0.getShape(d.pos);
		s1 = sp1.getShape(d.pos);
		es0 = sp0.getEntity(d);
		es1 = sp1.getEntity(d);
	}

	@Override
	public double radius() {
		return inRange() ? sp0.getRadius() : sp1.getRadius();
	}

	private boolean inRange() {
		return dot.pos.dis(pl) < rad;
	}

}