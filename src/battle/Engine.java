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

		private static class Mask {

			private final double mult;

			private int len;

			private final Collection<Entity> ce;

			public Mask(double m, int l, Collection<Entity> c) {
				mult = m;
				len = l;
				ce = c;
			}

		}

		public int time, clock;

		private int t;

		private final List<Mask> mask = new ArrayList<>();
		private final List<Mask> temp = new ArrayList<>();

		public void slow(double mult, int len, Collection<Entity> e) {
			temp.add(new Mask(mult, len, e));
		}

		private int dispach(Entity e) {
			double ans = t;
			for (Mask m : mask)
				if (m.ce == null || !m.ce.contains(e))
					ans *= m.mult;
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

	public void add(Control.UpdCtrl e) {
		utmp.add(e);
	}

	public void add(Entity e) {
		temp.add(e);
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
		clearTmp();
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
