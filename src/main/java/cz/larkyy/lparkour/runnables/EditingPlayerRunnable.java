package cz.larkyy.lparkour.runnables;

import cz.larkyy.lparkour.LParkour;
import cz.larkyy.lparkour.objects.EditingPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class EditingPlayerRunnable extends BukkitRunnable {

    private final LParkour main;

    public EditingPlayerRunnable(LParkour main) {
        this.main = main;
    }

    @Override
    public void run() {
        if (main.getStorageHandler().getEditingPlayers()!=null) {
            for (Map.Entry<UUID, EditingPlayer> pair : main.getStorageHandler().getEditingPlayers().entrySet()) {
                EditingPlayer editingPlayer = pair.getValue();

                if (Bukkit.getPlayer(editingPlayer.getUuid())!=null) {
                    Player p = Bukkit.getPlayer(editingPlayer.getUuid());
                    if (editingPlayer.getStart() != null) {
                        p.spawnParticle(Particle.VILLAGER_HAPPY,editingPlayer.getStart(),7,0.3,0.3,0.3);
                    }

                    if (editingPlayer.getEnd() != null) {
                        Particle.DustOptions dust = new Particle.DustOptions(
                                Color.fromRGB(255, 0, 0), 1);

                        p.spawnParticle(Particle.REDSTONE,editingPlayer.getEnd(),7,0.3,0.3,0.3,dust);
                    }
                }
            }
        }
    }
}
