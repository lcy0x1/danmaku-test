package entity;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Engine {

	public int time;

	private final Map<Integer,List<Entity>> entities = new TreeMap<>();
	
	public void update(int t) {
		handleCollision();
	}
	
	private void handleCollision() {
		entities.forEach((i,l)->{
			int atk=i>>16&65535;
			if(atk==0)
				return;
			entities.forEach((j,q)->{
				int base=j&65535;
				if((atk&base)==0)
					return;
				for(Entity e0:l)
					for(Entity e1:q)
						e0.collide(e1);
			});
		});
	}
	
	public void add(Entity e) {
		
	}

}
