package stage;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import battle.Engine;
import stage.StageSet.Spell;
import stage.StageSet.SpellSet;
import util.P;

public class StageSecs extends StageSection {

	public static class InterSection extends StageSection.TimedStage {

		private static class Transition extends BossProfile {

			private final StageSection.BossDire dire;

			private P p0, p1;

			private Transition(StageSection ss, int b, P p) {
				super(b, new P(0, 0));
				p0 = p;
				dire = new StageSection.BossDire(ss, id, pos);
			}

		}

		private static P getRandomPoint() {
			return P.polar(600, -rand(p2 / 2));
		}

		private final Map<Integer, Transition> trans = new TreeMap<>();

		private InterSection(BossProfile[] prev, BossProfile[] next) {
			super(TRANSITION_TIME);
			for (BossProfile bp : prev)
				trans.put(bp.id, new Transition(this, bp.id, bp.pos));
			for (BossProfile bp : next) {
				Transition t = null;
				if (trans.containsKey(bp.id))
					t = trans.get(bp.id);
				else
					trans.put(bp.id, t = new Transition(this, bp.id, null));
				t.p1 = bp.pos;
			}
		}

		@Override
		public void draw() {
			trans.forEach((i, t) -> t.dire.draw());
		}

		@Override
		public BossProfile[] getBoss() {
			return null;
		}

		@Override
		public void update(int dt) {
			super.update(dt);
			trans.forEach((i, t) -> {
				if (t.p0 == null)
					t.p0 = getRandomPoint();
				if (t.p1 == null)
					t.p1 = getRandomPoint();
				t.pos.setTo(t.p0.middleC(t.p1, 1.0 * time / length));
			});
		}

	}

	public static class SpellItr extends StageItr {

		private final SpellSet set;
		private final Iterator<Spell> itr;

		private StageSection prev = null, temp = null;

		public SpellItr(SpellSet ss, int diff) {
			super(diff);
			set = ss;
			itr = set.list.iterator();
		}

		@Override
		public boolean hasNext() {
			return temp != null || itr.hasNext();
		}

		@Override
		public StageSection next() {
			if (temp != null) {
				prev = temp;
				temp = null;
				return prev;
			}
			temp = itr.next().getStage(diff);
			BossProfile[] bp0 = prev == null ? new BossProfile[0] : prev.getBoss();
			return new InterSection(bp0, temp.getBoss());
		}

	}

	public static abstract class StageItr {

		public final int diff;

		public StageItr(int diffic) {
			diff = diffic;
		}

		public abstract boolean hasNext();

		public abstract StageSection next();

	}

	private static final int TRANSITION_TIME = 1500;

	private final StageItr list;

	private StageSection cur;

	public StageSecs(StageItr si) {
		list = si;
		cur = list.next();
	}

	@Override
	public void draw() {
		cur.draw();
	}

	@Override
	public boolean finished() {
		return !list.hasNext() && cur.finished();
	}

	@Override
	public BossProfile[] getBoss() {
		return null;
	}

	@Override
	public void post() {
		cur.post();
		if (finished())
			return;
		if (cur.finished()) {
			System.out.println("nexting");
			cur = list.next();
			Engine.RUNNING.clear();
		}
	}

	@Override
	public void update(int dt) {
		super.update(dt);
		cur.update(dt);

	}

}
