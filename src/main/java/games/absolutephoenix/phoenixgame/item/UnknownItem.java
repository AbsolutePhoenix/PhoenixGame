package games.absolutephoenix.phoenixgame.item;

import games.absolutephoenix.phoenixgame.gfx.Sprite;

public class UnknownItem extends StackableItem {
	
	protected UnknownItem(String reqName) {
		super(reqName, Sprite.missingTexture(1, 1));
	}
	
	public UnknownItem clone() {
		return new UnknownItem(getName());
	}
}
