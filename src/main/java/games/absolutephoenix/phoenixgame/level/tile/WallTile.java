package games.absolutephoenix.phoenixgame.level.tile;

import games.absolutephoenix.phoenixgame.core.Game;
import games.absolutephoenix.phoenixgame.core.io.Sound;
import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.Entity;
import games.absolutephoenix.phoenixgame.entity.mob.AirWizard;
import games.absolutephoenix.phoenixgame.entity.mob.Mob;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.entity.particle.SmashParticle;
import games.absolutephoenix.phoenixgame.entity.particle.TextParticle;
import games.absolutephoenix.phoenixgame.gfx.Color;
import games.absolutephoenix.phoenixgame.gfx.ConnectorSprite;
import games.absolutephoenix.phoenixgame.gfx.Sprite;
import games.absolutephoenix.phoenixgame.item.Item;
import games.absolutephoenix.phoenixgame.item.Items;
import games.absolutephoenix.phoenixgame.item.ToolItem;
import games.absolutephoenix.phoenixgame.item.ToolType;
import games.absolutephoenix.phoenixgame.level.Level;

public class WallTile extends Tile {
	
	private static final String obrickMsg = "The airwizard must be defeated first.";
	
	private ConnectorSprite sprite;
	
	protected Material type;
	
	protected WallTile(Material type) {
		super(type.name()+" Wall", (ConnectorSprite)null);
		this.type = type;
		switch(type) {
			case Wood:
				sprite = new ConnectorSprite(WallTile.class, new Sprite(0, 14, 3, 3, 1, 3), new Sprite(3, 14, 2, 2, 1, 3), new Sprite(1, 15, 2, 2, 1, 0, true));
				break;
			case Stone:
				sprite = new ConnectorSprite(WallTile.class, new Sprite(10, 14, 3, 3, 1, 3), new Sprite(13, 14, 2, 2, 1, 3), new Sprite(11, 15, 2, 2, 1, 0, true));
				break;
			case Obsidian:
				sprite = new ConnectorSprite(WallTile.class, new Sprite(20, 14, 3, 3, 1, 3), new Sprite(23, 14, 2, 2, 1, 3), new Sprite(21, 15, 2, 2, 1, 0, true));
				break;
		}
		csprite = sprite;
	}
	
	public boolean mayPass(Level level, int x, int y, Entity e) {
		return false;
	}
	
	@Override
	public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
		if(Game.isMode("creative") || level.depth != -3 || type != Material.Obsidian || AirWizard.beaten) {
			hurt(level, x, y, random.nextInt(6) / 6 * dmg / 2);
			return true;
		} else {
			Game.notifications.add(obrickMsg);
			return false;
		}
	}
	
	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		if(Game.isMode("creative"))
			return false; // go directly to hurt method
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Pickaxe) {
				if(level.depth != -3 || type != Material.Obsidian || AirWizard.beaten) {
						if (player.payStamina(4 - tool.level) && tool.payDurability()) {
							hurt(level, xt, yt, random.nextInt(10) + (tool.level) * 5 + 10);
							return true;
						}
				} else {
					Game.notifications.add(obrickMsg);
				}
			}
		}
		return false;
	}
	
	public void hurt(Level level, int x, int y, int dmg) {
		int damage = level.getData(x, y) + dmg;
		int sbwHealth = 100;
		if (Game.isMode("creative")) dmg = damage = sbwHealth;
		
		level.add(new SmashParticle(x * 16, y * 16));
		Sound.monsterHurt.play();

		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
		if (damage >= sbwHealth) {
			String itemName = "", tilename = "";
			switch(type) {
				case Wood: itemName = "Plank"; tilename = "Wood Planks"; break;
				case Stone: itemName = "Stone Brick"; tilename = "Stone Bricks"; break;
				case Obsidian: itemName = "Obsidian Brick"; tilename = "Obsidian"; break;
			}
			
			level.dropItem(x*16+8, y*16+8, 1, 3-type.ordinal(), Items.get(itemName));
			level.setTile(x, y, Tiles.get(tilename));
		}
		else {
			level.setData(x, y, damage);
		}
	}
	
	public void tick(Level level, int xt, int yt) {
		int damage = level.getData(xt, yt);
		if (damage > 0) level.setData(xt, yt, damage - 1);
	}
	
	public String getName(int data) {
		return Material.values[data].name() + " Wall";
	}
}
