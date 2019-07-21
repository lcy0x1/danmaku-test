package battle.bullet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import battle.Control;
import battle.Shape;
import battle.Sprite;
import util.P;

public abstract class Curve extends Shape.LineSegs implements Control.UpdCtrl {

	public static interface DotCont {

		public Dot getDot();

		public boolean isDead();

		public void clearCtrl(Class<? extends Control> cls);

		public void addCtrl(Control ctrl);

	}

	public static class FuncCurve extends Curve {

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

	public static class ListCurve extends Curve implements Control.MassCtrl<DotCont> {

		private Queue<DotCont> qd = new ArrayDeque<>();

		public ListCurve(Sprite.SParam cesp) {
			super(cesp);
		}

		public void addP(DotCont d) {
			d.clearCtrl(Dot.class);
			d.addCtrl(new Control.ElemCtrl<DotCont>(d, this));
			qd.add(d);
		}

		@Override
		public boolean finished() {
			return false;
		}

		@Override
		public boolean finished(DotCont b) {
			return !qd.contains(b) || qd.peek() == b && b.getDot().finished();
		}

		@Override
		public P[][] getPos() {
			List<P[]> list = new ArrayList<>();
			List<P> cur = null;
			for (DotCont d : qd) {
				if (!d.isDead()) {
					if (cur == null)
						cur = new ArrayList<>();
					cur.add(d.getDot().pos);
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

	public final Sprite.ESprite sprite;

	public Curve(Sprite.SParam cesp) {
		super(cesp.getRadius());
		sprite = cesp.getEntity(this);
	}

}
