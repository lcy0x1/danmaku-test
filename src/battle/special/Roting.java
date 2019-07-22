package battle.special;

import battle.Engine;
import battle.bullet.Mover;
import util.P;

public class Roting extends Mover.TimeMover {

	private final P c, v;
	private final double w, a, l;

	public Roting(P cen, P v0, double w0, double a0, double r0) {
		c = cen;
		v = v0;
		w = w0;
		a = a0;
		l = r0;
	}

	@Override
	public P disp(int t) {
		return P.polar(l, w * t + a).plus(v, t).plus(c);
	}

	@Override
	public boolean out(P pos, double r) {
		return P.polar(v.abs() * time - l, v.atan2()).plus(c).moveOut(v, Engine.BOUND, r);
	}

}