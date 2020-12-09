package games.absolutephoenix.phoenixgame.screen;

import games.absolutephoenix.phoenixgame.core.Game;
import games.absolutephoenix.phoenixgame.core.io.InputHandler;
import games.absolutephoenix.phoenixgame.entity.Entity;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.item.Inventory;
import games.absolutephoenix.phoenixgame.item.Item;
import games.absolutephoenix.phoenixgame.item.StackableItem;
import games.absolutephoenix.phoenixgame.screen.entry.ItemEntry;

class InventoryMenu extends ItemListMenu {
	
	private Inventory inv;
	private Entity holder;
	
	InventoryMenu(Entity holder, Inventory inv, String title) {
		super(ItemEntry.useItems(inv.getItems()), title);
		this.inv = inv;
		this.holder = holder;
	}
	
	InventoryMenu(InventoryMenu model) {
		super(ItemEntry.useItems(model.inv.getItems()), model.getTitle());
		this.inv = model.inv;
		this.holder = model.holder;
		setSelection(model.getSelection());
	}
	
	@Override
	public void tick(InputHandler input) {
		super.tick(input);
		
		boolean dropOne = input.getKey("drop-one").clicked && !(Game.getMenu() instanceof ContainerDisplay);
		
		if(getNumOptions() > 0 && (dropOne || input.getKey("drop-stack").clicked)) {
			ItemEntry entry = ((ItemEntry)getCurEntry());
			if(entry == null) return;
			Item invItem = entry.getItem();
			Item drop = invItem.clone();
			
			if(dropOne && drop instanceof StackableItem && ((StackableItem)drop).count > 1) {
				// just drop one from the stack
				((StackableItem)drop).count = 1;
				((StackableItem)invItem).count--;
			} else {
				// drop the whole item.
				if(!Game.isMode("creative") || !(holder instanceof Player))
					removeSelectedEntry();
			}
			
			if(holder.getLevel() != null) {
				if(Game.isValidClient())
					Game.client.dropItem(drop);
				else
					holder.getLevel().dropItem(holder.x, holder.y, drop);
			}
		}
	}
	
	@Override
	public void removeSelectedEntry() {
		inv.remove(getSelection());
		super.removeSelectedEntry();
	}
}
