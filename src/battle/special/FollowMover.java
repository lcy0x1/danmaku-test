package battle.special;

import java.util.List;

import battle.Engine;
import battle.Entity;
import battle.Shape;
import battle.bullet.Dot;
import battle.bullet.Mover;
import util.P;

public abstract class FollowMover implements Mover {

	private static class FollowClose extends FollowMover {

		private final int tar;

		public FollowClose(P v, double w, int targ) {
			super(v, w);
			tar = targ;
		}

		@Override
		protected P getTarget(P p) {
			List<Entity> l = Engine.RUNNING.getReceiver(tar);
			P p0 = null;
			for (Entity e : l) {
				Shape s = e.getShape();
				if (s == null || !(s instanceof Shape.PosShape))
					continue;
				P p1 = ((Shape.PosShape) s).pos;
				if (p0 == null || p.dis(p0) > p.dis(p1))
					p0 = p1;
			}
			return p0;
		}

	}

	private static class FollowTarget extends FollowMover {

		private final P tar;

		public FollowTarget(P v, double w, P targ) {
			super(v, w);
			tar = targ;
		}

		@Override
		protected P getTarget(P p) {
			return tar;
		}

	}

	public static FollowMover getMover(P v, double w, int t) {
		return new FollowClose(v, w, t);
	}

	public static FollowMover getMover(P v, double w, P t) {
		return new FollowTarget(v, w, t);
	}

	private final P pv;
	private final double mw;

	public FollowMover(P v, double w) {
		pv = v;
		mw = w;
	}

	@Override
	public boolean out(P pos, double r) {
		return pos.moveOut(pv, Engine.BOUND, r);
	}

	@Override
	public void update(Dot d, int t) {
		d.tmp.plus(pv, t);
		P tar = getTarget(d.tmp);
		if (tar == null)
			return;
		double a0 = d.tmp.atan2(tar);
		double da = (pv.atan2() - a0 + Math.PI * 5) % (Math.PI * 2) - Math.PI;
		if (Math.abs(da) < mw * t)
			pv.rotate(da);
		else
			pv.rotate(-mw * t * Math.signum(da));
	}

	protected abstract P getTarget(P p);

}
