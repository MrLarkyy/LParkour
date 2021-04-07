package cz.larkyy.lparkour.objects;

import cz.larkyy.lparkour.LParkour;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LevelObject {

    private Difficulty difficulty;

    private String name;

    private HashMap<UUID,Long> times;

    private List<UUID> creators;

    private Location start;
    private Location end;

    private ItemStack icon;

    private final LParkour main;

    public enum Difficulty{
        EASY,NORMAL,MEDIUM,HARD,IMPOSSIBLE
    }

    public LevelObject(LParkour main, String name, Difficulty difficulty, HashMap<UUID,Long> times, List<UUID> creators, Location start, Location end) {
        this.main = main;
        this.name = name;
        this.difficulty = difficulty;
        this.times = times;
        this.creators = creators;
        this.start = start;
        this.end = end;
        this.icon = defaultIcon();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnd(Location end) {
        this.end = end;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setCreators(List<UUID> creators) {
        this.creators = creators;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public HashMap<UUID, Long> getTimes() {
        return times;
    }

    public List<UUID> getCreators() {
        return creators;
    }

    public Location getEnd() {
        return end;
    }

    public Location getStart() {
        return start;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    private ItemStack defaultIcon() {
        return main.getUtils().mkItem(
                Material.PAPER,
                "&eLevel "+name,
                name,
                null,
                null);
    }
}
