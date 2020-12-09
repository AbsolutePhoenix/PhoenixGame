package games.absolutephoenix.phoenixgame.level.tile;

import games.absolutephoenix.phoenixgame.core.io.Sound;
import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.Entity;
import games.absolutephoenix.phoenixgame.entity.mob.Mob;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.gfx.Color;
import games.absolutephoenix.phoenixgame.gfx.Sprite;
import games.absolutephoenix.phoenixgame.item.Item;
import games.absolutephoenix.phoenixgame.item.ToolItem;
import games.absolutephoenix.phoenixgame.item.ToolType;
import games.absolutephoenix.phoenixgame.level.Level;

public class LavaBrickTile extends Tile {
	private static Sprite sprite = new Sprite(19, 2, 2, 2, 1);
	
	protected LavaBrickTile(String name) {
		super(name, sprite);
	}
	
	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Pickaxe) {
				if (player.payStamina(4 - tool.level) && tool.payDurability()) {
					level.setTile(xt, yt, Tiles.get("lava"));
					Sound.monsterHurt.play();
					return true;
				}
			}
		}
		return false;
	}

	public void bumpedInto(Level level, int x, int y, Entity entity) {
		if(entity instanceof Mob)
			((Mob)entity).hurt(this, x, y, 3);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) { return e.canWool(); }
}
