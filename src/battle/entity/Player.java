package battle.entity;

import battle.Engine;
import battle.Entity;
import battle.Shape;
import util.P;

public class Player extends Entity {

	public final P pos;

	public Player() {
		super(C_PLAYER, 0);
		pos = Engine.START.copy();
	}

	@Override
	public Shape getShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(int t) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void collide(Entity e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void draw() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void post() {
		// TODO Auto-generated method stub

	}

}
