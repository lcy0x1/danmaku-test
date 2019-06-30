package page;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class JL extends JLabel {

	private static final long serialVersionUID = 1L;

	public JL() {
		this("");
	}

	public JL(int i, String str) {
		this(str);
	}

	public JL(String str) {
		super(str);
		setBorder(BorderFactory.createEtchedBorder());
	}

}
