package games.absolutephoenix.phoenixgame.level.tile.farming;

import games.absolutephoenix.phoenixgame.core.io.Sound;
import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.Entity;
import games.absolutephoenix.phoenixgame.entity.ItemEntity;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.gfx.Sprite;
import games.absolutephoenix.phoenixgame.item.Item;
import games.absolutephoenix.phoenixgame.item.ToolItem;
import games.absolutephoenix.phoenixgame.item.ToolType;
import games.absolutephoenix.phoenixgame.level.Level;
import games.absolutephoenix.phoenixgame.level.tile.Tile;
import games.absolutephoenix.phoenixgame.level.tile.Tiles;

public class FarmTile extends Tile {
    private static Sprite sprite = new Sprite(12, 0, 2, 2, 1, true, new int[][] {{1, 0}, {0, 1}});

    public FarmTile(String name) {
        super(name, sprite);
    }
    protected FarmTile(String name, Sprite sprite) {
        super(name, sprite);
    }

    @Override
    public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            if (tool.type == ToolType.Shovel) {
                if (player.payStamina(4 - tool.level) && tool.payDurability()) {
                    level.setTile(xt, yt, Tiles.get("Dirt"));
                    Sound.monsterHurt.play();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void tick(Level level, int xt, int yt) {
        int age = level.getData(xt, yt);
        if (age < 5) level.setData(xt, yt, age + 1);
    }

    @Override
    public void steppedOn(Level level, int xt, int yt, Entity entity) {
        if (entity instanceof ItemEntity) return;
        if (random.nextInt(60) != 0) return;
        if (level.getData(xt, yt) < 5) return;
        level.setTile(xt, yt, Tiles.get("Dirt"));
    }
}
