package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.entity.Life;
import stage.StageSection.TimedStage;
import util.P;

public class SpellCard extends TimedStage implements Sprite.Dire {

	public static class BossSpell extends SpellCard {

		private static final double DEF_HP = 10000;

		private final Life l;
		private final Dot dot;

		private boolean added = false;

		protected BossSpell(int tot, P p, int boss) {
			super(tot, p);
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
			dot.tmp.setTo(pos);
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

	private final Sprite.ESprite esp;

	protected SpellCard(int tot) {
		this(tot, pc.copy());
	}

	protected SpellCard(int tot, P p) {
		super(tot);
		pos = p;
		esp = Sprite.getDot(1, 0, 2, Sprite.L_BG).getEntity(this);
	}

	@Override
	public void draw() {
		esp.draw();
	}

	@Override
	public BossProfile[] getBossPos() {
		return new BossProfile[] { new BossProfile(0, pos) };
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
		return time;
	}

}
