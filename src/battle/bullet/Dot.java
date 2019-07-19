package battle.bullet;

import battle.Control;
import battle.Shape;
import battle.Sprite;
import battle.Sprite.DSParam;
import battle.Sprite.ESprite;
import battle.Updatable;
import battle.bullet.Mover.CurveMover;
import battle.bullet.Mover.LineMover;
import battle.bullet.Mover.TimeMover;
import util.P;

public class Dot implements Sprite.Dire, Control.UpdCtrl {

	public static interface PosSprite extends Updatable {

		public boolean active();

		public Shape getShape();

		public Sprite.ESprite getSprite();

		public void load(Dot d);

		public double radius();

		@Override
		public default void update(int t) {
		}

	}

	private static class DefPS implements PosSprite {

		private DSParam dsp;

		private Sprite.ESprite sprite;
		private Shape shape;

		private DefPS(DSParam img) {
			dsp = img;
		}

		@Override
		public boolean active() {
			return !(shape instanceof Shape.NonShape);
		}

		@Override
		public Shape getShape() {
			return shape;
		}

		@Override
		public ESprite getSprite() {
			return sprite;
		}

		@Override
		public void load(Dot d) {
			if (dsp == null) {
				shape = new Shape.NonShape(d.pos);
				sprite = null;
			} else {
				shape = dsp.getShape(d.pos);
				sprite = dsp.getEntity(d);
			}
		}

		@Override
		public double radius() {
			return sprite == null ? Sprite.DEFRAD : sprite.radius();
		}

	}

	private static class DelayPS implements PosSprite {

		private final DSParam x0, x1;

		private Shape sc, s0;
		private Sprite.ESprite dc, d0;

		private int time;

		private DelayPS(int t0, DSParam r0, DSParam r1) {
			x0 = r0;
			x1 = r1;
			time = t0;
		}

		@Override
		public boolean active() {
			return !(sc instanceof Shape.NonShape);
		}

		@Override
		public Shape getShape() {
			return sc;
		}

		@Override
		public ESprite getSprite() {
			return dc;
		}

		@Override
		public void load(Dot d) {
			s0 = x0.getShape(d.pos);
			d0 = x0.getEntity(d);
			sc = new Shape.NonShape(d.pos);
			dc = x1.getEntity(d);
		}

		@Override
		public void post() {
			if (time < 0) {
				sc = s0;
				dc = d0;
			}
		}

		@Override
		public double radius() {
			return Math.max(d0.radius(), dc.radius());
		}

		@Override
		public void update(int t) {
			time -= t;
		}

	}

	public final P pos, tmp;
	public final PosSprite spr;

	public Mover move = null;

	private double dire = Double.NaN;
	private int time;

	public Dot(DSParam img, TimeMover tm) {
		this(tm.disp(0), img, tm);
	}

	/** curve with varying axial and constant angular speed */
	public Dot(P o, double ir, double ia, double v, double w, double aa, int t, DSParam img) {
		this(P.polar(ir, ia).plus(o), img, new CurveMover(ir, ia, v, w, aa, t));
	}

	/** curve with constant axial and angular speed */
	public Dot(P o, double ir, double ia, double v, double w, DSParam img) {
		this(P.polar(ir, ia).plus(o), img, new CurveMover(ir, ia, v, w));
	}

	/** static */
	public Dot(P p, DSParam img) {
		this(p, new DefPS(img));
	}

	/** custom mover */
	public Dot(P p, DSParam img, Mover m) {
		this(p, img);
		setMove(m);
	}

	/** delay */
	public Dot(P p, int t0, DSParam main, DSParam pre) {
		this(p, new DelayPS(t0, main, pre));
	}

	/** linear varying speed */
	public Dot(P p, P v, double a, int t, DSParam img) {
		this(p, img, new LineMover(v, a, 0, t));
	}

	/** linear varying speed */
	public Dot(P p, P v, double a, int t0, int t1, DSParam img) {
		this(p, img, new LineMover(v, a, t0, t1));
	}

	/** linear constant speed */
	public Dot(P p, P v, DSParam img) {
		this(p, img, new LineMover(v));
	}

	/** semi-linear */
	public Dot(P p, P v, P a, int t, DSParam img) {
		this(p, img, new LineMover(v, a, t));
	}

	/** custom sprite */
	public Dot(P p, PosSprite ps) {
		pos = p;
		tmp = p.copy();
		spr = ps;
		spr.load(this);
	}

	public Dot delay(int t) {
		time -= t;
		return this;
	}

	@Override
	public boolean finished() {
		if (move == null)
			return false;
		return move.out(pos, spr.radius());
	}

	@Override
	public double getDire() {
		return Double.isNaN(dire) ? 0 : dire;
	}

	@Override
	public P getPos() {
		return pos;
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public void post() {
		double d = move == null ? Double.NaN : move.getDire();
		if (Double.isNaN(d))
			if (pos.dis(tmp) > 0)
				d = pos.atan2(tmp);
		if (!Double.isNaN(d))
			dire = d;
		pos.setTo(tmp);
		spr.post();
	}

	public Dot setMove(Mover m) {
		move = m;
		double d = m.getDire();
		if (!Double.isNaN(d))
			dire = d;
		return this;
	}

	public Dot setMove(P v) {
		return setMove(new LineMover(v));
	}

	public Dot setMove(P v, double a, int t0, int t1) {
		return setMove(new LineMover(v, a, t0, t1));
	}

	public Dot setMove(P v, P a, int t0, int t1) {
		return setMove(new LineMover(v, a, t0, t1));
	}

	@Override
	public void update(int t) {
		tmp.setTo(pos);
		if (time >= 0 && move != null)
			move.update(this, t);
		spr.update(t);
		time += t;
	}

}
