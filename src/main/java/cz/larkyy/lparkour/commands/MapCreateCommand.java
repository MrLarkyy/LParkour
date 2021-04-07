package cz.larkyy.lparkour.commands;

import cz.larkyy.lparkour.LParkour;
import cz.larkyy.lparkour.objects.EditingPlayer;
import cz.larkyy.lparkour.storage.StorageHandler;
import cz.larkyy.lparkour.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapCreateCommand {

    private final MainCommand mainCommand;

    public MapCreateCommand(MainCommand mainCommand, CommandSender sender, String[] args) {
        this.mainCommand = mainCommand;

        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (getStorageHandler().getEditingPlayers().containsKey(p.getUniqueId())) {
                return;
            }

            if (args.length > 1) {

                if (getStorageHandler().getLevels().containsKey(args[1])) {
                    p.sendMessage("There is already a level with this Name!");
                    return;
                }

                EditingPlayer editingPlayer = new EditingPlayer(p,args[1]);
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
