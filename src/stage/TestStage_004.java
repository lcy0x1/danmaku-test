package stage;

import battle.Engine;
import battle.Sprite;
import battle.entity.Dot;
import battle.entity.DotBullet;

public class TestStage_004 extends SpellCard {

	public TestStage_004(int diff) {
		super(60000);
	}

	@Override
	public void update(int dt) {
		if (time == 0)
			Engine.RUNNING.add(new DotBullet(new Dot(pc, new Sprite.DESParam(30100, 0, 1))));
		super.update(dt);
	}

}
