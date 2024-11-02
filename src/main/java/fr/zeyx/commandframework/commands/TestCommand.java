package fr.zeyx.commandframework.commands;

import fr.zeyx.commandframework.annotations.Command;
import fr.zeyx.commandframework.annotations.CommandParam;
import fr.zeyx.commandframework.annotations.DefaultCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value="test",
        description = "une description"
)
public class TestCommand {

    @DefaultCommand
    public void testCommand(CommandSender sender, @CommandParam(value = "player") String player) {
        sender.sendMessage(player + " à dit test !");
    }

}
