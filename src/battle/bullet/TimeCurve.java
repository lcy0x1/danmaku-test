package battle.bullet;

import battle.Engine;
import battle.Sprite.SParam;
import battle.entity.Emiter;
import util.P;

public class TimeCurve extends Curve.ListCurve implements Emiter.Ticker {

	private final P pos;
	private final SParam sp;
	private final Mover move;
	private final int lt;

	public TimeCurve(SParam cesp, int n, int dt, P p, Mover m) {
		this(cesp, n, dt, p, m, 0, -1);

	}

	public TimeCurve(SParam cesp, int n, int dt, P p, Mover m, int del) {
		this(cesp, n, dt, p, m, del, -1);
	}

	public TimeCurve(SParam cesp, int n, int dt, P p, Mover m, int del, int t) {
		super(cesp);
		sp = cesp;
		move = m;
		pos = p;
		lt = t;
		Engine.RUNNING.add(new Emiter(0, dt, dt * n + del, this).setDelay(del));

	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		DotBullet db = new DotBullet(new Dot(pos.copy(), sp, move.copy()), lt);
		addP(db);
		Engine.RUNNING.add(db);
	}

}
