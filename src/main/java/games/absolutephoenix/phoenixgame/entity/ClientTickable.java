package games.absolutephoenix.phoenixgame.entity;

public interface ClientTickable extends Tickable {
	
	default void clientTick() {
		tick();
	}
	
}
