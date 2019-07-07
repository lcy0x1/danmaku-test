package battle.bullet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import battle.Control;
import battle.Shape;
import battle.Sprite;
import util.P;

public abstract class AbCurve extends Shape.LineSegs implements Control.UpdCtrl {

	public static class FuncCurve extends AbCurve {

		private final Func func;

		private int time = 0, dt = 0;

		private final int n;

		public FuncCurve(Func f, int num, Sprite.SParam para) {
			super(para);
			n = num;
			func = f;
		}

		@Override
		public boolean finished() {
			return count() == 0;
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
		public void post() {
			time += dt;
		}

		@Override
		public void update(int t) {
			dt = t;
		}

		private int count() {
			int ans = 0;
			for (int i = 0; i < n; i++)
				if (exist(i))
					ans++;
			return ans;
		}

		private int[][] countSeg() {
			int x = 0;
			boolean ctn = false;
			for (int i = 0; i < n; i++)
				if (exist(i)) {
					if (!ctn) {
						x++;
						ctn = true;
					}
				} else
					ctn = false;
			int[][] ans = new int[x][2];
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

		private boolean exist(int i) {
			return func.exist(time, i);
		}

	}

	public static class ListCurve extends AbCurve implements Control.MassCtrl<DotBullet> {

		private Queue<DotBullet> qd = new ArrayDeque<>();

		public ListCurve(Sprite.SParam cesp) {
			super(cesp);
		}

		public void addP(DotBullet d) {
			d.clearCtrl(Dot.class);
			d.addCtrl(new Control.ElemCtrl<DotBullet>(d, this));
			qd.add(d);
		}

		@Override
		public boolean finished() {
			return false;
		}

		@Override
		public boolean finished(DotBullet b) {
			return !qd.contains(b) || qd.peek() == b && b.dot.finished();
		}

		@Override
		public P[][] getPos() {
			List<P[]> list = new ArrayList<>();
			List<P> cur = null;
			for (DotBullet d : qd) {
				if (!d.isDead()) {
					if (cur == null)
						cur = new ArrayList<>();
					cur.add(d.dot.pos);
				} else {
					if (cur != null)
						list.add(cur.toArray(new P[0]));
					cur = null;
				}
			}
			if (cur != null)
				list.add(cur.toArray(new P[0]));
			return list.toArray(new P[0][]);
		}

		@Override
		public void post() {
			// it should not effect the graphics too much
			int i = 0, max = Math.max(1, qd.size() / 40);
			while (qd.peek() != null && qd.peek().isDead()) {
				qd.poll();
				i++;
				if (i >= max)
					break;
			}
		}

		@Override
		public void update(int dt) {
		}

	}

	public final Sprite.CurveESprite sprite;

	public AbCurve(Sprite.SParam cesp) {
		super(cesp.r);
		sprite = cesp.getEntity(this);
	}

}
