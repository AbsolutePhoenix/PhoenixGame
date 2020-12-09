package games.absolutephoenix.phoenixgame.item;

import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.Entity;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.entity.mob.Sheep;
import games.absolutephoenix.phoenixgame.gfx.Sprite;

import java.util.ArrayList;

public class ShearItem extends Item {

	protected static ArrayList<Item> getAllInstances() {
		ArrayList<Item> items = new ArrayList<>();

		items.add(new ShearItem());

		return items;
	}

	private ShearItem() {
		super("Shear", new Sprite(1, 12, 0));
	}

	@Override
	public boolean interact(Player player, Entity entity, Direction attackDir) {
		if (entity instanceof Sheep) {
			if (!((Sheep) entity).cut) {
				((Sheep) entity).shear();
				return true;
			}
		}
		return false;
	}

	@Override
	public Item clone() { return new ShearItem(); }
}
