package battle.bullet;

import java.util.ArrayList;
import java.util.List;

import battle.Engine;
import battle.Sprite;
import battle.bullet.Curve.ListCurve;
import battle.bullet.Mover.FuncMover;
import battle.entity.Emiter;
import util.FM;
import util.P;

public interface Func {

	public static class Adder implements Emiter.Ticker {

		private final P pos;
		private final ListCurve c;
		private final Func vlf;
		private final double a;
		private final Sprite.SParam sp;
		private final int lt;

		public Adder(P p, ListCurve lc, Func vlfs, double a0, Sprite.SParam spr, int t) {
			pos = p;
			c = lc;
			vlf = vlfs;
			a = a0;
			sp = spr;
			lt = t;
		}

		@Override
		public void tick(Emiter e, int it, int ex) {
			Dot d = new Dot(pos.copy(), sp, new FuncMover(vlf, 0, it, a));
			DotBullet b = new DotBullet(d, lt);
			if (c != null)
				c.addP(b);
			Engine.RUNNING.add(b);
		}

	}

	public static class QuadVLF extends VeloListFunc {

		@Override
		protected double getTX(int dt, int t0) {
			return dt * dt * 0.5 / t0;
		}

	}

	public static class TrigVLF extends VeloListFunc {

		@Override
		protected double getTX(int dt, int t0) {
			return (dt - t0 / FM.PI * FM.sin(FM.PI * dt / t0)) / 2;
		}

	}

	public static abstract class VeloListFunc implements Func {

		private final List<Integer> lt = new ArrayList<Integer>();
		private final List<P> lp = new ArrayList<P>();
		private final List<P> dest = new ArrayList<P>();

		private int n = 0;

		public VeloListFunc() {
			lp.add(new P(0, 0));
			dest.add(new P(0, 0));
			lt.add(0);
			n++;
		}

		public void add(P v, int time) {
			lp.add(v);
			lt.add(lt.get(n - 1) + time);
			dest.add(lp.get(n - 1).copy().plus(v).times(0.5 * time).plus(dest.get(n - 1)));
			n++;
		}

		@Override
		public boolean exist(P pos, double r, int time, int i) {
			return true;
		}

		@Override
		public P func(int time, int ind) {
			int i = 0;
			while (i + 1 < n && lt.get(i + 1) < time)
				i++;
			if (i + 1 < n) {
				int dt = time - lt.get(i);
				int t0 = lt.get(i + 1) - lt.get(i);
				P v0 = lp.get(i);
				P dv = v0.sf(lp.get(i + 1));
				return dest.get(i).copy().plus(v0, dt).plus(dv, getTX(dt, t0));
			}
			return dest.get(n - 1).copy().plus(lp.get(n - 1), time - lt.get(n - 1));
		}

		protected abstract double getTX(int dt, int t0);

	}

	public boolean exist(P pos, double r, int time, int i);

	public P func(int time, int i);

}
