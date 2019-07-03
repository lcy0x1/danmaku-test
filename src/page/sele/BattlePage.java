package page.sele;

import battle.Engine;
import jogl.GLFPC;
import page.JBTN;
import page.Page;
import stage.TestStage;

public class BattlePage extends Page {

	private static final long serialVersionUID = 1L;

	private final JBTN back = new JBTN("back");

	private final GLFPC fpc;

	private final Engine e;

	protected BattlePage(Page p) {
		super(p);
		e = new Engine(new TestStage());
		fpc = new GLFPC(e);
		ini();
		resized();
	}

	@Override
	protected void resized(int x, int y) {
		setBounds(0, 0, x, y);
		set(back, x, y, 0, 0, 200, 50);
		fpc.setBounds(x / 2 - y * 2 / 5, 0, y * 4 / 5, y);
	}

	@Override
	protected void timer(int t) {
		e.update(16);
		fpc.paint();
	}

	private void addListeners() {
		back.setLnr(e -> changePanel(getFront()));
	}

	private void ini() {
		add(back);
		add(fpc);
		addListeners();
	}

}
