package games.absolutephoenix.phoenixgame.item;

import java.util.ArrayList;

import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.gfx.Sprite;
import games.absolutephoenix.phoenixgame.level.Level;
import games.absolutephoenix.phoenixgame.level.tile.Tile;
import games.absolutephoenix.phoenixgame.level.tile.TorchTile;

public class TorchItem extends TileItem {
	
	public static ArrayList<Item> getAllInstances() {
		ArrayList<Item> items = new ArrayList<>();
		items.add(new TorchItem());
		return items;
	}
	
	private TorchItem() { this(1); }
	private TorchItem(int count) {
		super("Torch", (new Sprite(11, 3, 0)), count, "", "dirt", "Wood Planks", "Stone Bricks", "Obsidian", "Wool", "Red Wool", "Blue Wool", "Green Wool", "Yellow Wool", "Black Wool", "grass", "sand");
	}
	
	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, Direction attackDir) {
		if(validTiles.contains(tile.name)) {
			level.setTile(xt, yt, TorchTile.getTorchTile(tile));
			return super.interactOn(true);
		}
		return super.interactOn(false);
	}
	
	@Override
	public boolean equals(Item other) {
		return other instanceof TorchItem;
	}
	
	@Override
	public int hashCode() { return 8931; }
	
	public TorchItem clone() {
		return new TorchItem(count);
	}
}
