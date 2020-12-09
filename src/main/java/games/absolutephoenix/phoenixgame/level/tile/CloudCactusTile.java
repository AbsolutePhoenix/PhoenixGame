package games.absolutephoenix.phoenixgame.level.tile;

import games.absolutephoenix.phoenixgame.core.Game;
import games.absolutephoenix.phoenixgame.core.io.Settings;
import games.absolutephoenix.phoenixgame.core.io.Sound;
import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.Entity;
import games.absolutephoenix.phoenixgame.entity.mob.AirWizard;
import games.absolutephoenix.phoenixgame.entity.mob.Mob;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.entity.particle.SmashParticle;
import games.absolutephoenix.phoenixgame.entity.particle.TextParticle;
import games.absolutephoenix.phoenixgame.gfx.Color;
import games.absolutephoenix.phoenixgame.gfx.Sprite;
import games.absolutephoenix.phoenixgame.item.Item;
import games.absolutephoenix.phoenixgame.item.ToolItem;
import games.absolutephoenix.phoenixgame.item.ToolType;
import games.absolutephoenix.phoenixgame.level.Level;

public class CloudCactusTile extends Tile {
	private static Sprite sprite = new Sprite(6, 2, 2, 2, 1);
	
	protected CloudCactusTile(String name) {
		super(name, sprite);
	}
	
	public boolean mayPass(Level level, int x, int y, Entity e) {
		return e instanceof AirWizard;
	}

	public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
		hurt(level, x, y, 0);
		return true;
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		if(Game.isMode("creative"))
			return false; // go directly to hurt method
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.Pickaxe) {
				if (player.payStamina(6 - tool.level) && tool.payDurability()) {
					hurt(level, xt, yt, 1);
					return true;
				}
			}
		}
		return false;
	}
	
	public void hurt(Level level, int x, int y, int dmg) {
		int damage = level.getData(x, y) + dmg;
		int health = 10;
		if(Game.isMode("creative")) dmg = damage = health;
		level.add(new SmashParticle(x * 16, y * 16));
		Sound.monsterHurt.play();

		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
		if (damage >= health) {
			level.setTile(x, y, Tiles.get("cloud"));
		} else
			level.setData(x, y, damage);
	}

	public void bumpedInto(Level level, int x, int y, Entity entity) {
		if (entity instanceof AirWizard) return;
		
		if(entity instanceof Mob)
			((Mob)entity).hurt(this, x, y, 1+Settings.getIdx("diff"));
	}
}
