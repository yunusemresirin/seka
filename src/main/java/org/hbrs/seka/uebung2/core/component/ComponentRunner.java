package org.hbrs.seka.uebung2;

import org.hbrs.seka.uebung2.annotations.Start;
import org.hbrs.seka.uebung2.annotations.Stop;
import org.hbrs.seka.uebung2.records.Component;
import org.hbrs.seka.uebung2.records.RunningComponent;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ComponentRunner {

    public RunningComponent startComponent(Component component) {
        try {
            // Übernommen von https://stackoverflow.com/questions/11016092/how-to-load-classes-at-runtime-from-a-folder-or-jar

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

                if (startMethod != null) {
                    Object instance = clazz.getDeclaredConstructor().newInstance();

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
                    "Keine Klasse mit einer @Start-Methode im JAR gefunden."
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