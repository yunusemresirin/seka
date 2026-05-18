package org.hbrs.seka.uebung2_3.core.component;

import org.hbrs.seka.uebung2_3.core.component.annotations.Inject;
import org.hbrs.seka.uebung2_3.core.component.annotations.Start;
import org.hbrs.seka.uebung2_3.core.component.annotations.Stop;
import org.hbrs.seka.uebung2_3.core.component.records.Component;
import org.hbrs.seka.uebung2_3.core.component.records.RunningComponent;
import org.hbrs.seka.uebung2_3.core.logging.ConsoleLogger;
import org.hbrs.seka.uebung2_3.core.logging.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ComponentRunner {

    private final Logger logger = new ConsoleLogger();

    public void validateComponent(Component component) {
        try {
            File jar = component.jarPath().toFile();

            if (!jar.exists() || !jar.isFile() || !jar.getName().endsWith(".jar")) {
                throw new IllegalArgumentException("Komponente muss ein gültiges .jar-File sein.");
            }

            int startingClassCount = 0;

            try (JarFile jarFile = new JarFile(jar);
                 URLClassLoader classLoader = URLClassLoader.newInstance(
                         new URL[]{new URL("jar:file:" + jar.getAbsolutePath() + "!/")},
                         ComponentRunner.class.getClassLoader()
                 )) {

                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();

                    if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                        continue;
                    }

                    String className = jarEntry.getName()
                            .substring(0, jarEntry.getName().length() - 6)
                            .replace('/', '.');

                    Class<?> clazz = classLoader.loadClass(className);

                    Method startMethod = findStartMethod(clazz);
                    Method stopMethod = findStopMethod(clazz);

                    if (startMethod != null || stopMethod != null) {
                        if (startMethod == null || stopMethod == null) {
                            throw new IllegalStateException(
                                    "Starting Class muss genau eine @Start- und eine @Stop-Methode besitzen: " + className
                            );
                        }

                        validateInjectFields(clazz, className);
                        startingClassCount++;
                    }
                }
            }

            if (startingClassCount != 1) {
                throw new IllegalStateException(
                        "Ein Komponenten-JAR muss genau eine Starting Class enthalten. Gefunden: " + startingClassCount
                );
            }

        } catch (Exception e) {
            throw new RuntimeException("Komponentenmodell ist ungültig.", e);
        }
    }

    public RunningComponent startComponent(Component component) {
        try {
            File jar = component.jarPath().toFile();

            JarFile jarFile = new JarFile(jar);

            URL[] urls = {
                    new URL("jar:file:" + jar.getAbsolutePath() + "!/")
            };

            URLClassLoader classLoader = URLClassLoader.newInstance(
                    urls,
                    ComponentRunner.class.getClassLoader()
            );

            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();

                if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                    continue;
                }

                String className = jarEntry.getName()
                        .substring(0, jarEntry.getName().length() - 6)
                        .replace('/', '.');

                Class<?> clazz = classLoader.loadClass(className);

                Method startMethod = findStartMethod(clazz);
                Method stopMethod = findStopMethod(clazz);

                if (startMethod != null && stopMethod != null) {
                    Object instance = clazz.getDeclaredConstructor().newInstance();

                    injectLogger(instance);

                    Thread thread = new Thread(() -> {
                        try {
                            startMethod.setAccessible(true);
                            startMethod.invoke(instance);
                        } catch (Exception e) {
                            throw new RuntimeException("Komponente konnte nicht gestartet werden.", e);
                        }
                    });

                    thread.start();

                    jarFile.close();

                    return new RunningComponent(
                            instance,
                            stopMethod,
                            classLoader,
                            thread
                    );
                }
            }

            jarFile.close();
            classLoader.close();

            throw new IllegalStateException(
                    "Keine gültige Starting Class mit @Start- und @Stop-Methode gefunden."
            );

        } catch (Exception e) {
            throw new RuntimeException("Komponente konnte nicht gestartet werden.", e);
        }
    }

    public void stopComponent(RunningComponent runningComponent) {
        try {
            Method stopMethod = runningComponent.stopMethod();

            if (stopMethod != null) {
                stopMethod.setAccessible(true);
                stopMethod.invoke(runningComponent.instance());
            }

            Thread thread = runningComponent.thread();

            if (thread != null && thread.isAlive()) {
                thread.interrupt();
            }

            runningComponent.classLoader().close();

        } catch (Exception e) {
            throw new RuntimeException("Komponente konnte nicht gestoppt werden.", e);
        }
    }

    private void validateInjectFields(Class<?> clazz, String className) {
        int injectFieldCount = 0;

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Inject.class)) {
                continue;
            }

            injectFieldCount++;

            if (!Logger.class.isAssignableFrom(field.getType())) {
                throw new IllegalStateException(
                        "Feld " + field.getName() +
                                " in " + className +
                                " ist mit @Inject annotiert, aber nicht vom Typ Logger."
                );
            }
        }

        if (injectFieldCount > 1) {
            throw new IllegalStateException(
                    "Starting Class darf höchstens ein @Inject-Feld für Logger enthalten: " + className
            );
        }
    }

    private void injectLogger(Object instance) {
        Class<?> currentClass = instance.getClass();

        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Inject.class)) {
                    continue;
                }

                if (!Logger.class.isAssignableFrom(field.getType())) {
                    throw new IllegalStateException(
                            "Feld " + field.getName() +
                                    " ist mit @Inject annotiert, aber nicht vom Typ Logger."
                    );
                }

                try {
                    field.setAccessible(true);
                    field.set(instance, logger);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(
                            "Logger konnte nicht in Feld " + field.getName() + " injiziert werden.",
                            e
                    );
                }
            }

            currentClass = currentClass.getSuperclass();
        }
    }

    private Method findStartMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Start.class)) {
                return method;
            }
        }

        return null;
    }

    private Method findStopMethod(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Stop.class)) {
                return method;
            }
        }

        return null;
    }

}