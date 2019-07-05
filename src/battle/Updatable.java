package battle;

public interface Updatable {

	public default void post() {
	}

	public void update(int dt);

}
