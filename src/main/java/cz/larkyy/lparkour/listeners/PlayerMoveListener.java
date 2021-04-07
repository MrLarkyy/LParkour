package cz.larkyy.lparkour.listeners;

import cz.larkyy.lparkour.LParkour;
import cz.larkyy.lparkour.objects.LevelObject;
import cz.larkyy.lparkour.objects.PlayerObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final LParkour main;

    public PlayerMoveListener (LParkour main) {
        this.main = main;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        PlayerObject player = main.getStorageHandler().getPlayers().get(p.getUniqueId());

        if (!main.getStorageHandler().getPlayers().get(p.getUniqueId()).isInLevel()) {
            return;
        }

        LevelObject level = player.getActualLevel();
        Location end = level.getEnd();

        Location loc1 = e.getFrom().clone();
        Location loc2 = e.getTo().clone();

        loc1.setYaw(0);
        loc1.setPitch(0);
        loc2.setYaw(0);
        loc2.setPitch(0);

        loc1.setX(Math.floor(loc1.getX()));
        loc1.setY(Math.floor(loc1.getY()));
        loc1.setZ(Math.floor(loc1.getZ()));
        loc2.setX(Math.floor(loc2.getX()));
        loc2.setY(Math.floor(loc2.getY()));
        loc2.setZ(Math.floor(loc2.getZ()));

        if (loc1.equals(loc2))
            return;

        if (loc2.distance(end)<=0.5) {
            p.sendMessage("You have reached the end!");
            player.addTotal();
            player.addLevel(level);
            player.setActualLevel(null);
        }

    }
}
