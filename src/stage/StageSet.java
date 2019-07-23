package stage;

import java.util.ArrayList;
import java.util.List;

import battle.Control;

public class StageSet {

	public static class Spell {

		public final String name;

		private final Class<? extends StageSection> cls;

		private Spell(Class<? extends StageSection> clas, String str) {
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
				.add(S007.class, "NS1 ref")//
				.add(S014.class, "S1 fence")//
				.add(S013.class, "NS2 wave")//
				.add(S000.class, "S2 resonance")//
				.add(S015.class, "NS3 flow")//
				.add(S012.class, "S3 wall"));

		list.add(new SpellSet("Reimu")//
				.add(S026.class, "NS1 double")//
				.add(S024.class, "S1 enclose")//
				.add(S027.class, "NS2 chaos")//
				.add(S020.class, "S2 square")//
				.add(S029.class, "NS3 boundary")//
				.add(S011.class, "S3 concious")//
				.add(S001.class, "LW immunity"));

		list.add(new SpellSet("Sanae")//
				.add(S009.class, "NS1 rotate")//
				.add(S019.class, "S2 star")//
				.add(S008.class, "NS2 laser ref"));

		list.add(new SpellSet("Sakuya")//
				.add(S018.class, "NS1 warn")//
				.add(S016.class, "S1 trace")//
				.add(S017.class, "NS2 follow ref")//
				.add(S023.class, "S2 extend")//
				.add(S031.class, "NS3 wall ref")//
				.add(S022.class, "S3 shrink")//
				.add(S021.class, "LW trap"));

		list.add(new SpellSet("Yukari")//
				.add(S028.class, "NS1 wave-particle")//
				.add(S004.class, "S1 tantacle")//
				.add(S030.class, "NS2 wave-particle")//
				.add(S025.class, "S2 elec")//
				.add(S032.class, "LW boundary"));

		list.add(new SpellSet("Yuuka")//
				.add(S005.class, "S1")//
				.add(S010.class, "S2 gene"));

		list.add(new SpellSet("Yuyuko")//
				.add(S006.class, "S1"));

		list.add(new SpellSet("Youmu")//
				.add(S002.class, "LW"));

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
