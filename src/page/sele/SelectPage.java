package page.sele;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import page.JBTN;
import page.JTG;
import page.Page;
import util.Data;

public class SelectPage extends Page {

	private static final long serialVersionUID = 1L;

	private final JBTN back = new JBTN("back");
	private final JBTN strt = new JBTN("start");

	private final JTG cbl = new JTG("clear bullet");
	private final JTG cbg = new JTG("clear BG");
	private final JTG geo = new JTG("draw geomotry");
	private final JTG tex = new JTG("draw texture");

	private final JList<String> list = new JList<String>(
			new String[] { "Okina", "Reimu", "Youmu", "Koishi", "Yukari", "Yuuka", "Yuyuko", "ref", "star" });
	private final JComboBox<String> jcb = new JComboBox<>(new String[] { "easy", "normal", "hard", "lunatic" });
	private final JScrollPane jspl = new JScrollPane(list);

	private int sele;

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
		set(jcb, x, y, 450, 300, 200, 50);
		set(cbl, x, y, 450, 400, 200, 50);
		set(cbg, x, y, 450, 500, 200, 50);
		set(geo, x, y, 450, 600, 200, 50);
		set(tex, x, y, 450, 700, 200, 50);
	}

	private void addListeners() {
		back.setLnr(e -> changePanel(getFront()));
		strt.setLnr(e -> changePanel(new BattlePage(this, sele, jcb.getSelectedIndex())));

		cbl.setLnr(x -> Data.CLEARBL = cbl.isSelected());
		cbg.setLnr(x -> Data.CLEARBG = cbg.isSelected());
		geo.setLnr(x -> Data.DEBUG = geo.isSelected());
		tex.setLnr(x -> Data.DRAWSPRITE = tex.isSelected());

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting())
					return;
				setSele(list.getSelectedIndex());
			}

		});
	}

	private void ini() {
		add(back);
		add(strt);
		add(jspl);
		add(jcb);
		add(cbl);
		add(cbg);
		add(geo);
		add(tex);

		cbl.setSelected(Data.CLEARBL);
		cbg.setSelected(Data.CLEARBG);
		geo.setSelected(Data.DEBUG);
		tex.setSelected(Data.DRAWSPRITE);

		jcb.setSelectedIndex(0);
		setSele(-1);
		addListeners();
	}

	private void setSele(int s) {
		sele = s;
		strt.setEnabled(sele >= 0);
	}

}
