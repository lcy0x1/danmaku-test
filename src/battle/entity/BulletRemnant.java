package battle.entity;

import battle.Entity;
import battle.Shape;
import battle.Sprite;
import util.P;

public class BulletRemnant extends Entity implements Sprite.Dire {

	private final P pos;

	public BulletRemnant(P p) {
		super(0, 0);
		pos = p.copy();
	}

	@Override
	public void draw() {

	}

	@Override
	public double getDire() {
		return 0;
	}

	@Override
	public P getPos() {
		return pos;
	}

	@Override
	public Shape getShape() {
		return null;
	}

	@Override
	public int getTime() {
		return 0;
	}

	@Override
	protected void attack(Entity e) {
	}

}
