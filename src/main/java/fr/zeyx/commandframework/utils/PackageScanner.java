package fr.zeyx.commandframework.utils;

import fr.zeyx.commandframework.annotations.Command;
import org.reflections.Reflections;

import java.util.Set;

public class PackageScanner {

    public static Set<Class<?>> getClassesInPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(Command.class);
    }

}
