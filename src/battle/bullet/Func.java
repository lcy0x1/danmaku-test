package battle.bullet;

import java.util.ArrayList;
import java.util.List;

import util.P;

public interface Func {

	public static class VeloListFunc implements Func {

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
		public boolean exist(int time, int i) {
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
				return dest.get(i).copy().plus(v0, dt).plus(dv, dt * dt * 0.5 / t0);
			}
			return dest.get(n - 1).copy().plus(lp.get(n - 1), time - lt.get(n - 1));
		}

	}

	public boolean exist(int time, int i);

	public P func(int time, int i);

}
