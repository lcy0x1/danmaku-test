package stage;

import battle.Control;
import battle.Engine;
import battle.Sprite;
import battle.Updatable;
import battle.entity.Player;
import util.FM;
import util.P;

public abstract class StageSection extends FM implements Control.UpdCtrl {

	public static class BossDire implements Sprite.Dire {

		private final StageSection ss;
		private final Sprite.ESprite esp;
		private final P pos;

		public BossDire(StageSection sec, int id, P p) {
			esp = Sprite.getBoss(id).getEntity(this);
			pos = p;
			ss = sec;
		}

		public void draw() {
			esp.draw();
		}

		@Override
		public double getDire() {
			return 0;
		}

		@Override
		public P getPos() {
			return pos;
		}

		@Override
		public int getTime() {
			return ss.time;
		}

	}

	public static class BossProfile implements Comparable<BossProfile> {

		public final int id;

		public final P pos;

		public BossProfile(int n, P p) {
			id = n;
			pos = p;
		}

		@Override
		public int compareTo(BossProfile o) {
			return Integer.compare(id, o.id);
		}

	}

	public static abstract class TimedStage extends StageSection {

		public final int length;

		protected TimedStage(int tot) {
			length = tot;
		}

		@Override
		public boolean finished() {
			return time > length;
		}

	}

	public static final P o = Engine.BOUND;
	public static final P pc = new P(o.x / 2, o.y / 2);

	public static final double p2 = FM.PI * 2;

	public static void add(Updatable e) {
		Engine.RUNNING.add(e);
	}

	public static void add(Updatable e, int ex) {
		if (ex > 0)
			e.update(ex);
		Engine.RUNNING.add(e);
	}

	public static Player getPlayer() {
		return Engine.RUNNING.pl;
	}

	public static double rand(double a) {
		return Engine.RUNNING.r.nextDouble() * a;
	}

	public int time;

	public abstract BossProfile[] getBoss();

	@Override
	public void update(int dt) {
		time += dt;
	}

}
