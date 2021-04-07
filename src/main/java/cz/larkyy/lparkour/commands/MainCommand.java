package cz.larkyy.lparkour.commands;

import cz.larkyy.lparkour.LParkour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private final LParkour main;

    public MainCommand(LParkour main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "createmap":
                new MapCreateCommand(this,sender,args);
                break;
            case "join":
                new MapJoinCommand(this,sender,args);
                break;
            case "editmap":
                new MapEditCommand(this,sender,args);
                break;
            default:
                break;
        }

        return false;
    }

    public LParkour getMain() {
        return main;
    }
}
