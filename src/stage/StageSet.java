package stage;

import java.util.ArrayList;
import java.util.List;

import battle.Control;

public class StageSet {
	
	public static class Spell {
		
		public final String name;
		
		private final Class<? extends SpellCard> cls;
		
		private Spell(Class<? extends SpellCard> clas, String str) {
			name=str;
			cls=clas;
		}
		
		public Control.UpdCtrl getStage(int diff) {
			try {
				return (SpellCard) cls.getConstructor(Integer.TYPE).newInstance(diff);
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
			list.add(new Spell(cls,str));
			return this;
		}

	}

	public static final List<SpellSet> list = new ArrayList<>();

	static {
		
		list.add(new SpellSet("Okina")//
				.add(TestStage_015.class, "NS1 flow")//
				.add(TestStage_000.class, "S1 resonance")//
				.add(TestStage_013.class, "NS2 wave")//
				.add(TestStage_012.class, "S2 wall")//
				.add(TestStage_014.class, "S3 fence"));

		list.add(new SpellSet("Reimu")//
				.add(TestStage_011.class, "S1 concious")//
				.add(TestStage_001.class, "S2 immunity")//
				.add(TestStage_020.class, "S3 square"));

		list.add(new SpellSet("Sanae")//
				.add(TestStage_009.class, "NS1 rotate")//
				.add(TestStage_007.class, "S1 ref")//
				.add(TestStage_008.class, "NS2 laser ref")//
				.add(TestStage_019.class, "S2 star"));

		list.add(new SpellSet("Sakuya")//
				.add(TestStage_018.class, "NS1 warn")//
				.add(TestStage_016.class, "S1 trace")//
				.add(TestStage_017.class, "NS2 ref")//
				.add(TestStage_021.class, "S2 trap"));

		list.add(new SpellSet("Yukari")//
				.add(TestStage_004.class, "S1 tantacle"));

		list.add(new SpellSet("Yuuka")//
				.add(TestStage_005.class, "S1")//
				.add(TestStage_010.class, "S2 gene"));

		list.add(new SpellSet("Yuyuko")//
				.add(TestStage_006.class, "S1"));

		list.add(new SpellSet("Youmu")//
				.add(TestStage_002.class, "S1"));

		list.add(new SpellSet("Koishi")//
				.add(TestStage_003.class, "S1"));

	}

	public static Control.UpdCtrl getStage(int i,int j, int diff) {
		return list.get(i).list.get(j).getStage(diff);
	}
	
	public static String[] getNames() {
		String[] ans=new String[list.size()];
		for(int i=0;i<list.size();i++)
			ans[i]=list.get(i).name;
		return ans;
	}
	
	public static String[] getNames(int n) {
		SpellSet ss=list.get(n);
		String[] ans=new String[ss.list.size()];
		for(int i=0;i<ss.list.size();i++)
			ans[i]=ss.list.get(i).name;
		return ans;
	}

}
