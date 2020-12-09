package games.absolutephoenix.phoenixgame.screen;

import games.absolutephoenix.phoenixgame.core.Game;
import games.absolutephoenix.phoenixgame.core.io.InputHandler;
import games.absolutephoenix.phoenixgame.entity.mob.Player;

public class PlayerInvDisplay extends Display {
	
	private Player player;
	
	public PlayerInvDisplay(Player player) {
		super(new InventoryMenu(player, player.getInventory(), "Inventory"));
		this.player = player;
	}
	
	@Override
	public void tick(InputHandler input) {
		super.tick(input);
		
		if(input.getKey("menu").clicked) {
			Game.exitMenu();
			return;
		}
		
		if(input.getKey("attack").clicked && menus[0].getNumOptions() > 0) {
			player.activeItem = player.getInventory().remove(menus[0].getSelection());
			Game.exitMenu();
		}
	}
}
