package battle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import battle.entity.Player;
import jogl.util.FakeGraphics;
import stage.StageSection;
import util.FM;
import util.P;

public class Engine {

	public static class StartProfile {

		private final StageSection upd;
		private final P pos;
		private final int t0;

		public StartProfile(StageSection sc) {
			upd = sc;
			pos = START;
			t0 = 0;

		}

		public Engine getInstance() {
			return new Engine(upd, new Player(pos, t0));
		}

	}

	public static class Time implements Updatable {

		public static abstract class Mask {

			public int len;

			public Mask(int l) {
				len = l;
			}

			public abstract double slow(Updatable e);

		}

		private static class MaskExc extends Mask {

			private final double mult;

			private final Collection<Updatable> ce;

			public MaskExc(double m, int l, Collection<Updatable> c) {
				super(l);
				mult = m;
				ce = c;
			}

			@Override
			public double slow(Updatable e) {
				if (ce.contains(e))
					return 1;
				return mult;
			}

		}

		private static class MaskInc extends Mask {

			private final double mult;

			private final Collection<Updatable> ce;

			public MaskInc(double m, int l, Collection<Updatable> c) {
				super(l);
				mult = m;
				ce = c;
			}

			@Override
			public double slow(Updatable e) {
				if (ce.contains(e))
					return mult;
				return 1;
			}

		}

		public int time, clock;

		private int t;

		private final List<Mask> mask = new ArrayList<>();
		private final List<Mask> temp = new ArrayList<>();

		@Override
		public void post() {
			time += dispach(this);
			clock += t;
			for (Mask m : mask)
				m.len -= t;
			mask.removeIf(m -> m.len <= 0);
			t = 0;
		}

		public void slow(Mask m) {
			temp.add(m);
		}

		public void slowExc(double mult, int len, Updatable... es) {
			Set<Updatable> l = new HashSet<>();
			for (Updatable e : es)
				l.add(e);
			l.add(this);
			temp.add(new MaskExc(mult, len, l));
		}

		/** slow all callers except ones in list */
		public void slowExcC(double mult, int len, Collection<Updatable> e) {
			temp.add(new MaskExc(mult, len, e));
		}

		public void slowInc(double mult, int len, Updatable... es) {
			Set<Updatable> l = new HashSet<>();
			for (Updatable e : es)
				l.add(e);
			temp.add(new MaskInc(mult, len, l));
		}

		/** slow all callers in list, if list is null then slow all non-null */
		public void slowIncC(double mult, int len, Collection<Updatable> e) {
			temp.add(new MaskInc(mult, len, e));
		}

		@Override
		public void update(int dt) {
			mask.addAll(temp);
			temp.clear();
			t = dt;
		}

		private int dispach(Updatable e) {
			double ans = t;
			for (Mask m : mask)
				ans *= m.slow(e);
			return (int) ans;
		}

	}

	public static class UpdateProfile {

		private final P pos;
		private final int time;
		private final boolean attack;

		public UpdateProfile(P p, int t, boolean atk) {
			pos = p;
			time = t;
			attack = atk;
		}

	}

	public static final P BOUND = new P(800, 1000), START = new P(400, 900);

	public static final int CLEAR_POINTS = 1;

	public static Engine RUNNING = null;
	public static Sprite.Pool RENDERING = null;

	public final Player pl;
	public final Time time;
	public final StageSection stage;
	public final Random r = new Random();

	public int drawLoad = 0;

	private final Map<Integer, List<Entity>> entities = new TreeMap<>();
	private final List<Entity> temp = new ArrayList<>();
	private final List<Control.UpdCtrl> updc = new ArrayList<>();
	private final List<Control.UpdCtrl> utmp = new ArrayList<>();

	private int clearMark = -1;

	private Engine(StageSection sc, Player p) {
		pl = p;
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

	public void clear(int type) {
		clearMark = type;
	}

	public int count() {
		int[] c = new int[1];
		entities.values().forEach(l -> c[0] += l.size());
		return c[0];
	}

	public void draw(FakeGraphics fg) {
		RENDERING = new Sprite.Pool();
		updc.forEach(e -> e.draw());
		entities.forEach((i, l) -> l.forEach(e -> e.draw()));
		drawLoad = RENDERING.count();
		RENDERING.flush(fg);
		RENDERING = null;
	}

	public List<Entity> getReceiver(int base) {
		List<Entity> ans = new ArrayList<>();
		entities.forEach((i, l) -> {
			if ((i & base) > 0)
				ans.addAll(l);
		});
		return ans;
	}

	public void update(UpdateProfile up) {
		FM.clear(false);
		RUNNING = this;
		pl.ext.plus(up.pos).limit(BOUND);
		pl.atker.attack = up.attack;
		time.update(up.time);

		updc.forEach(e -> e.update(time.dispach(e)));
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
		time.post();
		clearTmp();
		RUNNING = null;
		FM.clear(false);
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
		updc.addAll(utmp);
		utmp.clear();
		if (clearMark > 0) {
			clearMark = -1;
			updc.removeIf(e -> e != stage);
			entities.forEach((i, l) -> {
				if ((i & (Entity.C_PATK | Entity.C_PLAYER | Entity.C_CLEARER)) == 0)
					l.forEach(e -> e.getEntCtrl().killed(Control.K_FINISH));
			});
		}
	}

}
