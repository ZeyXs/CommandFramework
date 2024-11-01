package fr.zeyx.commandframework.commands.manager;

import fr.zeyx.commandframework.annotations.Command;
import fr.zeyx.commandframework.annotations.DefaultCommand;
import fr.zeyx.commandframework.annotations.DefaultSubCommand;
import fr.zeyx.commandframework.annotations.SubCommand;
import fr.zeyx.commandframework.utils.PackageScanner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, CommandData> commands = new HashMap<>();

    public void registerCommandsInPackage(String packageName) {
        try {
            for (Class<?> commandClass : PackageScanner.getClassesInPackage(packageName)) {
                registerCommandClass(commandClass, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerCommandClass(Class<?> commandClass, CommandData parentData) {
        if (commandClass.isAnnotationPresent(Command.class) || commandClass.isAnnotationPresent(SubCommand.class)) {
            CommandData commandData;
            String commandName;

            if (commandClass.isAnnotationPresent(Command.class)) { // si c'est une classe @Command
                Command commandAnnotation = commandClass.getAnnotation(Command.class);
                commandName = commandAnnotation.value();
                commandData = new CommandData(commandAnnotation, commandClass);

                if (parentData == null) {
                    commands.put(commandName, commandData);
                } else {
                    // on rentre jamais dans ce cas
                    parentData.addSubCommandData(commandName, commandData);
                }
            } else { // si c'est une sous commande
                SubCommand subCommandAnnotation = commandClass.getAnnotation(SubCommand.class);
                commandName = subCommandAnnotation.value();
                commandData = new CommandData(null, commandClass);
                if (parentData != null) {
                    parentData.addSubCommandData(commandName, commandData);
                }
            }

            // enregistrement des méthodes de la classe pour les sous-commandes
            for (Method method : commandClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(DefaultCommand.class) || method.isAnnotationPresent(DefaultSubCommand.class)) {
                    commandData.setDefaultMethod(method);
                } else if (method.isAnnotationPresent(SubCommand.class)) {
                    SubCommand subCommand = method.getAnnotation(SubCommand.class);
                    commandData.addSubCommandMethod(subCommand.value(), method);
                }
            }

            // enregistrement récursif des sous-classes
            for (Class<?> innerClass : commandClass.getDeclaredClasses()) {
                if (innerClass.isAnnotationPresent(SubCommand.class)) {
                    registerCommandClass(innerClass, commandData);
                }
            }
        }
    }

    public CommandData getCommand(String name) {
        return commands.get(name);
    }
}
