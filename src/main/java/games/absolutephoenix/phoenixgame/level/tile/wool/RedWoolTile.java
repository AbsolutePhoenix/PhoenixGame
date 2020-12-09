package games.absolutephoenix.phoenixgame.level.tile.wool;

import games.absolutephoenix.phoenixgame.gfx.Sprite;

public class RedWoolTile extends Wool {
    private static Sprite sprite = new Sprite(10, 0, 2, 2, 1);

    public RedWoolTile(String name) {
        super(name, sprite, "Red Wool");
    }
}
