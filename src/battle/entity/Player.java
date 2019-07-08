package battle.entity;

import battle.Control;
import battle.Engine;
import battle.Entity;
import battle.Shape;
import battle.Sprite;
import util.Data;
import util.P;

public class Player extends Entity implements Sprite.DotESprite.Dire, Shape {

	private static final int DEADTIME = 3000;

	public final P ext, pos;
	public int deadCount;

	private final Shape.Circle shape;
	private final Sprite.DotESprite img;

	private int time, deadTime;

	public Player() {
		super(C_PLAYER, 0);
		pos = Engine.START.copy();
		ext = pos.copy();
		shape = new Shape.Circle(pos.copy(), 2);
		img = new Sprite.SParam(0, 0, 1).getEntity(this);
	}

	public void attacked(Entity e) {
		if (deadTime > 0 || Engine.RUNNING.stage.finished())
			return;
		deadTime = DEADTIME;
		deadCount++;
		if (Data.CLEARBL)
			Engine.RUNNING.add(new Clearer(pos.copy(), 0, 0.4, 1000, Control.K_BULLET));
	}

	@Override
	public double dis(Shape s) {
		double dis = pos.dis(ext);
		int n = (int) (1 + dis / shape.r / 2);
		double ans = Double.MAX_VALUE;
		for (int i = 0; i <= n; i++) {
			shape.pos.setTo(pos.middle(ext, 1.0 / n * i));
			ans = Math.min(ans, s.dis(shape));
		}
		return ans;
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
		return this;
	}

	@Override
	public void post() {
		pos.setTo(ext);
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

}
