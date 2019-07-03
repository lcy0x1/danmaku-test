package page;

import javax.swing.JLabel;

import page.sele.SelectPage;

public class MainPage extends Page {

	private static final long serialVersionUID = 1L;

	private final JLabel memo = new JLabel();

	private final JBTN strt = new JBTN("start");

	public MainPage() {
		super(null);

		ini();
		resized();
	}

	@Override
	protected void renew() {
		Runtime.getRuntime().gc();
		setMemo();
	}

	@Override
	protected void resized(int x, int y) {
		setBounds(0, 0, x, y);
		set(memo, x, y, 50, 30, 500, 50);
		set(strt, x, y, 200, 200, 200, 50);
	}

	private void addListeners() {
		strt.setLnr(x -> changePanel(new SelectPage(this)));
	}

	private void ini() {
		add(memo);
		add(strt);
		setMemo();
		addListeners();
	}

	private void setMemo() {
		long f = Runtime.getRuntime().freeMemory();
		long t = Runtime.getRuntime().totalMemory();
		long m = Runtime.getRuntime().maxMemory();
		double per = 100.0 * (t - f) / m;
		memo.setText("memory used: " + (t - f >> 20) + " MB / " + (m >> 20) + " MB, " + (int) per + "%");

	}

}
