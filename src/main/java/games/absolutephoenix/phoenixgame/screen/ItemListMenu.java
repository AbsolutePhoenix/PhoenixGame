package games.absolutephoenix.phoenixgame.screen;

import games.absolutephoenix.phoenixgame.gfx.Point;
import games.absolutephoenix.phoenixgame.screen.entry.ItemEntry;

class ItemListMenu extends Menu {
	
	static Builder getBuilder() {
		return new Builder(true, 0, RelPos.LEFT)
			.setPositioning(new Point(9, 9), RelPos.BOTTOM_RIGHT)
			.setDisplayLength(9)
			.setSelectable(true)
			.setScrollPolicies(1, false);
	}
	
	ItemListMenu(Builder b, ItemEntry[] entries, String title) {
		super(b
			.setEntries(entries)
			.setTitle(title)
			.createMenu()
		);
	}
	
	ItemListMenu(ItemEntry[] entries, String title) {
		this(getBuilder(), entries, title);
	}
}
