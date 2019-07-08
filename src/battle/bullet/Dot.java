package battle.bullet;

import battle.Control;
import battle.Shape;
import battle.Sprite.DSParam;
import battle.Sprite;
import battle.bullet.Mover.*;
import util.P;

public class Dot implements Sprite.DotESprite.Dire, Control.UpdCtrl {

	public final P pos, tmp;

	public Sprite.DotESprite sprite;
	public Shape.PosShape shape;
	private double dire;
	public Mover move = null;

	/** curve with varying axial and constant angular speed */
	public Dot(P o, double ir, double ia, double v, double w, double aa, int t, DSParam img) {
		this(P.polar(ir, ia).plus(o), img, new CurveMover(ir, ia, v, w, aa, t));
	}

	/** curve with constant axial and angular speed */
	public Dot(P o, double ir, double ia, double v, double w, DSParam img) {
		this(P.polar(ir, ia).plus(o), img, new CurveMover(ir, ia, v, w));
	}

	/** linear varying speed */
	public Dot(P p, P v, double a, int t0, int t1, DSParam img) {
		this(p, img, new LineMover(a, t0, t1, v));
	}

	/** linear varying speed */
	public Dot(P p, P v, double a, int t, DSParam img) {
		this(p, img, new LineMover(a, 0, t, v));
	}

	/** semi-linear */
	public Dot(P p, P v, P a, int t, DSParam img) {
		this(p, img, new LineMover(a, t, v));
	}

	/** linear constant speed */
	public Dot(P p, P v, DSParam img) {
		this(p, img, new LineMover(v));
	}

	/** static */
	public Dot(P p, DSParam img) {
		pos = p;
		tmp = p.copy();
		if (img == null) {
			shape = null;
			sprite = null;
		} else {
			shape = img.getShape(pos);
			sprite = img.getEntity(this);
		}
	}

	public Dot(P p, DSParam img, Mover m) {
		this(p, img);
		setMove(m);
	}

	public Dot(DSParam img, TimeMover tm) {
		this(tm.disp(0), img, tm);
	}

	@Override
	public boolean finished() {
		if (move == null)
			return false;
		return move.out(pos, sprite == null ? Sprite.DEFRAD : sprite.radius());
	}

	@Override
	public double getDire() {
		return dire;
	}

	@Override
	public P getPos() {
		return pos;
	}

	@Override
	public void post() {
		if (pos.dis(tmp) > 0)
			dire = pos.atan2(tmp);
		pos.setTo(tmp);
	}

	public Dot setMove(Mover m) {
		move = m;
		return this;
	}

	@Override
	public void update(int t) {
		tmp.setTo(pos);
		if (move != null)
			move.update(this, t);
	}

}
