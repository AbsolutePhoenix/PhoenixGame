package games.absolutephoenix.phoenixgame.entity.mob;

import games.absolutephoenix.phoenixgame.core.io.Settings;
import games.absolutephoenix.phoenixgame.entity.Entity;
import games.absolutephoenix.phoenixgame.gfx.Color;
import games.absolutephoenix.phoenixgame.gfx.MobSprite;
import games.absolutephoenix.phoenixgame.item.Items;

public class Snake extends EnemyMob {
	private static MobSprite[][][] sprites;
	static {
		sprites = new MobSprite[4][4][2];
		for (int i = 0; i < 4; i++) {
			MobSprite[][] list  = MobSprite.compileMobSpriteAnimations(8, 8 + (i * 2));
			sprites[i] = list;
		}
	}
	
	public Snake(int lvl) {
		super(lvl, sprites, lvl>1?8:7, 100);
	}
	
	@Override
	protected void touchedBy(Entity entity) {
		if(entity instanceof Player) {
			int damage = lvl + Settings.getIdx("diff");
			((Player)entity).hurt(this, damage);
		}
	}
	
	public void die() {
		int num = Settings.get("diff").equals("Hard") ? 1 : 0;
		dropItem(num, num+1, Items.get("scale"));
		
		if(random.nextInt(24/lvl/(Settings.getIdx("diff")+1)) == 0)
			dropItem(1, 1, Items.get("key"));
		
		super.die();
	}
}
