package battle.bullet;

import battle.Engine;
import battle.Sprite.SParam;
import battle.entity.Emiter;
import util.P;

public class TimeCurve extends Curve.ListCurve implements Emiter.Ticker {

	private final P pos;
	private final SParam sp;
	private final Mover move;

	public TimeCurve(SParam cesp, int n, int dt, P p, Mover m) {
		super(cesp);
		sp = cesp;
		move = m;
		pos = p;
		Engine.RUNNING.add(new Emiter(0, dt, dt * n, this));

	}

	@Override
	public void tick(Emiter e, int it, int ex) {
		DotBullet db = new DotBullet(new Dot(pos.copy(), sp, move.copy()));
		addP(db);
		Engine.RUNNING.add(db);
	}

}
