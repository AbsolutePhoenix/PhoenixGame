package games.absolutephoenix.phoenixgame.item;

import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.Entity;
import games.absolutephoenix.phoenixgame.entity.furniture.Furniture;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.gfx.Sprite;

public class PowerGloveItem extends Item {
	
	public PowerGloveItem() {
		super("Power Glove", new Sprite(0, 12, 0));
	}
	
	public boolean interact(Player player, Entity entity, Direction attackDir) {
		if (entity instanceof Furniture) { // If the power glove is used on a piece of furniture...
			Furniture f = (Furniture) entity;
			f.take(player); // Picks up the furniture
			return true;
		}
		return false; // method returns false if we were not given a furniture entity.
	}
	
	public PowerGloveItem clone() {
		return new PowerGloveItem();
	}
}
