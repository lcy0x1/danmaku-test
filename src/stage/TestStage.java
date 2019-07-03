package stage;

import battle.Engine;
import battle.Sprite;
import battle.StageControl;
import battle.entity.Dot;
import battle.entity.DotBullet;
import util.P;

public class TestStage implements StageControl {

	private int time = 0;

	@Override
	public boolean finished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(int dt) {
		int f = 100;
		int rem = f - time % f;
		if (rem <= dt) {
			int ext = dt - rem;
			add(new P(400, 500), ext);
			add(new P(800, 500), ext);
			add(new P(0, 500), ext);
			add(new P(0, 0), ext);
		}
		time += dt;
	}

	private void add(P p, int ext) {
		Sprite.ESParam esp = new Sprite.ESParam(20201, 1, 1);
		Dot d = new Dot(p, P.polar(0.2, Math.PI / 2), esp);
		DotBullet b = new DotBullet(d);
		b.update(ext);
		Engine.RUNNING.add(b);
	}

}
