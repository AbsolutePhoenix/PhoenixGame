package games.absolutephoenix.phoenixgame.screen.entry;

import games.absolutephoenix.phoenixgame.core.io.InputHandler;
import games.absolutephoenix.phoenixgame.gfx.Screen;
import games.absolutephoenix.phoenixgame.gfx.SpriteSheet;

public class BlankEntry extends ListEntry {
	
	public BlankEntry() {
		setSelectable(false);
	}
	
	@Override
	public void tick(InputHandler input) {}
	
	@Override
	public void render(Screen screen, int x, int y, boolean isSelected) {}
	
	@Override
	public int getWidth() {
		return SpriteSheet.boxWidth;
	}
	
	@Override
	public String toString() { return " "; }
}
