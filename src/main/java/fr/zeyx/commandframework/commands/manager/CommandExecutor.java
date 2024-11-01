package fr.zeyx.commandframework.commands.manager;

import fr.zeyx.commandframework.annotations.CommandParam;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    private final CommandRegistry registry;

    public CommandExecutor(CommandRegistry registry) {
        this.registry = registry;
    }

    public void executeCommand(String commandName, CommandSender sender, String[] args) {
        CommandData commandData = registry.getCommand(commandName);
        if (commandData == null) {
            // TODO: rendre ça configurable pour le dev
            sender.sendMessage("Unknown command: " + commandName);
            return;
        }

        CommandData currentCommandData = commandData;
        int i = 0;

        // on va chercher le commandData qui contient toutes les SubCommands (donc la feuille de l'arbre)
        while (i < args.length) {
            String subCommandName = args[i];
            CommandData subCommandData = currentCommandData.getSubCommandData(subCommandName);
            if (subCommandData == null) break;
            currentCommandData = subCommandData;
            i++;
        }

        System.out.println(currentCommandData.getSubCommandMethod("set"));

        if (i == args.length) {
            // si aucun argument restant (ou si pas d'arguments), exécute la méthode par défaut ou affiche le usage
            if (currentCommandData.getDefaultMethod() != null) {
                invokeMethod(currentCommandData.getDefaultMethod(), sender, args);
            } else {
                // TODO: rendre ça configurable pour le dev
                sender.sendMessage("Usage: " + currentCommandData.getCommandAnnotation().usage());
            }
        } else {
            String remainingSubCommand = args[i];
            Method subCommandMethod = currentCommandData.getSubCommandMethod(remainingSubCommand);
            if (subCommandMethod != null) {
                String[] remainingArgs = Arrays.copyOfRange(args, i + 1, args.length); // récup les paramètres restants
                invokeMethod(subCommandMethod, sender, remainingArgs);
            } else {
                invokeMethod(currentCommandData.getDefaultMethod(), sender, args);
            }
        }
    }

    private void invokeMethod(Method method, CommandSender sender, String... args) {
        try {
            List<Object> methodArgs = new ArrayList<>();
            methodArgs.add(sender);

            var params = method.getParameters();
            for (int i = 0; i < params.length - 1; i++) { // traitement des params
                CommandParam paramAnnotation = params[i + 1].getAnnotation(CommandParam.class);

                if (paramAnnotation != null) {
                    if (i < args.length) {
                        methodArgs.add(convertArg(args[i], params[i + 1].getType()));
                    } else if (paramAnnotation.optional()) {
                        methodArgs.add(null); // si optionnel est pas fournis, alors null
                    } else {
                        // TODO: rendre ça configurable pour le dev
                        sender.sendMessage("Missing parameters: " + paramAnnotation.value());
                        return;
                    }
                }
            }

            // TODO: Si sous classe pas static alors on le tej

            method.setAccessible(true);
            method.invoke(method.getDeclaringClass().getConstructor().newInstance(), methodArgs.toArray());
        } catch (Exception exception) {
            exception.printStackTrace();
            sender.sendMessage("An error occurred when executing this command.");
        }
    }

    // TODO: Rajouter une implémentation automatique pour Player et OfflinePlayer ?
    private Object convertArg(String arg, Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(arg);
        } else if (type == double.class || type == Double.class) {
            return Double.parseDouble(arg);
        }
        return arg;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = command.getName().toLowerCase();
        executeCommand(commandName, sender, args);
        return true;
    }
}
