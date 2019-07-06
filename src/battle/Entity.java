package battle;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity implements Updatable {

	public static final int C_ENEMY = 1, C_PLAYER = 2, C_BULLET = 4, C_PATK = 8;

	protected final int base, atk;
	private final List<Control> ctrls = new ArrayList<Control>();
	private final Control.EntCtrl ent = new Control.EntCtrl(0);

	private Entity[] trails;

	public Entity(int i0, int i1) {
		base = i0;
		atk = i1;
		addCtrl(ent);
	}

	public final void addCtrl(Control c) {
		ctrls.add(c);
	}

	public final Control.EntCtrl getEntCtrl() {
		return ent;
	}

	public abstract Shape getShape();

	public final boolean isDead() {
		for (Control c : ctrls)
			if (c.finished())
				return true;
		return false;
	}

	@Override
	public void post() {
		for (Control c : ctrls)
			if (c instanceof Updatable)
				((Updatable) c).post();
		if (isDead() && trails != null)
			for (Entity e : trails)
				Engine.RUNNING.add(e);
	}

	public Entity setLv(int lv) {
		ent.setLv(lv);
		return this;
	}

	public final void trail(Entity... es) {
		trails = es;
	}

	/** main entrance of timer. this implementation updates control only */
	@Override
	public void update(int t) {
		for (Control c : ctrls)
			if (c instanceof Updatable)
				((Updatable) c).update(t);
	}

	/** this entity attacks the entity e */
	protected abstract void attack(Entity e);

	protected abstract void draw();

}
