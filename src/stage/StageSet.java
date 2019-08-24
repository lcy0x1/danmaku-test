package stage;

import java.util.ArrayList;
import java.util.List;
import stage.s0.*;

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

		public StageSection getStage(int diff) {
			return new StageSecs(new StageSecs.SpellItr(this, diff));
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

		list.add(new SpellSet("Reimu (8)")//
				.add(S026.class, "(N1) double")//
				.add(S024.class, "(S1) [Exterminate] The Unbreakable Seal")//
				.add(S027.class, "(N2) chaos")//
				.add(S020.class, "(S2) [Boundary] Spell of the Boundary")//
				.add(S029.class, "(N3) boundary")//
				.add(S011.class, "(S3) [Boundary] Boundary of Conciousness")//
				.add(S046.class, "(S4) Final Extermination")//
				.add(S001.class, "(LW) Hakurei Immunity System"));

		list.add(new SpellSet("Sanae (4)")//
				.add(S009.class, "(N1) rotate")//
				.add(S040.class, "(N2) segment ref")//
				.add(S008.class, "(N3) laser ref")//
				.add(S019.class, "(S2) [Star] Let Me Teach You How to Draw Stars"));

		list.add(new SpellSet("Youmu (1)")//
				.add(S002.class, "LW"));

		list.add(new SpellSet("Okina (10)")//
				.add(S007.class, "(N1) ref")//
				.add(S015.class, "(S1-1) [Spring] Cherry Blossom")//
				.add(S034.class, "(S1-2) [Summer] falling Iceburg")//
				.add(S028.class, "(S1-3) [Autumn] Wall Whirlwind")//
				.add(S013.class, "(S1-4) [Winter] Quiet Snowflakes")//
				.add(S014.class, "(N2) gate")//
				.add(S000.class, "(S2) [Life] Resonance of Four Seasons")//
				.add(S041.class, "(N3) oscillation")//
				.add(S012.class, "(S3) [Spirit] Spiritual Commutor")//
				.add(S039.class, "(LW) Berserk Spirit and Chaotic Life"));

		list.add(new SpellSet("Yukari (10)")//
				.add(S045.class, "(N1) double Rot 1")//
				.add(S004.class, "(S1) [Mystery] Tantacles of the Unknown")//
				.add(S036.class, "(N2) W-P 1")//
				.add(S025.class, "(S2) [Chaos] Evening Stars")//
				.add(S030.class, "(N3) W-P 2")//
				.add(S042.class, "(S3) [Boundary] Primitization of Epiphenomenon")//
				.add(S033.class, "(N4) Chasing Reality")//
				.add(S044.class, "(S4) [Boundary] Boundary of Terror and Beauty")//
				.add(S035.class, "(S5) Yukari's Damaku Barrier")//
				.add(S032.class, "(LW) Gensokyo Hakurei Boundary"));

		list.add(new SpellSet("Yuyuko (2)")//
				.add(S006.class, "(S1) [Ghost] Ghost Migration")//
				.add(S038.class, "(LW) Butterflies of the Death"));

		list.add(new SpellSet("Koishi (3)")//
				.add(S005.class, "S1 [Love] Love Trap")//
				.add(S010.class, "S2 [Love] Collision of Genes")//
				.add(S003.class, "S3 [Awaken] The Third Self"));

		list.add(new SpellSet("Temp (2)")//
				.add(S037.class, "(N1) ")//
				.add(S043.class, "(S4) Now or Later")//
				.add(S047.class, "(S1) Time Reverse"));

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
		if (j < 0)
			return list.get(i).getStage(diff);
		return list.get(i).list.get(j).getStage(diff);
	}

}
