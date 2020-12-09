package games.absolutephoenix.phoenixgame.entity.furniture;

import java.util.ArrayList;

import games.absolutephoenix.phoenixgame.core.Game;
import games.absolutephoenix.phoenixgame.entity.mob.Player;
import games.absolutephoenix.phoenixgame.gfx.Sprite;
import games.absolutephoenix.phoenixgame.item.Recipe;
import games.absolutephoenix.phoenixgame.item.Recipes;
import games.absolutephoenix.phoenixgame.screen.CraftingDisplay;

public class Crafter extends Furniture {
	
	public enum Type {
		Workbench (new Sprite(16, 26, 2, 2, 2), 3, 2, Recipes.workbenchRecipes),
		Oven (new Sprite(12, 26, 2, 2, 2), 3, 2, Recipes.ovenRecipes),
		Furnace (new Sprite(14, 26, 2, 2, 2), 3, 2, Recipes.furnaceRecipes),
		Anvil (new Sprite(8, 26, 2, 2, 2), 3, 2, Recipes.anvilRecipes),
		Enchanter (new Sprite(24, 26, 2, 2, 2), 7, 2, Recipes.enchantRecipes),
		Loom (new Sprite(26, 26, 2, 2, 2), 7, 2, Recipes.loomRecipes);
		
		public ArrayList<Recipe> recipes;
		protected Sprite sprite;
		protected int xr, yr;
		
		
		Type(Sprite sprite, int xr, int yr, ArrayList<Recipe> list) {
			this.sprite = sprite;
			this.xr = xr;
			this.yr = yr;
			recipes = list;
			Crafter.names.add(this.name());
		}
	}
	public static ArrayList<String> names = new ArrayList<>();
	
	public Type type;
	
	/**
	 * Creates a crafter of a given type.
	 * @param type What type of crafter this is.
	 */
	public Crafter(Type type) {
		super(type.name(), type.sprite, type.xr, type.yr);
		this.type = type;
	}
	
	public boolean use(Player player) {
		Game.setMenu(new CraftingDisplay(type.recipes, type.name(), player));
		return true;
	}
	
	@Override
	public Furniture clone() {
		return new Crafter(type);
	}
	
	@Override
	public String toString() { return type.name()+getDataPrints(); }
}
