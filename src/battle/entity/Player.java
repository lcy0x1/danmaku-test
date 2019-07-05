package battle.entity;

import battle.Control.EntCtrl;
import battle.Engine;
import battle.Entity;
import battle.Shape;
import battle.Sprite;
import util.P;

public class Player extends Entity implements Sprite.DotESprite.Dire {

	private static final int DEADTIME = 3000;

	public final P ext, pos;
	public int deadCount;

	private final Shape shape;
	private final Sprite.DotESprite img;

	private int time, deadTime;

	public Player() {
		super(C_PLAYER, 0);
		pos = Engine.START.copy();
		ext = pos.copy();
		shape = new Shape.Circle(pos, 2);
		img = new Sprite.DESParam(0, 0, 1).getEntity(this);
	}

	public void attacked(Entity e) {
		if (deadTime > 0)
			return;
		ext.setTo(Engine.START);
		deadTime = DEADTIME;
		deadCount++;
	}

	@Override
	public EntCtrl getCtrl() {
		return null;
	}

	@Override
	public double getDire() {
		return time * Math.PI / 6000;
	}

	@Override
	public P getPos() {
		return pos;
	}

	@Override
	public Shape getShape() {
		return shape;
	}

	@Override
	public void update(int t) {
		time += t;
		if (deadTime > 0)
			deadTime -= t;
	}

	@Override
	protected void attack(Entity e) {
	}

	@Override
	protected void draw() {
		if (deadTime <= 0 || deadTime / 100 % 2 == 0)
			img.draw();
	}

	@Override
	protected boolean isDead() {
		return false;
	}

	@Override
	protected void post() {
		pos.setTo(ext);
	}

}
