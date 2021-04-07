package cz.larkyy.lparkour.inventories;

import cz.larkyy.lparkour.LParkour;
import cz.larkyy.lparkour.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CreationMenu implements InventoryHolder {

    private LParkour main;
    private Utils utils;
    private Inventory gui;
    private int page;

    public CreationMenu(LParkour main, int page) {
        this.main = main;
        this.page = page;
        this.utils = main.getUtils();
    }

    @Override
    public @NotNull Inventory getInventory() {

        gui = Bukkit.createInventory(this,27,"Choose Difficulty...");
        // DIFFICULTY, CREATORS, START, END
        solveItems();
        return gui;
    }
    private void solveItems() {

        ItemStack easy = utils.mkItem(Material.LIME_WOOL,"&a&lEASY","easy",null,null);
        ItemStack normal = utils.mkItem(Material.GREEN_WOOL,"&2&lNORMAL","normal",null,null);
        ItemStack medium = utils.mkItem(Material.YELLOW_WOOL,"&e&lMEDIUM","medium",null,null);
        ItemStack hard = utils.mkItem(Material.RED_WOOL,"&c&lHARD","hard",null,null);
        ItemStack impossible = utils.mkItem(Material.PURPLE_WOOL,"&5&lIMPOSSIBLE","impossible",null,null);

        gui.setItem(11,easy);
        gui.setItem(12,normal);
        gui.setItem(13,medium);
        gui.setItem(14,hard);
        gui.setItem(15,impossible);

    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
