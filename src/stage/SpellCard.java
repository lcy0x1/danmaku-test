package stage;

import battle.Control;
import battle.Engine;
import battle.entity.Clearer;
import util.P;

public class SpellCard implements Control.UpdCtrl {

	public static final P o = Engine.BOUND;
	public static final P pc = new P(o.x / 2, o.y / 2);

	public static final double p2 = Math.PI * 2;

	public int time;

	private final int length;

	protected SpellCard(int tot) {
		length = tot;
	}

	@Override
	public boolean finished() {
		return time > length;
	}

	@Override
	public void post() {
		if (finished()) {
			Engine.RUNNING.add(new Clearer(pc, 0, 1, 1000, K_BULLET));
			Engine.RUNNING.add(new Clearer(pc, 0, 0.75, 1000, K_FUNCTIONAL));
			Engine.RUNNING.add(new Clearer(pc, 0, 0.5, 1000, K_FINISH));
		}
	}

	@Override
	public void update(int dt) {
		time += dt;
	}

}
