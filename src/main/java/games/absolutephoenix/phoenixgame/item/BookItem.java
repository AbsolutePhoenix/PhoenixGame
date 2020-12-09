package games.absolutephoenix.phoenixgame.item;

import java.util.ArrayList;

import games.absolutephoenix.phoenixgame.core.Game;
import games.absolutephoenix.phoenixgame.entity.Direction;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.gfx.Color;
import games.absolutephoenix.phoenixgame.gfx.Sprite;
import games.absolutephoenix.phoenixgame.level.Level;
import games.absolutephoenix.phoenixgame.level.tile.Tile;
import games.absolutephoenix.phoenixgame.screen.BookData;
import games.absolutephoenix.phoenixgame.screen.BookDisplay;

public class BookItem extends Item {
	
	protected static ArrayList<Item> getAllInstances() {
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(new BookItem("Book", new Sprite(0, 8, 0), null));
		items.add(new BookItem("Antidious", new Sprite(1, 8, 0), BookData.antVenomBook, true));
		return items;
	}
	
	protected String book; // TODO this is not saved yet; it could be, for editable books.
	private final boolean hasTitlePage;
	private Sprite sprite;
	
	private BookItem(String title, Sprite sprite, String book) { this(title, sprite, book, false); }
	private BookItem(String title, Sprite sprite, String book, boolean hasTitlePage) {
		super(title, sprite);
		this.book = book;
		this.hasTitlePage = hasTitlePage;
		this.sprite = sprite;
	}
	
	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, Direction attackDir) {
		Game.setMenu(new BookDisplay(book, hasTitlePage));
		return true;
	}
	
	@Override
	public boolean interactsWithWorld() { return false; }
	
	public BookItem clone() {
		return new BookItem(getName(), sprite, book, hasTitlePage);
	}
}
