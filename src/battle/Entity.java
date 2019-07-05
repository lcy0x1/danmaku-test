package battle;

public abstract class Entity implements Updatable {

	public static final int C_ENEMY = 1, C_PLAYER = 2, C_BULLET = 4, C_PATK = 8;

	protected final int base, atk;

	private Entity[] trails;

	public Entity(int i0, int i1) {
		base = i0;
		atk = i1;
	}

	public abstract Control.EntCtrl getCtrl();

	public abstract Shape getShape();

	public boolean isDead() {
		return getCtrl() != null && getCtrl().finished();
	}

	@Override
	public void post() {
		if (getCtrl() != null && getCtrl().finished() && trails != null)
			for (Entity e : trails)
				Engine.RUNNING.add(e);
	}

	public void trail(Entity... es) {
		trails = es;
	}

	/** main entrance of timer. this implementation updates control only */
	@Override
	public void update(int t) {
		if (getCtrl() != null)
			getCtrl().update(t);
	}

	/** this entity attacks the entity e */
	protected abstract void attack(Entity e);

	protected abstract void draw();

}
