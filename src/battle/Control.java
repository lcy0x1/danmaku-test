package battle;

public interface Control {

	public static abstract class EntCtrl implements UpdCtrl {

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

	public static interface MoveCtrl extends Control {

	}

	public static class TimeCtrl extends EntCtrl {

		public MoveCtrl move;

		private int rem;

		public TimeCtrl() {
			super(0);
			rem = -1;
		}

		public TimeCtrl(int tot) {
			super(0);
			rem = tot;
		}

		public TimeCtrl(int tot, int lv) {
			super(lv);
			rem = tot;
		}

		@Override
		public boolean finished() {
			if (super.finished())
				return true;
			if (move != null && move.finished())
				return true;
			if (rem == 0)
				return true;
			return false;
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
