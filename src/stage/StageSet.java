package stage;

import battle.Control;
import battle.Control.UpdCtrl;

public class StageSet {

	public static Control.UpdCtrl getStage(String i, int diff) {
		try {
			return (UpdCtrl) Class.forName("stage.TestStage_" + i).getConstructor(Integer.TYPE).newInstance(diff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
