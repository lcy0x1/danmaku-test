package page.sele;

import page.JBTN;
import page.Page;

public class SelectPage extends Page {

	private static final long serialVersionUID = 1L;

	private final JBTN back = new JBTN("back");

	private final JBTN strt = new JBTN("start");

	public SelectPage(Page p) {
		super(p);

		ini();
		resized();
	}

	@Override
	protected void resized(int x, int y) {
		setBounds(0, 0, x, y);
		set(back, x, y, 0, 0, 200, 50);
		set(strt, x, y, 200, 200, 200, 50);
	}

	private void addListeners() {
		back.setLnr(e -> changePanel(getFront()));
		strt.setLnr(e -> changePanel(new BattlePage(this)));
	}

	private void ini() {
		add(back);
		add(strt);
		addListeners();
	}

}
