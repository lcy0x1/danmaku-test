package battle.bullet;

import battle.Control;
import battle.Engine;
import battle.Shape;
import battle.Sprite;
import battle.entity.Bullet;
import util.FM;
import util.P;

public class BulletRing extends Dot {

	private class SubBullet extends Bullet implements Sprite.Dire, Control {

		private final BulletRing par;
		private final P pos;
		private final Sprite.ESprite esp;
		private final Shape.PosShape shape;
		private final int ind;

		private SubBullet(BulletRing br, int id, P p, Sprite.SParam sp) {
			par = br;
			ind = id;
			pos = p;
			esp = sp.getEntity(this);
			shape = sp.getShape(pos);
			addCtrl(this);
		}

		@Override
		public void draw() {
			if (finished())
				return;
			if (esp == null)
				return;
			if (pos.out(Engine.BOUND, esp.radius()))
				return;
			esp.draw();
		}

		@Override
		public boolean finished() {
			return par.move.out(pos, esp.radius());
		}

		@Override
		public double getDire() {
			return par.getDire(ind);
		}

		@Override
		public P getPos() {
			return pos;
		}

		@Override
		public Shape getShape() {
			return shape;
		}

		@Override
		public int getTime() {
			return par.getTime();
		}

	}

	private final SubBullet[] list;
	private final int n;
	private final P cen;

	public BulletRing(P c, Sprite.SParam sp, int num, Mover m) {
		super(c.copy(), sp, m);
		n = num;
		cen = c;
		list = new SubBullet[n];
		for (int i = 0; i < n; i++)
			Engine.RUNNING.add(list[i] = new SubBullet(this, i, cen.copy(), sp));
	}

	@Override
	public void draw() {
	}

	@Override
	public boolean finished() {
		for (SubBullet sb : list)
			if (!sb.isDead())
				return false;
		return true;
	}

	@Override
	public void post() {
		super.post();
		double d = cen.dis(pos);
		double a = cen.atan2(pos);
		for (int i = 0; i < n; i++)
			list[i].pos.setTo(P.polar(d, a + FM.PI * 2 / n * i).plus(cen));
	}

	private double getDire(int ind) {
		return getDire() + FM.PI * 2 * ind / n;
	}

}
