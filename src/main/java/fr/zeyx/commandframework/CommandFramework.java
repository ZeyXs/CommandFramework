package fr.zeyx.commandframework;

import fr.zeyx.commandframework.commands.manager.CommandExecutor;
import fr.zeyx.commandframework.commands.manager.CommandRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public final class CommandFramework extends JavaPlugin {

    private static CommandFramework instance;
    private CommandRegistry registry;

    @Override
    public void onEnable() {
        instance = this;

        registry = new CommandRegistry();
        registry.registerCommandsInPackage("fr.zeyx.commandframework.commands");

        getCommand("heal").setExecutor(new CommandExecutor(registry));
        getCommand("murder_mystery").setExecutor(new CommandExecutor(registry));
    }

    public static CommandFramework getInstance() {
        return instance;
    }

}