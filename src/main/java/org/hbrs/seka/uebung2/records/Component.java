package org.hbrs.seka.uebung2;

import org.hbrs.seka.uebung2.annotations.Start;
import org.hbrs.seka.uebung2.annotations.Stop;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public record Component(Path jarPath) {

    @Start
    public void start() {
        System.out.println("Component started");
        // Invoke Start-Method of Start-Class (Method with @Start-Annotation)

    }

    @Stop
    public void stop() {
        System.out.println("Component stopped");
    }

    @Override
    public @NotNull String toString() {
        return "Component{" +
                "jarPath=" + jarPath +
                '}';
    }
}
