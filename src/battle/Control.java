package battle;

public interface Control {

	public static interface EntCtrl extends UpdCtrl {

		public void killed(Entity e);

	}

	public static interface MoveCtrl extends Control {

	}

	public static class TimeCtrl implements EntCtrl, UpdCtrl {

		public MoveCtrl move;

		private int rem;

		private boolean killed = false;

		public TimeCtrl() {
			rem = -1;
		}

		public TimeCtrl(int tot) {
			rem = tot;
		}

		@Override
		public boolean finished() {
			if (move != null && move.finished())
				return true;
			if (rem == 0)
				return true;
			if (killed)
				return true;
			return false;// TODO
		}

		@Override
		public void killed(Entity e) {

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

	public boolean finished();

}
