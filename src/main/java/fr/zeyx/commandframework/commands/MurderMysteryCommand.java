package fr.zeyx.commandframework.commands;

import fr.zeyx.commandframework.annotations.*;
import org.bukkit.command.CommandSender;

@Command(
        value = "murder_mystery",
        aliases = {"mm", "murdermystery"},
        permission = "murder.admin",
        usage = "/murder_mystery <help|role>"
)
public class MurderMysteryCommand {

    @DefaultCommand
    public void defaultCommand(CommandSender sender) {
        sender.sendMessage("afficher l'aide");
    }

    @SubCommand("role")
    public static class RoleCommand {

        @SubCommand("set")
        public void setRole(
                CommandSender sender,
                @CommandParam(value = "player", optional = false) String player,
                @CommandParam(value = "role", optional = true) String role
        ) {
            sender.sendMessage("set " + player + " to " + role);
        }

        @DefaultSubCommand
        public void defaultRoleCommand(CommandSender sender) {
            sender.sendMessage("afficher l'aide pour la sous commande role");
        }
    }

    @SubCommand("help")
    public void helpMain(CommandSender sender) {
        sender.sendMessage("afficher l'aide générale mais avec help");
    }
}
