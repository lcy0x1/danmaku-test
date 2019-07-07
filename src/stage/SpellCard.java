package stage;

import battle.Control;
import battle.Engine;
import battle.Updatable;
import battle.entity.Clearer;
import util.P;

public class SpellCard implements Control.UpdCtrl {

	public static final P o = Engine.BOUND;
	public static final P pc = new P(o.x / 2, o.y / 2);

	public static final double p2 = Math.PI * 2;

	public static void add(Updatable e) {
		Engine.RUNNING.add(e);
	}

	public static void add(Updatable e, int ex) {
		e.update(ex);
		Engine.RUNNING.add(e);
	}

	public static double rand(double a) {
		return Engine.RUNNING.r.nextDouble() * a;
	}

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
			add(new Clearer(pc, 0, 1, 1000, K_BULLET));
			add(new Clearer(pc, 0, 0.75, 1000, K_FUNCTIONAL));
			add(new Clearer(pc, 0, 0.5, 1000, K_FINISH));
		}
	}

	@Override
	public void update(int dt) {
		time += dt;
	}

}
