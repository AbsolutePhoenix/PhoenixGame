package games.absolutephoenix.phoenixgame.level.tile;

import games.absolutephoenix.phoenixgame.core.io.Sound;
import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.Entity;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.gfx.Color;
import games.absolutephoenix.phoenixgame.gfx.ConnectorSprite;
import games.absolutephoenix.phoenixgame.gfx.Sprite;
import games.absolutephoenix.phoenixgame.item.Item;
import games.absolutephoenix.phoenixgame.item.Items;
import games.absolutephoenix.phoenixgame.item.ToolItem;
import games.absolutephoenix.phoenixgame.item.ToolType;
import games.absolutephoenix.phoenixgame.level.Level;

public class CloudTile extends Tile {
	private static ConnectorSprite sprite = new ConnectorSprite(CloudTile.class, new Sprite(0, 22, 3, 3, 1, 3), new Sprite(3, 24, 2, 2, 1, 3), new Sprite(3, 22, 2, 2, 1))
	{
		public boolean connectsTo(Tile tile, boolean isSide) {
			return tile != Tiles.get("Infinite Fall");
		}
	};
	
	protected CloudTile(String name) {
		super(name, sprite);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return true;
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		// we don't want the tile to break when attacked with just anything, even in creative mode
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Shovel && player.payStamina(5)) {
				level.setTile(xt, yt, Tiles.get("Infinite Fall")); // would allow you to shovel cloud, I think.
				Sound.monsterHurt.play();
				level.dropItem(xt*16+8, yt*16+8, 1, 3, Items.get("cloud"));
				return true;
			}
		}
		return false;
	}
}
