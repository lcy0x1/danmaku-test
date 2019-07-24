package stage;

import battle.Sprite;
import battle.bullet.Dot;
import battle.entity.Life;
import stage.StageSection.TimedStage;
import util.P;

public class SpellCard extends TimedStage implements Sprite.Dire {

	public static class BossSpell extends SpellCard {

		private final Life l;
		private final Dot dot;

		protected BossSpell(int tot, P p, Sprite.DSParam sp) {
			super(tot, p);
			l = new Life(tot, tot, dot = new Dot(p.copy(), sp));
		}

		@Override
		public boolean finished() {
			return super.finished() || l.isDead();
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
			l.update(dt);
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
		esp = Sprite.getDot(1, 0, Sprite.BG).getEntity(this);
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
