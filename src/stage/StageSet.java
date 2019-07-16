package stage;

import battle.Control;
import battle.Control.UpdCtrl;

public class StageSet {

	public static final String[] SPNAME = new String[] { "Okina S1 resonance", "Reimu S2 immunity", "Youmu", "Koishi",
			"Yukari S1 tentacle", "Yuuka S1", "Yuyuko S1", "ref", "Sanae NS1 laser ref", "Sanae NS2 rotate",
			"Yuuka S2 gene", "Reimu S1 concious", "Okina S2 wall", "Okina NS2 wave", "Okina S3 fence", "Okina NS1 flow",
			"Sakuya S1 trace", "Sakuya NS2 ref", "Sakuya NS1 warn", "Sanae S1 star", "Reimu S3 square",
			"Sakuya S2 trap" };

	public static final String[] CHNAME = { "test", "Okina", "Reimu", "Sanae", "Sakuya", "Yukari", "Yuuka", "Yuyuko",
			"Youmu", "Koishi" };

	public static final int[][] spell = { { 7 }, { 0, 12, 13, 14, 15 }, { 1, 11, 20 }, { 8, 9, 19 }, { 16, 17, 18, 21 },
			{ 4 }, { 5, 10 }, { 6 }, { 2 }, { 3 } };

	public static Control.UpdCtrl getStage(String i, int diff) {
		try {
			return (UpdCtrl) Class.forName("stage.TestStage_" + i).getConstructor(Integer.TYPE).newInstance(diff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
