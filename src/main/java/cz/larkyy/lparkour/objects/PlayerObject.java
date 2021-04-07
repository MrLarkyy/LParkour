package cz.larkyy.lparkour.objects;

import java.util.List;
import java.util.UUID;

public class PlayerObject {

    private List<LevelObject> levels;

    private int total;

    private UUID uuid;

    private boolean editing = false;

    private EditingType editingType = null;

    private LevelObject actualLevel;

    public enum EditingType {
        StartLoc,EndLoc
    }

    public PlayerObject(UUID uuid, List<LevelObject> levels, int total, LevelObject actualLevel) {
        this.levels = levels;
        this.uuid = uuid;
        this.total = total;
        this.actualLevel = actualLevel;
    }

    public boolean isInLevel() {
        return (actualLevel!=null);
    }

    public List<LevelObject> getLevels() {
        return levels;
    }

    public void addLevel(LevelObject level) {
        if (!levels.contains(level))
            this.levels.add(level);
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isEditing() {
        return editing;
    }

    public int getTotal() {
        return total;
    }

    public LevelObject getActualLevel() {
        return actualLevel;
    }

    public void addTotal() {
        this.total++;
    }

    public void setActualLevel(LevelObject actualLevel) {
        this.actualLevel = actualLevel;
    }

    public void setEditing(EditingType editingType) {
        this.editing = true;
        this.editingType = editingType;
    }
}
