package battle;

public class LifeControl {

	public interface MoveControl {

		public boolean out();

	}

	public MoveControl move;

	private int rem;

	private boolean killed = false;

	public LifeControl() {
		rem = -1;
	}

	public LifeControl(int tot) {
		rem = tot;
	}

	public boolean isDead() {
		if (move != null && move.out())
			return true;
		if (rem == 0)
			return true;
		if (killed)
			return true;
		return false;// TODO
	}

	public void kill(Entity e) {

	}

	public void update(int t) {
		if (rem > 0) {
			rem -= t;
			if (rem < 0)
				rem = 0;
		}
	}

}
