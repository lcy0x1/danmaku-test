package page.sele;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import page.JBTN;
import page.Page;

public class SelectPage extends Page {

	private static final long serialVersionUID = 1L;

	private final JBTN back = new JBTN("back");
	private final JBTN strt = new JBTN("start");

	private final JList<String> list = new JList<String>(new String[] { "000", "001", "002" });
	private final JScrollPane jspl = new JScrollPane(list);

	private String sele;

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
		set(jspl, x, y, 200, 300, 200, 800);
	}

	private void addListeners() {
		back.setLnr(e -> changePanel(getFront()));
		strt.setLnr(e -> changePanel(new BattlePage(this, sele)));
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting())
					return;
				setSele(list.getSelectedValue());
			}

		});
	}

	private void ini() {
		add(back);
		add(strt);
		add(jspl);
		setSele(null);
		addListeners();
	}

	private void setSele(String s) {
		sele = s;
		strt.setEnabled(sele != null);
	}

}
