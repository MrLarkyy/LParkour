package cz.larkyy.lparkour.objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class EditingPlayer {

    private final UUID uuid;

    private final String name;

    private LevelObject.Difficulty difficulty;

    private Location start;

    private Location end;

    private List<UUID> creators;

    private LevelObject level = null;

    private ItemStack[] inventory;

    public EditingPlayer(Player p, String name) {
        this.name = name;
        this.uuid = p.getUniqueId();
        this.inventory = p.getInventory().getContents();
    }

    public EditingPlayer(Player p, LevelObject level) {
        this.level = level;
        this.name = level.getName();
        this.difficulty = level.getDifficulty();
        this.start = level.getStart();
        this.end = level.getEnd();
        this.creators = getCreators();
        this.uuid = p.getUniqueId();
        this.inventory = p.getInventory().getContents();
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

    public List<UUID> getCreators() {
        return creators;
    }

    public LevelObject.Difficulty getDifficulty() {
        return difficulty;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setCreators(List<UUID> creators) {
        this.creators = creators;
    }

    public void setDifficulty(LevelObject.Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setEnd(Location end) {
        this.end = end;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public LevelObject getLevel() {
        return level;
    }
}
