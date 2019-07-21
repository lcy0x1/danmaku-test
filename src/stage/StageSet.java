package stage;

import java.util.ArrayList;
import java.util.List;

import battle.Control;

public class StageSet {

	public static class Spell {

		public final String name;

		private final Class<? extends SpellCard> cls;

		private Spell(Class<? extends SpellCard> clas, String str) {
			name = str;
			cls = clas;
		}

		public Control.UpdCtrl getStage(int diff) {
			try {
				return cls.getConstructor(Integer.TYPE).newInstance(diff);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	public static class SpellSet {

		public final String name;

		public final List<Spell> list = new ArrayList<>();

		private SpellSet(String str) {
			name = str;
		}

		private SpellSet add(Class<? extends SpellCard> cls, String str) {
			list.add(new Spell(cls, str));
			return this;
		}

	}

	public static final List<SpellSet> list = new ArrayList<>();

	static {

		list.add(new SpellSet("Okina")//
				.add(S015.class, "NS1 flow")//
				.add(S000.class, "S1 resonance")//
				.add(S013.class, "NS2 wave")//
				.add(S012.class, "S2 wall")//
				.add(S014.class, "S3 fence"));

		list.add(new SpellSet("Reimu")//
				.add(S011.class, "S1 concious")//
				.add(S001.class, "S2 immunity")//
				.add(S020.class, "S3 square")//
				.add(S024.class, "S4 enclose"));

		list.add(new SpellSet("Sanae")//
				.add(S009.class, "NS1 rotate")//
				.add(S007.class, "S1 ref")//
				.add(S008.class, "NS2 laser ref")//
				.add(S019.class, "S2 star"));

		list.add(new SpellSet("Sakuya")//
				.add(S018.class, "NS1 warn")//
				.add(S016.class, "S1 trace")//
				.add(S017.class, "NS2 ref")//
				.add(S023.class, "S2 extend")//
				.add(S022.class, "S3 shrink")//
				.add(S021.class, "S4 trap"));

		list.add(new SpellSet("Yukari")//
				.add(S004.class, "S1 tantacle"));

		list.add(new SpellSet("Yuuka")//
				.add(S005.class, "S1")//
				.add(S010.class, "S2 gene"));

		list.add(new SpellSet("Yuyuko")//
				.add(S006.class, "S1"));

		list.add(new SpellSet("Youmu")//
				.add(S002.class, "S1"));

		list.add(new SpellSet("Koishi")//
				.add(S003.class, "S1"));

	}

	public static String[] getNames() {
		String[] ans = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
			ans[i] = list.get(i).name;
		return ans;
	}

	public static String[] getNames(int n) {
		SpellSet ss = list.get(n);
		String[] ans = new String[ss.list.size()];
		for (int i = 0; i < ss.list.size(); i++)
			ans[i] = ss.list.get(i).name;
		return ans;
	}

	public static Control.UpdCtrl getStage(int i, int j, int diff) {
		return list.get(i).list.get(j).getStage(diff);
	}

}
