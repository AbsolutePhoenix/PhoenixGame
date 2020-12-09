package games.absolutephoenix.phoenixgame.level.tile.wool;

import games.absolutephoenix.phoenixgame.gfx.Sprite;

public class NormalWoolTile extends Wool {
    private static Sprite sprite = new Sprite(8, 0, 2, 2, 1);

    public NormalWoolTile(String name) {
        super(name, sprite, "Wool");
    }
}
