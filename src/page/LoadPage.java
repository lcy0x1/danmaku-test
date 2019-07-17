package page;

public class LoadPage extends Page {

	private static final long serialVersionUID = 1L;

	protected LoadPage() {
		super(null);

		ini();
		resized();
	}

	@Override
	protected void resized(int x, int y) {
		setBounds(0, 0, x, y);
	}

	private void addListeners() {
	}

	private void ini() {
		addListeners();
	}

}
