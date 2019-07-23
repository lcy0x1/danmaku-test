package page.sele;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import page.JBTN;
import page.JTG;
import page.Page;
import stage.StageSet;
import util.Data;

public class SelectPage extends Page {

	private static final long serialVersionUID = 1L;

	private final JBTN back = new JBTN("back");
	private final JBTN strt = new JBTN("start");

	private final JTG cbl = new JTG("clear bullet");
	private final JTG cbg = new JTG("clear BG");
	private final JTG geo = new JTG("draw geomotry");
	private final JTG tex = new JTG("draw texture");

	private final JList<String> jlm = new JList<String>(StageSet.getNames());
	private final JList<String> jls = new JList<String>();
	private final JComboBox<String> jcb = new JComboBox<>(new String[] { "easy", "normal", "hard", "lunatic" });
	private final JScrollPane jspm = new JScrollPane(jlm);
	private final JScrollPane jsps = new JScrollPane(jls);

	private int sele, ssub;

	public SelectPage(Page p) {
		super(p);

		ini();
		resized();
	}

	@Override
	protected void resized(int x, int y) {
		setBounds(0, 0, x, y);
		set(back, x, y, 0, 0, 200, 50);
		set(jspm, x, y, 200, 200, 400, 800);
		set(jsps, x, y, 650, 200, 600, 800);
		set(strt, x, y, 1300, 200, 200, 50);
		set(jcb, x, y, 1300, 300, 200, 50);
		set(cbl, x, y, 1300, 400, 200, 50);
		set(cbg, x, y, 1300, 500, 200, 50);
		set(geo, x, y, 1300, 600, 200, 50);
		set(tex, x, y, 1300, 700, 200, 50);
	}

	private void addListeners() {
		back.setLnr(e -> changePanel(getFront()));
		strt.setLnr(e -> changePanel(new BattlePage(this, sele, ssub, jcb.getSelectedIndex())));

		cbl.setLnr(x -> Data.CLEARBL = cbl.isSelected());
		cbg.setLnr(x -> Data.CLEARBG = cbg.isSelected());
		geo.setLnr(x -> Data.DEBUG = geo.isSelected());
		tex.setLnr(x -> Data.DRAWSPRITE = tex.isSelected());

		jlm.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (isAdj() || e.getValueIsAdjusting())
					return;
				setMain(jlm.getSelectedIndex());
			}

		});

		jls.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (isAdj() || e.getValueIsAdjusting())
					return;
				setSub(jls.getSelectedIndex());
			}

		});

	}

	private void ini() {
		add(back);
		add(strt);
		add(jspm);
		add(jsps);
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
		setMain(-1);
		addListeners();
	}

	private void setMain(int s) {
		change(true);
		sele = s;
		if (s == -1) {
			jls.clearSelection();
			jls.setListData(new String[0]);
			setSub(-1);
		} else {
			jls.clearSelection();
			jls.setListData(StageSet.getNames(s));
			jls.setSelectedIndex(0);
			setSub(0);
		}
		change(false);
	}

	private void setSub(int s) {
		ssub = s;
		strt.setEnabled(ssub >= 0);
	}

}
