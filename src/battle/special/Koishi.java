package battle.special;

import battle.Control;
import battle.Shape;
import battle.Sprite;
import battle.bullet.Dot;
import battle.bullet.DotBullet;

public class Koishi implements Control.UpdCtrl {

	private final int f, s;
	private final Dot d;
	private final DotBullet b;

	private final Shape.PosShape s0, s1;
	private final Sprite.ESprite d0, d1;

	private int time;

	public Koishi(DotBullet db, Sprite.DSParam des, int F, int T, int S) {
		b = db;
		d = db.dot;
		f = F;
		s = S;
		time = T;
		s0 = d.shape;
		d0 = d.sprite;
		s1 = des.getShape(d.pos);
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
