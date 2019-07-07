package battle.entity;

import battle.Control;

public class Emiter implements Control.UpdCtrl {

	public static interface Ticker {

		public void tick(Emiter e, int it, int ex);

	}

	public final int id;

	private final Control ctrl;
	private final Ticker tick;
	private final int f;

	private final Control.TimeCtrl tc;

	public int time;

	public Emiter(int ID, int freq, Control lc, Ticker tic) {
		id = ID;
		ctrl = lc;
		f = freq;
		tick = tic;
		tc = null;
	}

	public Emiter(int ID, int freq, int time, Ticker tic) {
		id = ID;
		ctrl = tc = new Control.TimeCtrl(time);
		f = freq;
		tick = tic;
	}

	@Override
	public boolean finished() {
		return ctrl.finished();
	}

	@Override
	public void update(int dt) {
		if (tc != null)
			tc.update(dt);
		int rem = f - (time + f - 1) % f - 1;
		if (rem < dt) {
			int it = (time + rem) / f;
			System.out.println(it+","+rem);//TODO
			tick.tick(this, it, rem);
		}
		time += dt;
	}

}
