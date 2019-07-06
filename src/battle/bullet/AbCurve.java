package battle.bullet;

import battle.Control;
import battle.Shape;
import battle.Sprite;
import util.P;

public abstract class AbCurve extends Shape.LineSegs implements Control.UpdCtrl {

	public interface Func {

		public P func(int time, int i);

		public boolean exist(int time, int i);

	}

	public class FuncCurve extends AbCurve {

		private final Func func;

		private int time = 0, dt = 0;

		public FuncCurve(Func f, int num, Sprite.CESParam para) {
			super(num, para);
			func = f;
		}

		@Override
		public P[][] getPos() {
			int[][] seg = countSeg();
			P[][] ans = new P[seg.length][];
			for (int i = 0; i < seg.length; i++) {
				ans[i] = new P[seg[i][1]];
				for (int j = 0; j < seg[i][1]; j++)
					ans[i][j] = func.func(time, seg[i][0] + j);
			}
			return ans;
		}

		@Override
		public boolean finished() {
			return count() == 0;
		}

		@Override
		protected int count() {
			int ans = 0;
			for (int i = 0; i < n; i++)
				if (exist(i))
					ans++;
			return ans;
		}

		@Override
		protected int[][] countSeg() {
			int n = 0;
			boolean ctn = false;
			for (int i = 0; i < n; i++)
				if (exist(i)) {
					if (!ctn) {
						n++;
						ctn = true;
					}
				} else
					ctn = false;

			int[][] ans = new int[n][2];
			int a = -1;
			ctn = false;
			for (int i = 0; i < n; i++)
				if (exist(i)) {
					if (!ctn) {
						a++;
						ctn = true;
						ans[a][0] = i;
					}
					ans[a][1]++;
				} else
					ctn = false;
			return ans;
		}

		@Override
		protected boolean exist(int i) {
			if (!super.exist(i))
				return false;
			return func.exist(time, i);
		}

		@Override
		public void update(int t) {
			dt = t;
		}

		@Override
		public void post() {
			time += dt;
		}

	}

	public final Sprite.CurveESprite sprite;

	protected final int n;

	public AbCurve(int num, Sprite.CESParam cesp) {
		super(cesp.r);
		n = num;
		sprite = cesp.getEntity(this);
	}

	protected abstract int count();

	protected abstract int[][] countSeg();

	protected boolean exist(int t) {
		return true;
	}

}
