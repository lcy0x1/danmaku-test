package battle;

public interface Control {

	public static class ElemCtrl<T> implements Control {

		private final MassCtrl<T> mass;

		private final T mast;

		public ElemCtrl(T master, MassCtrl<T> m) {
			mass = m;
			mast = master;
		}

		@Override
		public boolean finished() {
			return mass.finished(mast);
		}

	}

	public static class EntCtrl implements Control {

		private int hard;

		private boolean killed = false;

		public EntCtrl(int lv) {
			hard = lv;
		}

		@Override
		public boolean finished() {
			return killed;
		}

		public void killed(int t) {
			if (t >= hard)
				killed = true;
		}

		public void setLv(int lv) {
			hard = lv;
		}

	}

	public static interface MassCtrl<T> {

		public boolean finished(T t);

	}

	public static class TimeCtrl implements UpdCtrl {

		private int rem;

		public TimeCtrl(int tot) {
			rem = tot;
		}

		@Override
		public boolean finished() {
			return rem == 0;
		}

		@Override
		public void update(int t) {
			if (rem > 0) {
				rem -= t;
				if (rem < 0)
					rem = 0;
			}
		}

	}

	public static interface UpdCtrl extends Control, Updatable {

	}

	public static final int K_BULLET = 1, K_FUNCTIONAL = 2, K_FINISH = 3;

	public boolean finished();

}
