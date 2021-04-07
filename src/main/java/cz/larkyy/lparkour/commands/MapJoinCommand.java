package cz.larkyy.lparkour.commands;

import cz.larkyy.lparkour.LParkour;
import cz.larkyy.lparkour.objects.LevelObject;
import cz.larkyy.lparkour.objects.PlayerObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapJoinCommand {

    private final MainCommand mainCommand;

    public MapJoinCommand(MainCommand mainCommand, CommandSender sender, String[] args) {
        this.mainCommand = mainCommand;

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command!");
        }
        Player p = (Player) sender;

        if (getMain().getStorageHandler().getEditingPlayers().containsKey(p.getUniqueId())) {
            return;
        }

        if (args.length < 2) {
            return;
        }

        if (!getMain().getStorageHandler().getLevels().containsKey(args[1])) {
            p.sendMessage("There is no lvl with this name");
            return;
        }

        LevelObject level = getMain().getStorageHandler().getLevels().get(args[1]);
        PlayerObject player = getMain().getStorageHandler().getPlayers().get(p.getUniqueId());

        player.setActualLevel(level);
        p.teleport(level.getStart());
        p.sendMessage("You have joined the level");

    }

    private LParkour getMain(){
        return mainCommand.getMain();
    }
}
