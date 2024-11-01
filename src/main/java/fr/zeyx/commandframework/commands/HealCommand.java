package fr.zeyx.commandframework.commands;

import fr.zeyx.commandframework.annotations.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
    value = "heal",
    description = "Soigne un joueur",
    permission = "admin.heal",
    usage = "/heal [<joueur>]"
)
public class HealCommand {

    @DefaultCommand
    public void heal(CommandSender sender, @CommandParam(value = "player", optional = true) String playerName) {
        if(!(sender instanceof Player player)) return;
        if (playerName == null) {
            player.sendMessage("Vous avez été soigné !");
        } else {
            player.sendMessage("Le joueur " + playerName + " a été soigné !");
        }
    }

}

