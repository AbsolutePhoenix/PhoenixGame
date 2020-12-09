package games.absolutephoenix.phoenixgame.screen;

import javax.swing.Timer;

import games.absolutephoenix.phoenixgame.core.Game;
import games.absolutephoenix.phoenixgame.core.World;
import games.absolutephoenix.phoenixgame.core.io.Localization;
import games.absolutephoenix.phoenixgame.gfx.Color;
import games.absolutephoenix.phoenixgame.gfx.Ellipsis;
import games.absolutephoenix.phoenixgame.gfx.Ellipsis.DotUpdater.TimeUpdater;
import games.absolutephoenix.phoenixgame.gfx.Ellipsis.SmoothEllipsis;
import games.absolutephoenix.phoenixgame.gfx.Font;
import games.absolutephoenix.phoenixgame.gfx.FontStyle;
import games.absolutephoenix.phoenixgame.gfx.Screen;
import games.absolutephoenix.phoenixgame.saveload.Save;

public class LoadingDisplay extends Display {
	
	private static float percentage = 0;
	private static String progressType = "";
	
	private Timer t;
	private String msg = "";
	private Ellipsis ellipsis = new SmoothEllipsis(new TimeUpdater());
	
	public LoadingDisplay() {
		super(true, false);
		t = new Timer(500, e -> {
			World.initWorld();
			Game.setMenu(null);
		});
		t.setRepeats(false);
	}
	
	@Override
	public void init(Display parent) {
		super.init(parent);
		percentage = 0;
		progressType = "World";
		if(WorldSelectDisplay.loadedWorld())
			msg = "Loading";
		else
			msg = "Generating";
		t.start();
	}
	
	@Override
	public void onExit() {
		percentage = 0;
		if(!WorldSelectDisplay.loadedWorld()) {
			msg = "Saving";
			progressType = "World";
			new Save(WorldSelectDisplay.getWorldName());
			Game.notifications.clear();
		}
	}
	
	public static void setPercentage(float percent) {
		percentage = percent;
	}
	public static float getPercentage() { return percentage; }
	public static void setMessage(String progressType) { LoadingDisplay.progressType = progressType; }
	
	public static void progress(float amt) {
		percentage = Math.min(100, percentage + amt);
	}
	
	@Override
	public void render(Screen screen) {
		super.render(screen);
		int percent = Math.round(percentage);
		Font.drawParagraph(screen, new FontStyle(Color.RED), 6,
			Localization.getLocalized(msg)+(progressType.length()>0?" "+Localization.getLocalized(progressType):"")+ ellipsis.updateAndGet(),
			percent+"%"
		);
	}
}
