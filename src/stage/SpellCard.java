package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.entity.Clearer;
import battle.entity.Life;
import stage.StageSection.TimedStage;
import util.P;

public class SpellCard extends TimedStage {

	public static class LifeSpell extends SpellCard {

		private static final double DEF_HP = 10000;

		private final Life l;
		private final Dot dot;

		private boolean added = false;

		protected LifeSpell(int tot, P p, int boss) {
			super(tot, p, boss);
			l = new Life(DEF_HP, DEF_HP, dot = new Dot(p.copy(), Sprite.getBoss(boss)));
		}

		@Override
		public boolean finished() {
			return super.finished() || l.isDead();
		}

		public double getHP() {
			return l.health;
		}

		@Override
		public void post() {
			dot.tmp.setTo(dire.getPos());
			l.post();
			super.post();
		}

		@Override
		public void update(int dt) {
			super.update(dt);
			if (!added) {
				add(l);
				added = true;
			}
		}

	}

	public final P pos;
	public final StageSection.BossDire dire;
	private final int bs;
	private final Sprite.ESprite esp;

	protected SpellCard(int tot) {
		this(tot, pc.copy());
	}

	protected SpellCard(int tot, P p) {
		this(tot, p, -1);
	}

	protected SpellCard(int tot, P p, int boss) {
		super(tot);
		dire = new StageSection.BossDire(this, boss, pos = p);
		bs = boss;
		esp = Sprite.getDot(1, 0, 2, Sprite.L_BG).getEntity(dire);
	}

	@Override
	public void draw() {
		esp.draw();
	}

	@Override
	public BossProfile[] getBoss() {
		return new BossProfile[] { new BossProfile(bs, dire.getPos()) };
	}

	@Override
	public void post() {
		if (finished())
			add(new Clearer(pc, 0, 1, 1300, K_FINISH));
	}

}
