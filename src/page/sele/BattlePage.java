package page.sele;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import battle.Engine;
import jogl.GLFPC;
import main.Timer;
import page.JBTN;
import page.JL;
import page.Page;
import stage.StageSet;
import util.Data;
import util.P;

public class BattlePage extends Page {

	private static final long serialVersionUID = 1L;

	private final JBTN back = new JBTN("back");
	private final JL jctn = new JL();
	private final JL jtim = new JL();
	private final JL jdct = new JL();
	private final GLFPC fpc;
	private final Engine e;

	private boolean pause = true;

	private P pre, disp = new P(0, 0);

	private boolean up, down, left, right, shift;

	protected BattlePage(Page p, int sta, int diff) {
		super(p);
		e = new Engine(StageSet.getStage(Data.str(sta, 3), diff));
		fpc = new GLFPC(e);
		ini();
		resized();
	}

	@Override
	protected void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_UP)
			up = true;
		if (ke.getKeyCode() == KeyEvent.VK_DOWN)
			down = true;
		if (ke.getKeyCode() == KeyEvent.VK_LEFT)
			left = true;
		if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
			right = true;
		if (ke.getKeyCode() == KeyEvent.VK_SHIFT)
			shift = true;
	}

	@Override
	protected void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE)
			pause = !pause;
		if (ke.getKeyCode() == KeyEvent.VK_UP)
			up = false;
		if (ke.getKeyCode() == KeyEvent.VK_DOWN)
			down = false;
		if (ke.getKeyCode() == KeyEvent.VK_LEFT)
			left = false;
		if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
			right = false;
		if (ke.getKeyCode() == KeyEvent.VK_SHIFT)
			shift = false;
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
		fpc.setBounds(x / 2 - y * 2 / 5, 0, y * 4 / 5, y);
	}

	@Override
	protected void timer(int t) {
		if (!pause) {
			P move = new P(0, 0);
			if (up)
				move.y--;
			if (down)
				move.y++;
			if (left)
				move.x--;
			if (right)
				move.x++;
			move.times(shift ? 2 : 5);
			e.pl.ext.plus(move).plus(disp).limit(Engine.BOUND);
			e.update(Timer.p);
			jctn.setText("count:" + e.count());
			int sec = e.time.clock / 1000;
			jtim.setText("time:" + Data.str(sec / 60, 2) + ":" + Data.str(sec % 60, 2));
			jdct.setText("death:" + e.pl.deadCount);
		}
		disp.setTo(0, 0);
		fpc.paint();
	}

	private void addListeners() {
		back.setLnr(e -> changePanel(getFront()));
	}

	private void ini() {
		add(back);
		add(jctn);
		add(jtim);
		add(jdct);
		add(fpc);
		addListeners();
	}

}
