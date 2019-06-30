package page;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JToggleButton;

public class JTG extends JToggleButton {

	private static final long serialVersionUID = 1L;

	public JTG() {
	}

	public JTG(int i, String str) {
		this(str);
	}

	public JTG(String str) {
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
