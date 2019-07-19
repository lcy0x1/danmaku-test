package battle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import battle.entity.Player;
import jogl.util.FakeGraphics;
import util.P;

public class Engine {

	public static class Time {

		public static abstract class Mask {

			private int len;

			public Mask(int l) {
				len = l;
			}

			public abstract double slow(Entity e);

		}

		private static class MaskExc extends Mask {

			private final double mult;

			private final Collection<Entity> ce;

			public MaskExc(double m, int l, Collection<Entity> c) {
				super(l);
				mult = m;
				ce = c;
			}

			@Override
			public double slow(Entity e) {
				if (ce == null || !ce.contains(e))
					return mult;
				return 1;
			}

		}

		private static class MaskInc extends Mask {

			private final double mult;

			private final Collection<Entity> ce;

			public MaskInc(double m, int l, Collection<Entity> c) {
				super(l);
				mult = m;
				ce = c;
			}

			@Override
			public double slow(Entity e) {
				if (e == null)
					return 1;
				if (ce == null || ce.contains(e))
					return mult;
				return 1;
			}

		}

		public int time, clock;

		private int t;

		private final List<Mask> mask = new ArrayList<>();
		private final List<Mask> temp = new ArrayList<>();

		/** slow all callers except ones in list */
		public void slowExc(double mult, int len, Collection<Entity> e) {
			temp.add(new MaskExc(mult, len, e));
		}

		public void slowExc(double mult, int len, Entity e) {
			List<Entity> l = new ArrayList<>();
			l.add(e);
			temp.add(new MaskExc(mult, len, l));
		}

		/** slow all callers in list, if list is null then slow all non-null */
		public void slowInc(double mult, int len, Collection<Entity> e) {
			temp.add(new MaskInc(mult, len, e));
		}

		private int dispach(Entity e) {
			double ans = t;
			for (Mask m : mask)
				ans *= m.slow(e);
			return (int) ans;
		}

		private void end() {
			time += dispach(null);
			clock += t;
			for (Mask m : mask)
				m.len -= t;
			mask.removeIf(m -> m.len <= 0);
			t = 0;
		}

		private void start(int dt) {
			mask.addAll(temp);
			temp.clear();
			t = dt;
		}

	}

	public static final P BOUND = new P(800, 1000), START = new P(400, 900);

	public static Engine RUNNING = null;
	public static Sprite.Pool RENDERING = null;

	public final Player pl;
	public final Time time;
	public final Control.UpdCtrl stage;
	public final Random r = new Random();

	private final Map<Integer, List<Entity>> entities = new TreeMap<>();
	private final List<Entity> temp = new ArrayList<>();
	private final List<Control.UpdCtrl> updc = new ArrayList<>();
	private final List<Control.UpdCtrl> utmp = new ArrayList<>();

	public Engine(Control.UpdCtrl sc) {
		pl = new Player();
		add(pl);
		time = new Time();
		stage = sc;
		add(stage);
	}

	public void add(Updatable e) {
		if (e instanceof Entity)
			temp.add((Entity) e);
		else if (e instanceof Control.UpdCtrl)
			utmp.add((Control.UpdCtrl) e);
	}

	public int count() {
		int[] c = new int[1];
		entities.values().forEach(l -> c[0] += l.size());
		return c[0];
	}

	public void draw(FakeGraphics fg) {
		RENDERING = new Sprite.Pool();
		entities.forEach((i, l) -> l.forEach(e -> e.draw()));
		RENDERING.flush(fg);
		RENDERING = null;
	}

	public void update(int t) {
		RUNNING = this;
		time.start(t);
		updc.forEach(e -> e.update(time.dispach(null)));
		entities.forEach((i, l) -> l.forEach(e -> e.update(time.dispach(e))));
		entities.forEach((i, l) -> {
			int atk = i >> 16 & 65535;
			if (atk == 0)
				return;
			entities.forEach((j, q) -> {
				int base = j & 65535;
				if ((atk & base) == 0)
					return;
				for (Entity e0 : l)
					for (Entity e1 : q)
						e0.attack(e1);
			});
		});
		updc.forEach(e -> e.post());
		entities.forEach((i, l) -> l.forEach(e -> e.post()));
		updc.removeIf(e -> e.finished());
		entities.forEach((i, l) -> l.removeIf(e -> e.isDead()));
		time.end();
		clearTmp();
		RUNNING = null;
	}

	private void clearTmp() {
		temp.forEach(e -> {
			int key = e.atk << 16 | e.base;
			List<Entity> l;
			if (entities.containsKey(key))
				l = entities.get(key);
			else
				entities.put(key, l = new ArrayList<Entity>());
			l.add(e);
		});
		temp.clear();
		utmp.forEach(e -> updc.add(e));
		utmp.clear();
	}

}
