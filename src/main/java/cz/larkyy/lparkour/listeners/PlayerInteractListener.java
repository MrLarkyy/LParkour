package cz.larkyy.lparkour.listeners;

import cz.larkyy.lparkour.LParkour;
import cz.larkyy.lparkour.inventories.CreationMenu;
import cz.larkyy.lparkour.objects.EditingPlayer;
import cz.larkyy.lparkour.objects.LevelObject;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerInteractListener implements Listener {

    private final LParkour main;

    public PlayerInteractListener(LParkour main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!main.getStorageHandler().getEditingPlayers().containsKey(p.getUniqueId()))
            return;

        e.setCancelled(true);

        EditingPlayer editingPlayer = main.getStorageHandler().getEditingPlayers().get(p.getUniqueId());

        if (!e.getHand().equals(EquipmentSlot.HAND))
            return;

        if (e.getItem()==null || e.getItem().getType().equals(Material.AIR))
            return;

        ItemStack is = e.getItem();

        if (is.getItemMeta()==null) {
            return;
        }

        ItemMeta im = is.getItemMeta();

        if (im.getLocalizedName().equals("difficulty")) {
            p.openInventory(new CreationMenu(main, 0).getInventory());
        }
        if (im.getLocalizedName().equals("start")) {
            editingPlayer.setStart(p.getLocation());
            p.sendMessage("You have set the Start Location!");
        }
        if (im.getLocalizedName().equals("end")) {
            editingPlayer.setEnd(p.getLocation());
            p.sendMessage("You have set the End Location!");
        }
        if (im.getLocalizedName().equals("finish")) {
            if (editingPlayer.getDifficulty()==null) {
                p.sendMessage("You must set the Difficulty!");
                return;
            }
            if (editingPlayer.getStart()==null) {
                p.sendMessage("You must set the Start location!");
                return;
            }
            if (editingPlayer.getEnd()==null) {
                p.sendMessage("You must set the End location!");
                return;
            }

            if (editingPlayer.getLevel()==null) {
                addLevel(p, editingPlayer);
            } else {
                editLevel(p,editingPlayer);
            }
        }
    }

    private void editLevel(Player p, EditingPlayer editingPlayer) {
        LevelObject level = editingPlayer.getLevel();

        level.setName(editingPlayer.getName());
        level.setDifficulty(editingPlayer.getDifficulty());
        level.setStart(editingPlayer.getStart());
        level.setEnd(editingPlayer.getEnd());
        level.setCreators(editingPlayer.getCreators());

        p.sendMessage("Level has been edited!");
        p.getInventory().setContents(editingPlayer.getInventory());
        main.getStorageHandler().removeEditingPlayer(editingPlayer);

    }

    private void addLevel(Player p, EditingPlayer editingPlayer) {

        Location end = editingPlayer.getEnd().clone();
        end.setX(Math.floor(end.getX()));
        end.setY(Math.floor(end.getY()));
        end.setZ(Math.floor(end.getZ()));
        end.setYaw(0);
        end.setPitch(0);

        LevelObject level = new LevelObject(
                main,
                editingPlayer.getName(),
                editingPlayer.getDifficulty(),
                new HashMap<>(),
                new ArrayList<>(),
                editingPlayer.getStart(),
                end
        );

        main.getStorageHandler().addLevel(level);

        p.sendMessage("Level has been added!");
        p.getInventory().setContents(editingPlayer.getInventory());
        main.getStorageHandler().removeEditingPlayer(editingPlayer);

    }
}
