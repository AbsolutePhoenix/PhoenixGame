package games.absolutephoenix.phoenixgame.level.tile;

import games.absolutephoenix.phoenixgame.core.io.Sound;
import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.Entity;
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

public class SandTile extends Tile {
	static Sprite steppedOn, normal = new Sprite(9, 6, 2, 2, 1);
	static {
		Sprite.Px[][] pixels = new Sprite.Px[2][2];
		pixels[0][0] = new Sprite.Px(9, 8, 0, 1);
		pixels[0][1] = new Sprite.Px(10, 6, 0, 1);
		pixels[1][0] = new Sprite.Px(9, 7, 0, 1);
		pixels[1][1] = new Sprite.Px(9, 8, 0, 1);
		steppedOn = new Sprite(pixels);
	}
	
	private ConnectorSprite sprite = new ConnectorSprite(SandTile.class, new Sprite(6, 6, 3, 3, 1, 3), normal)
	{
		public boolean connectsTo(Tile tile, boolean isSide) {
			if(!isSide) return true;
			return tile.connectsToSand;
		}
	};
	
	protected SandTile(String name) {
		super(name, (ConnectorSprite)null);
		csprite = sprite;
		connectsToSand = true;
		maySpawn = true;
	}
	
	public void render(Screen screen, Level level, int x, int y) {
		boolean steppedOn = level.getData(x, y) > 0;
		
		if(steppedOn) csprite.full = SandTile.steppedOn;
		else csprite.full = SandTile.normal;

		csprite.sparse.color = DirtTile.dCol(level.depth);
		
		csprite.render(screen, level, x, y);
	}

	public void tick(Level level, int x, int y) {
		int d = level.getData(x, y);
		if (d > 0) level.setData(x, y, d - 1);
	}

	public void steppedOn(Level level, int x, int y, Entity entity) {
		if (entity instanceof Mob) {
			level.setData(x, y, 10);
		}
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Shovel) {
				if (player.payStamina(4 - tool.level) && tool.payDurability()) {
					level.setTile(xt, yt, Tiles.get("hole"));
					Sound.monsterHurt.play();
					level.dropItem(xt*16+8, yt*16+8, Items.get("sand"));
					return true;
				}
			}
		}
		return false;
	}
}
