package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import util.P;

public class Engine {

	public static final P BOUND = new P(800, 1000);

	public int time;

	private final Map<Integer, List<Entity>> entities = new TreeMap<>();

	public void add(Entity e) {
		int key = e.atk << 16 | e.base;
		List<Entity> l;
		if (entities.containsKey(key))
			l = entities.get(key);
		else
			entities.put(key, l = new ArrayList<Entity>());
		l.add(e);
	}

	public void update(int t) {
		entities.forEach((i, l) -> l.forEach(e -> e.update(t)));
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
	}

}
