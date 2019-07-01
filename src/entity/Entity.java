package entity;

public abstract class Entity {

	public static final int C_ENEMY = 1, C_PLAYER = 2, C_BULLET = 4, C_PATK = 8;

	protected final int base, atk;

	public Entity(int i0, int i1) {
		base = i0;
		atk = i1;
	}

	/** this entity attacks the entity e */
	protected abstract void collide(Entity e);

	protected abstract boolean isDead();

	protected abstract void post();

	protected abstract void update(int t);

}
