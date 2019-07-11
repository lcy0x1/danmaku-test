package battle.special;

import battle.Shape;
import battle.Shape.PosShape;
import battle.Sprite;
import battle.Sprite.ESprite;
import battle.bullet.Dot;

public class Koishi implements Dot.PosSprite {

	private final int f, s;

	private final Sprite.DSParam ds0, ds1;

	private Shape.PosShape sc, s0, s1;
	private Sprite.ESprite dc, d0, d1;

	private int time;

	public Koishi(Sprite.DSParam x0, Sprite.DSParam x1, int F, int T, int S) {
		ds0 = x0;
		ds1 = x1;
		f = F;
		s = S;
		time = T;

	}

	@Override
	public boolean active() {
		return true;
	}

	@Override
	public PosShape getShape() {
		return sc;
	}

	@Override
	public ESprite getSprite() {
		return dc;
	}

	@Override
	public void load(Dot d) {
		sc = s0 = ds0.getShape(d.pos);
		dc = d0 = ds0.getEntity(d);
		s1 = ds1.getShape(d.pos);
		d1 = ds1.getEntity(d);
	}

	@Override
	public void post() {
		if (time < s) {
			sc = s0;
			dc = d0;
		} else {
			sc = s1;
			dc = d1;
		}
	}

	@Override
	public double radius() {
		return Math.max(d0.radius(), d1.radius());
	}

	@Override
	public void update(int dt) {
		time += dt;
		time %= f;
	}

}
