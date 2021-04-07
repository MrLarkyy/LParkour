package cz.larkyy.lparkour.listeners;

import cz.larkyy.lparkour.LParkour;
import cz.larkyy.lparkour.inventories.CreationMenu;
import cz.larkyy.lparkour.objects.EditingPlayer;
import cz.larkyy.lparkour.objects.LevelObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryClickListener implements Listener {

    private LParkour main;

    public InventoryClickListener(LParkour main) {
        this.main = main;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player))
            return;
        Player p = (Player) e.getWhoClicked();

        if (!main.getStorageHandler().getEditingPlayers().containsKey(p.getUniqueId())) {
            return;
        }
        EditingPlayer editingPlayer = main.getStorageHandler().getEditingPlayers().get(p.getUniqueId());
        e.setCancelled(true);

        if (e.getInventory().getHolder() instanceof CreationMenu) {

            if (e.getCurrentItem()==null || e.getCurrentItem().getType().equals(Material.AIR))
                return;
            ItemStack is = e.getCurrentItem();

            if (is.getItemMeta()==null)
                return;
            ItemMeta im = is.getItemMeta();

            editingPlayer.setDifficulty(LevelObject.Difficulty.valueOf(im.getLocalizedName().toUpperCase()));
            p.closeInventory();
            p.sendMessage("Difficulty set to "+im.getLocalizedName());

        }

    }
}
