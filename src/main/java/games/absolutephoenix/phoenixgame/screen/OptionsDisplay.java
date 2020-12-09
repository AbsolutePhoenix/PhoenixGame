package games.absolutephoenix.phoenixgame.screen;

import games.absolutephoenix.phoenixgame.core.Game;
import games.absolutephoenix.phoenixgame.core.io.Localization;
import games.absolutephoenix.phoenixgame.core.io.Settings;
import games.absolutephoenix.phoenixgame.saveload.Save;
import games.absolutephoenix.phoenixgame.screen.entry.SelectEntry;

public class OptionsDisplay extends Display {
	
	public OptionsDisplay() {
		super(true, new Menu.Builder(false, 6, RelPos.LEFT,
				Settings.getEntry("diff"),
				Settings.getEntry("fps"),
				Settings.getEntry("sound"),
				Settings.getEntry("autosave"),
				Settings.getEntry("skinon"),
				new SelectEntry("Change Key Bindings", () -> Game.setMenu(new KeyInputDisplay())),
				Settings.getEntry("language"),
				Settings.getEntry("textures")
			)
			.setTitle("Options")
			.createMenu()
		);
	}
	
	@Override
	public void onExit() {
		Localization.changeLanguage((String)Settings.get("language"));
		new Save();
		Game.MAX_FPS = (int)Settings.get("fps");
	}
}
