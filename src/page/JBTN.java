package page;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;

public class JBTN extends JButton {

	private static final long serialVersionUID = 1L;

	public JBTN() {
	}

	public JBTN(int i, String str) {
		this(str);
	}

	public JBTN(String str) {
		super(str);
	}

	public void setLnr(Consumer<ActionEvent> c) {
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				c.accept(e);
			}

		});
	}

}
