package games.absolutephoenix.phoenixgame.level.tile.wool;

import games.absolutephoenix.phoenixgame.core.io.Sound;
import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.Entity;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.gfx.Sprite;
import games.absolutephoenix.phoenixgame.item.Item;
import games.absolutephoenix.phoenixgame.item.Items;
import games.absolutephoenix.phoenixgame.item.ToolItem;
import games.absolutephoenix.phoenixgame.item.ToolType;
import games.absolutephoenix.phoenixgame.level.Level;
import games.absolutephoenix.phoenixgame.level.tile.Tile;
import games.absolutephoenix.phoenixgame.level.tile.Tiles;

public class Wool extends Tile {
	private String woolType;

	public Wool(String name, Sprite sprite, String woolType) {
		super(name, sprite);
		this.woolType = woolType;
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Shovel) {
				if (player.payStamina(3 - tool.level) && tool.payDurability()) {
					level.setTile(xt, yt, Tiles.get("hole"));
					Sound.monsterHurt.play();
					level.dropItem(xt*16+8, yt*16+8, Items.get(woolType));
					return true;
				}
			}
		}
		return false;
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return e.canWool();
	}
}
