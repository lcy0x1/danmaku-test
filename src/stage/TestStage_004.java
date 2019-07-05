package stage;

import battle.Engine;
import battle.Sprite;
import battle.Control;
import battle.entity.Dot;
import battle.entity.DotBullet;
import util.P;

public class TestStage_004 implements Control.UpdCtrl {

	private static final P o = Engine.BOUND;
	private int time = 0;

	@Override
	public boolean finished() {
		return time > 50000;
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			Engine.RUNNING.add(new DotBullet(new Dot(new P(o.x / 2, o.y / 2), new Sprite.DESParam(30100, 0, 1))));
		time += dt;

	}

}
