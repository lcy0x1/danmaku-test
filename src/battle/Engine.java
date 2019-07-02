package battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import battle.entity.Player;
import jogl.util.FakeGraphics;
import util.P;

public class Engine {

	public static class TimeDispach {

		public int time;

		private int t;

		private int dispach(Entity e) {
			return t;
		}

		private void end() {
			time += t;
			t = 0;
		}

		private void start(int dt) {
			t = dt;
		}

	}

	public static final P BOUND = new P(800, 1000), START = new P(400, 900);

	public static Engine RUNNING = null;
	public static Sprite.Pool RENDERING = null;

	public final Player pl;
	public final TimeDispach time;

	private final Map<Integer, List<Entity>> entities = new TreeMap<>();

	public Engine() {
		pl = new Player();
		time = new TimeDispach();
	}

	public void add(Entity e) {
		int key = e.atk << 16 | e.base;
		List<Entity> l;
		if (entities.containsKey(key))
			l = entities.get(key);
		else
			entities.put(key, l = new ArrayList<Entity>());
		l.add(e);
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
						e0.collide(e1);
			});
		});
		entities.forEach((i, l) -> l.forEach(e -> e.post()));
		entities.forEach((i, l) -> l.removeIf(e -> e.isDead()));
		time.end();
		RUNNING = null;
	}

}
