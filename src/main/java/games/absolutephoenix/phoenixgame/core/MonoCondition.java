package games.absolutephoenix.phoenixgame.core;

@FunctionalInterface
public interface MonoCondition<T> {
	boolean check(T arg);
}
