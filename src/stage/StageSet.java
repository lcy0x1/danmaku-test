package stage;

import java.util.ArrayList;
import java.util.List;

public class StageSet {

	public static class Spell {

		public final String name;

		private final Class<? extends StageSection> cls;

		private Spell(Class<? extends StageSection> clas, String str) {
			name = str;
			cls = clas;
		}

		public StageSection getStage(int diff) {
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

		private SpellSet add(Class<? extends StageSection> cls, String str) {
			list.add(new Spell(cls, str));
			return this;
		}

	}

	public static final List<SpellSet> list = new ArrayList<>();

	static {

		list.add(new SpellSet("Okina")//
				.add(S007.class, "(N1) ref")//
				.add(S014.class, "(S1) [Door] The Mystrious Gate")//
				.add(S013.class, "(N2) wave")//
				.add(S000.class, "(S2) [Season] Resonance of Four Seasons")//
				.add(S015.class, "(N3) flow")//
				.add(S012.class, "(S3) [Spirit] Activated Spirits")//
				.add(S034.class, "(N4) shift"));

		list.add(new SpellSet("Reimu")//
				.add(S026.class, "(N1) double")//
				.add(S024.class, "(S1) [Exterminate] The Unbreakable Seal")//
				.add(S027.class, "(N2) chaos")//
				.add(S020.class, "(S2) [Exterminate] Spell of the Boundary")//
				.add(S029.class, "(N3) boundary")//
				.add(S011.class, "(S3) [Boundary] Boundary of Conciousness")//
				.add(S001.class, "(LW) Hakurei Immunity System"));

		list.add(new SpellSet("Sanae")//
				.add(S009.class, "(N1) rotate")//
				.add(S019.class, "(S2) [Star] Let Me Teach You How to Draw Stars")//
				.add(S008.class, "(N2) laser ref"));

		list.add(new SpellSet("Sakuya")//
				.add(S018.class, "(N1) warn")//
				.add(S016.class, "(S1) [Warning] The Last Warning")//
				.add(S017.class, "(N2) follow ref")//
				.add(S023.class, "(S2) [Time] Sakuya's World")//
				.add(S031.class, "(N3) wall ref")//
				.add(S022.class, "(S3) [Time] The Shrinking Universe")//
				.add(S021.class, "(LW) Sakuya's Magical Trap"));

		list.add(new SpellSet("Yukari")//
				.add(S033.class, "(N1) exp")//
				.add(S028.class, "(N2) W-P 1")//
				.add(S004.class, "(S2) [Mystery] Tantacles of the Unknown")//
				.add(S030.class, "(N3) W-P 2")//
				.add(S025.class, "(S3) [Chaos] Evening Stars")//
				.add(S032.class, "(LW) Asymetrical Damaku Barrier"));

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

	public static StageSection getStage(int i, int j, int diff) {
		return list.get(i).list.get(j).getStage(diff);
	}

}
