package fr.zeyx.commandframework.commands.manager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DynamicCommand extends Command {

    private final CommandExecutor executor;

    protected DynamicCommand(String name, CommandExecutor executor) {
        super(name);
        this.executor = executor;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        executor.executeCommand(getName(), sender, args);
        return true;
    }

}
