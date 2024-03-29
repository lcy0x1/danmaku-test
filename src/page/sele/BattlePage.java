package page.sele;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import battle.Engine;
import jogl.GLFPC;
import main.Timer;
import page.JBTN;
import page.JL;
import page.Page;
import stage.SpellCard.LifeSpell;
import stage.StageSection;
import stage.StageSection.TimedStage;
import stage.StageSet;
import util.Data;
import util.P;

public class BattlePage extends Page {

	private static final long serialVersionUID = 1L;

	private static final int[] KEYS = { KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
			KeyEvent.VK_SHIFT, KeyEvent.VK_Z };

	private static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, SHIFT = 4, SHOOT = 5;

	private final JBTN back = new JBTN("back");
	private final JL jctn = new JL();
	private final JL jtim = new JL();
	private final JL jdct = new JL();
	private final JL jtrm = new JL();
	private final JL jlhp = new JL();
	private final JL jeff = new JL();
	private final GLFPC fpc;
	private final Engine e;

	private boolean pause = true;

	private P pre, disp = new P(0, 0);

	private final boolean[] press = new boolean[KEYS.length];

	protected BattlePage(Page p, int cha, int sta, int diff) {
		super(p);
		System.gc();
		e = new Engine.StartProfile(StageSet.getStage(cha, sta, diff)).getInstance();
		fpc = new GLFPC(e);
		ini();
		resized();
	}

	@Override
	protected synchronized void keyPressed(KeyEvent ke) {
		for (int i = 0; i < KEYS.length; i++)
			if (ke.getKeyCode() == KEYS[i])
				press[i] = true;
	}

	@Override
	protected synchronized void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE)
			pause = !pause;
		for (int i = 0; i < KEYS.length; i++)
			if (ke.getKeyCode() == KEYS[i])
				press[i] = false;
	}

	@Override
	protected void mouseDragged(MouseEvent me) {
		mouseMoved(me);
	}

	@Override
	protected void mouseMoved(MouseEvent me) {
		P pos = new P(fpc.getLocationOnScreen()).sf(new P(me.getLocationOnScreen()));
		pos.divide(new P(fpc.getSize()));
		pos.times(Engine.BOUND);
		if (pre == null)
			pre = pos;
		if (me.isShiftDown())
			disp.plus(pre, -1).plus(pos);
		pre = pos;
	}

	@Override
	protected void resized(int x, int y) {
		setBounds(0, 0, x, y);
		set(back, x, y, 0, 0, 200, 50);
		set(jctn, x, y, 1800, 100, 400, 50);
		set(jtim, x, y, 1800, 150, 400, 50);
		set(jdct, x, y, 1800, 200, 400, 50);
		set(jtrm, x, y, 1800, 250, 400, 50);
		set(jlhp, x, y, 1800, 300, 400, 50);
		set(jeff, x, y, 1800, 350, 400, 50);
		fpc.setBounds(x / 2 - y * 2 / 5, 0, y * 4 / 5, y);
	}

	@Override
	protected void timer(int t) {
		long t0 = 0, t1 = 0;
		if (!pause) {
			P move = new P(0, 0);
			if (press[UP])
				move.y--;
			if (press[DOWN])
				move.y++;
			if (press[LEFT])
				move.x--;
			if (press[RIGHT])
				move.x++;
			move.times(press[SHIFT] ? 2 : 5).plus(disp);
			t0 = System.nanoTime();
			e.update(new Engine.UpdateProfile(move, Timer.p, press[SHOOT]));
			t1 = System.nanoTime();
			jctn.setText("count:" + e.drawLoad + "/" + e.count());
			int sec = e.time.clock / 1000;
			jtim.setText("time:" + Data.str(sec / 60, 2) + ":" + Data.str(sec % 60, 2));
			jdct.setText("death:" + e.pl.deadCount);

			StageSection ss = e.stage;
			if (ss instanceof TimedStage) {
				TimedStage st = (TimedStage) ss;
				int ttim = st.length;
				int ctim = ttim - st.time;
				jtrm.setText("time remain: " + ctim / 1000 + "/" + ttim / 1000);
			} else
				jtrm.setText("");
			if (ss instanceof LifeSpell) {
				LifeSpell bs = (LifeSpell) ss;
				double hp = bs.getHP();
				jlhp.setText("HP: " + (int) (hp * 100) + "%");
			} else
				jlhp.setText("");
		}
		disp.setTo(0, 0);
		long t2 = System.nanoTime();
		fpc.paint();
		long t3 = System.nanoTime();
		jeff.setText("calc: " + ((t1 - t0) >> 20) + "ms, graph: " + ((t3 - t2) >> 20) + "ms");
	}

	private void addListeners() {
		back.setLnr(e -> changePanel(getFront()));
	}

	private void ini() {
		add(back);
		add(jctn);
		add(jtim);
		add(jdct);
		add(jtrm);
		add(jlhp);
		add(jeff);
		add(fpc);
		addListeners();
	}

}
