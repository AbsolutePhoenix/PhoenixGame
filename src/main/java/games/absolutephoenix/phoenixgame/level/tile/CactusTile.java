package games.absolutephoenix.phoenixgame.level.tile;

import games.absolutephoenix.phoenixgame.core.Game;
import games.absolutephoenix.phoenixgame.core.io.Settings;
import games.absolutephoenix.phoenixgame.core.io.Sound;
import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.Entity;
import games.absolutephoenix.phoenixgame.entity.mob.Mob;
import games.absolutephoenix.phoenixgame.entity.particle.SmashParticle;
import games.absolutephoenix.phoenixgame.entity.particle.TextParticle;
import games.absolutephoenix.phoenixgame.gfx.Color;
import games.absolutephoenix.phoenixgame.gfx.Screen;
import games.absolutephoenix.phoenixgame.gfx.Sprite;
import games.absolutephoenix.phoenixgame.item.Items;
import games.absolutephoenix.phoenixgame.level.Level;

public class CactusTile extends Tile {
	private static Sprite sprite = new Sprite(6, 0, 2, 2, 1);
	
	protected CactusTile(String name) {
		super(name, sprite);
		connectsToSand = true;
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return false;
	}

	public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
		int damage = level.getData(x, y) + dmg;
		int cHealth = 10;
		if (Game.isMode("creative")) dmg = damage = cHealth;
		level.add(new SmashParticle(x * 16, y * 16));
		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
		
		if (damage >= cHealth) {
			//int count = random.nextInt(2) + 2;
			level.setTile(x, y, Tiles.get("sand"));
			Sound.monsterHurt.play();
			level.dropItem(x*16+8, y*16+8, 2, 4, Items.get("Cactus"));
		} else {
			level.setData(x, y, damage);
		}
		return true;
	}

	@Override
	public void render(Screen screen, Level level, int x, int y) {
		Tiles.get("Sand").render(screen, level, x, y);

		sprite.render(screen, x<<4, y<<4);
	}

	public void bumpedInto(Level level, int x, int y, Entity entity) {
		if(!(entity instanceof Mob)) return;
		Mob m = (Mob) entity;
		if (Settings.get("diff").equals("Easy")) {
			m.hurt(this, x, y, 1);
		}
		if (Settings.get("diff").equals("Normal")) {
			m.hurt(this, x, y, 1);
		}
		if (Settings.get("diff").equals("Hard")) {
			m.hurt(this, x, y, 2);
		}
	}

	public void tick(Level level, int xt, int yt) {
		int damage = level.getData(xt, yt);
		if (damage > 0) level.setData(xt, yt, damage - 1);
	}
}
