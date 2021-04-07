package cz.larkyy.lparkour.storage;

import cz.larkyy.lparkour.LParkour;
import cz.larkyy.lparkour.objects.EditingPlayer;
import cz.larkyy.lparkour.objects.LevelObject;
import cz.larkyy.lparkour.objects.PlayerObject;

import java.util.*;

public class StorageHandler {

    private final LParkour main;
    private HashMap<String, LevelObject> levels = new HashMap<>();
    private HashMap<UUID, PlayerObject> players = new HashMap<>();
    private HashMap<UUID, EditingPlayer> editingPlayers = new HashMap<>();

    public StorageHandler(LParkour main) {
        this.main = main;
    }

    public void loadLevels() {
        this.levels = main.getDatabaseHandler().loadLevels();
    }

    public void loadPlayers() {
        main.getServer().getConsoleSender().sendMessage("Trying to load Player's data");
        this.players = main.getDatabaseHandler().loadPlayers();
    }

    public void saveLevels() {
        for (Map.Entry<String,LevelObject> pair : levels.entrySet()) {
            main.getDatabaseHandler().setData(pair.getValue());
        }
    }

    public void savePlayers() {
        for (Map.Entry<UUID,PlayerObject> pair : players.entrySet()) {
            main.getDatabaseHandler().setData(pair.getValue());
        }
    }

    public void addPlayer(PlayerObject player) {
        players.put(player.getUuid(),player);
    }

    public void addLevel(LevelObject level) {
        levels.put(level.getName(),level);
    }

    public HashMap<UUID, PlayerObject> getPlayers() {
        return players;
    }

    public HashMap<String, LevelObject> getLevels() {
        return levels;
    }

    public HashMap<UUID, EditingPlayer> getEditingPlayers() {
        return editingPlayers;
    }

    public void addEditingPlayer(EditingPlayer editingPlayer) {
        this.editingPlayers.put(editingPlayer.getUuid(),editingPlayer);
    }
    public void removeEditingPlayer(EditingPlayer editingPlayer) {
        this.editingPlayers.remove(editingPlayer.getUuid());
    }
}
