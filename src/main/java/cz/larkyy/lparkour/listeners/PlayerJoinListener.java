package cz.larkyy.lparkour.listeners;

import cz.larkyy.lparkour.LParkour;
import cz.larkyy.lparkour.objects.PlayerObject;
import cz.larkyy.lparkour.storage.StorageHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class PlayerJoinListener implements Listener {

    private final LParkour main;

    public PlayerJoinListener(LParkour main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!main.isLoaded()) {
            p.kick(Component.newline().content("§c[Parkour] Plugins isn't loaded yet!"));
            return;
        }

        if (!getStorageHandler().getPlayers().containsKey(p.getUniqueId())) {
            PlayerObject player = new PlayerObject(
                    p.getUniqueId(),
                    new ArrayList<>(),
                    0,
                    null
            );
            p.sendMessage("You have been added to the DB");
            getStorageHandler().addPlayer(player);
        }
        else
            p.sendMessage("You are already in the DB");
    }

    private StorageHandler getStorageHandler() {
        return main.getStorageHandler();
    }

}
