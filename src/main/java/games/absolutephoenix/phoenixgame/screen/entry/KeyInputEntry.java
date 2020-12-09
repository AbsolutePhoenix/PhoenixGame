package games.absolutephoenix.phoenixgame.screen.entry;

import games.absolutephoenix.phoenixgame.core.io.InputHandler;
import games.absolutephoenix.phoenixgame.core.io.Localization;
import games.absolutephoenix.phoenixgame.gfx.Font;
import games.absolutephoenix.phoenixgame.gfx.Screen;

public class KeyInputEntry extends SelectEntry {
	
	private String action, mapping, buffer;
	
	public KeyInputEntry(String key) {
		super("", null);
		
		this.action = key.substring(0, key.indexOf(";"));
		setMapping(key.substring(key.indexOf(";")+1));
	}
	
	private void setMapping(String mapping) {
		this.mapping = mapping;
		
		StringBuilder buffer = new StringBuilder();
		for(int spaces = 0; spaces < Screen.w/Font.textWidth(" ") - action.length() - mapping.length(); spaces++)
			buffer.append(" ");
		
		this.buffer = buffer.toString();
	}
	
	@Override
	public void tick(InputHandler input) {
		if(input.getKey("c").clicked || input.getKey("enter").clicked)
			input.changeKeyBinding(action);
		else if(input.getKey("a").clicked)
			// add a binding, don't remove previous.
			input.addKeyBinding(action);
	}
	
	@Override
	public int getWidth() {
		return Screen.w;
	}
	
	@Override
	public String toString() {
		return Localization.getLocalized(action) + buffer + mapping;
	}
}
