package cz.larkyy.lparkour;

import cz.larkyy.lparkour.commands.MainCommand;
import cz.larkyy.lparkour.listeners.InventoryClickListener;
import cz.larkyy.lparkour.listeners.PlayerInteractListener;
import cz.larkyy.lparkour.listeners.PlayerJoinListener;
import cz.larkyy.lparkour.listeners.PlayerMoveListener;
import cz.larkyy.lparkour.runnables.EditingPlayerRunnable;
import cz.larkyy.lparkour.storage.DatabaseHandler;
import cz.larkyy.lparkour.storage.StorageHandler;
import cz.larkyy.lparkour.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class LParkour extends JavaPlugin {

    private DatabaseHandler databaseHandler;
    private StorageHandler storageHandler;
    private Utils utils;
    private boolean loaded = false;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        databaseHandler = new DatabaseHandler(this);
        storageHandler = new StorageHandler(this);

        new BukkitRunnable() {
            @Override
            public void run() {
                storageHandler.loadLevels();
                storageHandler.loadPlayers();
                loaded=true;
            }
        }.runTaskLater(this,5*20);

        utils = new Utils(this);

        getCommand("lparkour").setExecutor(new MainCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this),this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this),this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this),this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this),this);
        new EditingPlayerRunnable(this).runTaskTimerAsynchronously(this,60,20);
    }

    @Override
    public void onDisable() {
        storageHandler.saveLevels();
        storageHandler.savePlayers();
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public StorageHandler getStorageHandler() {
        return storageHandler;
    }

    public Utils getUtils() {
        return utils;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
