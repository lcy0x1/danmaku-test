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

		list.add(new SpellSet("Sakuya (7)")//
				.add(S018.class, "(N1) warn")//
				.add(S016.class, "(S1) [Warning] The Last Warning")//
				.add(S017.class, "(N2) follow ref")//
				.add(S023.class, "(S2) [Time] Sakuya's World")//
				.add(S031.class, "(N3) wall ref")//
				.add(S022.class, "(S3) [Time] The Shrinking Universe")//
				.add(S021.class, "(LW) Sakuya's Magical Trap"));

		list.add(new SpellSet("Reimu (7)")//
				.add(S026.class, "(N1) double")//
				.add(S024.class, "(S1) [Exterminate] The Unbreakable Seal")//
				.add(S027.class, "(N2) chaos")//
				.add(S020.class, "(S2) [Exterminate] Spell of the Boundary")//
				.add(S029.class, "(N3) boundary")//
				.add(S011.class, "(S3) [Boundary] Boundary of Conciousness")//
				.add(S001.class, "(LW) Hakurei Immunity System"));

		list.add(new SpellSet("Okina (8)")//
				.add(S007.class, "(N1) ref")//
				.add(S015.class, "(S1) [Spring] Cherry Blossom")//
				.add(S034.class, "(S2) [Summer] falling Iceburg")//
				.add(S028.class, "(S3) [Autumn] Wall Whirlwind")//
				.add(S013.class, "(S4) [Winter] Quiet Snowflakes")//
				.add(S000.class, "(S5) [Season] Resonance of Four Seasons")//
				.add(S014.class, "(S6) [Door] The Mystrious Gate")//
				.add(S012.class, "(S7) [Spirit] Activated Spirits"));

		list.add(new SpellSet("Yukari (7)")//
				.add(S036.class, "(N1) W-P 1")//
				.add(S004.class, "(S1) [Mystery] Tantacles of the Unknown")//
				.add(S030.class, "(N2) W-P 2")//
				.add(S025.class, "(S2) [Chaos] Evening Stars")//
				.add(S033.class, "(N3) exp")//
				.add(S035.class, "(S3) Yukari's Damaku Barrier")//
				.add(S032.class, "(LW) Gensokyo Hakurei Boundary"));

		list.add(new SpellSet("Sanae (3)")//
				.add(S009.class, "(N1) rotate")//
				.add(S019.class, "(S2) [Star] Let Me Teach You How to Draw Stars")//
				.add(S008.class, "(N2) laser ref"));

		list.add(new SpellSet("Yuuka (2)")//
				.add(S005.class, "S1")//
				.add(S010.class, "S2 gene"));

		list.add(new SpellSet("Yuyuko (1)")//
				.add(S006.class, "S1"));

		list.add(new SpellSet("Youmu (1)")//
				.add(S002.class, "LW"));

		list.add(new SpellSet("Koishi (1)")//
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
