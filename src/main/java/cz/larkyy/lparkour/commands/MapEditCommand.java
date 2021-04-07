package cz.larkyy.lparkour.commands;

import cz.larkyy.lparkour.LParkour;
import cz.larkyy.lparkour.objects.EditingPlayer;
import cz.larkyy.lparkour.objects.LevelObject;
import cz.larkyy.lparkour.storage.StorageHandler;
import cz.larkyy.lparkour.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapEditCommand {

    private final MainCommand mainCommand;

    public MapEditCommand(MainCommand mainCommand, CommandSender sender, String[] args) {
        this.mainCommand = mainCommand;

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (getStorageHandler().getEditingPlayers().containsKey(p.getUniqueId())) {
                return;
            }

            if (args.length > 1) {

                if (!getStorageHandler().getLevels().containsKey(args[1])) {
                    p.sendMessage("There is no Map with this Name!");
                    return;
                }

                LevelObject level = getStorageHandler().getLevels().get(args[1]);

                EditingPlayer editingPlayer = new EditingPlayer(p,level);
                getStorageHandler().addEditingPlayer(editingPlayer);

                getUtils().prepareEditingContents(p.getInventory());

            }

        } else
            sender.sendMessage("Only players can execute this command!");
    }

    private LParkour getMain() {
        return mainCommand.getMain();
    }

    private StorageHandler getStorageHandler() {
        return getMain().getStorageHandler();
    }

    private Utils getUtils() {
        return mainCommand.getMain().getUtils();
    }
}
