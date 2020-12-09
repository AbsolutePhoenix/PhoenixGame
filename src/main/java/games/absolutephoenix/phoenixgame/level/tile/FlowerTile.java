package games.absolutephoenix.phoenixgame.level.tile;

import games.absolutephoenix.phoenixgame.core.io.Sound;
import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.mob.Mob;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.gfx.ConnectorSprite;
import games.absolutephoenix.phoenixgame.gfx.Screen;
import games.absolutephoenix.phoenixgame.gfx.Sprite;
import games.absolutephoenix.phoenixgame.item.Item;
import games.absolutephoenix.phoenixgame.item.Items;
import games.absolutephoenix.phoenixgame.item.ToolItem;
import games.absolutephoenix.phoenixgame.item.ToolType;
import games.absolutephoenix.phoenixgame.level.Level;

public class FlowerTile extends Tile {
	private static Sprite flowerSprite = new Sprite(3, 8, 1);
	
	protected FlowerTile(String name) {
		super(name, (ConnectorSprite)null);
		connectsToGrass = true;
		maySpawn = true;
	}

	public void tick(Level level, int xt, int yt) {
		// TODO revise this method.
		if (random.nextInt(30) != 0) return;

		int xn = xt;
		int yn = yt;

		if (random.nextBoolean()) xn += random.nextInt(2) * 2 - 1;
		else yn += random.nextInt(2) * 2 - 1;

		if (level.getTile(xn, yn) == Tiles.get("dirt")) {
			level.setTile(xn, yn, Tiles.get("grass"));
		}
	}
	
	public void render(Screen screen, Level level, int x, int y) {
		Tiles.get("grass").render(screen, level, x, y);
		
		int data = level.getData(x, y);
		int shape = (data / 16) % 2;
		
		x = x << 4;
		y = y << 4;
		
		flowerSprite.render(screen, x + 8*shape, y);
		flowerSprite.render(screen, x + 8*(shape==0?1:0), y + 8);
	}

	public boolean interact(Level level, int x, int y, Player player, Item item, Direction attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Shovel) {
				if (player.payStamina(2 - tool.level) && tool.payDurability()) {
					level.setTile(x, y, Tiles.get("grass"));
					Sound.monsterHurt.play();
					level.dropItem(x*16+8, y*16+8, Items.get("Flower"));
					level.dropItem(x*16+8, y*16+8, Items.get("Rose"));
					return true;
				}
			}
		}
		return false;
	}

	public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
		level.dropItem(x*16+8, y*16+8, 1, 2, Items.get("Flower"));
		level.dropItem(x*16+8, y*16+8, 0, 1, Items.get("Rose"));
		level.setTile(x, y, Tiles.get("grass"));
		return true;
	}
}
