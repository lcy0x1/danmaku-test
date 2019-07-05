package battle.special;

import battle.Control;
import battle.Shape;
import battle.Sprite;
import battle.entity.Dot;
import battle.entity.DotBullet;

public class Koishi implements Control.UpdCtrl {

	private final int f, s;
	private final Dot d;
	private final DotBullet b;

	private final Shape.PosShape s0, s1;
	private final Sprite.DotESprite d0, d1;

	private int time;

	public Koishi(DotBullet db, Sprite.DESParam des, int F, int T, int S) {
		b = db;
		d = db.dot;
		f = F;
		s = S;
		time = T;
		s0 = d.shape;
		d0 = d.sprite;
		s1 = new Shape.Circle(d.pos, des.r);
		d1 = des.getEntity(d);
	}

	@Override
	public boolean finished() {
		return b.isDead();
	}

	@Override
	public void post() {
		if (time < s) {
			d.shape = s0;
			d.sprite = d0;
		} else {
			d.shape = s1;
			d.sprite = d1;
		}
	}

	@Override
	public void update(int dt) {
		time += dt;
		time %= f;
	}

}
