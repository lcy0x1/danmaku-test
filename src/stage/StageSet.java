package stage;

import battle.Control;
import battle.Control.UpdCtrl;

public class StageSet {

	public static Control.UpdCtrl getStage(String i) {
		try {
			return (UpdCtrl) Class.forName("stage.TestStage_" + i).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
