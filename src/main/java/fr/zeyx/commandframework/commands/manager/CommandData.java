package fr.zeyx.commandframework.commands.manager;

import fr.zeyx.commandframework.annotations.Command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CommandData {

    private final Command commandAnnotation;
    private final Class<?> commandClass;
    private Method defaultMethod;
    private final Map<String, Method> subCommandMethods = new HashMap<>();
    private final Map<String, CommandData> subCommandData = new HashMap<>();

    public CommandData(Command commandAnnotation, Class<?> commandClass) {
        this.commandAnnotation = commandAnnotation;
        this.commandClass = commandClass;
    }

    public void setDefaultMethod(Method method) {
        this.defaultMethod = method;
    }

    public void addSubCommandMethod(String name, Method method) {
        subCommandMethods.put(name, method);
    }

    public void addSubCommandData(String name, CommandData data) {
        subCommandData.put(name, data);
    }

    public CommandData getSubCommandData(String name) {
        return subCommandData.get(name);
    }

    public Method getDefaultMethod() {
        return defaultMethod;
    }

    public Method getSubCommandMethod(String name) {
        return subCommandMethods.get(name);
    }

    public Command getCommandAnnotation() {
        return commandAnnotation;
    }

    public Class<?> getCommandClass() {
        return commandClass;
    }

}
