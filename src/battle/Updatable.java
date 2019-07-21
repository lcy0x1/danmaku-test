package battle;

public interface Updatable {

	public default void draw() {
	}

	public default void post() {
	}

	public void update(int dt);

}
