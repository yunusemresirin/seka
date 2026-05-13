package org.hbrs.seka.uebung2.test;

import org.hbrs.seka.uebung2_3.core.component.ComponentRunner;
import org.hbrs.seka.uebung2_3.core.component.records.Component;
import org.hbrs.seka.uebung2_3.core.component.records.RunningComponent;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ComponentRunnerTest {

    private final ComponentRunner runner = new ComponentRunner();

    @Test
    void shouldLoadComponentFromJar() {
        Component component = new Component(Path.of("C:\\Users\\Yunus\\Projects\\components\\demo-component-1.0-SNAPSHOT.jar"));

        assertDoesNotThrow(() -> runner.validateComponent(component));
    }

    @Test
    void shouldLoadClassesWithSeparateClassLoader() {
        Component component = new Component(Path.of("C:\\Users\\Yunus\\Projects\\components\\demo-component-1.0-SNAPSHOT.jar"));

        RunningComponent runningComponent = runner.startComponent(component);

        assertNotNull(runningComponent.classLoader());
        assertNotSame(
                ComponentRunnerTest.class.getClassLoader(),
                runningComponent.classLoader()
        );

        assertEquals(
                runningComponent.classLoader(),
                runningComponent.instance().getClass().getClassLoader()
        );
    }

    @Test
    void shouldLoadDependentClassWithSameComponentClassLoader() {
        Component component = new Component(Path.of("C:\\Users\\Yunus\\Projects\\components\\demo-component-1.0-SNAPSHOT.jar"));

        RunningComponent runningComponent = runner.startComponent(component);

        ClassLoader componentClassLoader = runningComponent.instance().getClass().getClassLoader();

        /*
         * Wenn der Kunde meint, abhängige Klassen müssten manuell nachgeladen werden, dann ist das so nicht ganz korrekt formuliert:
         * Wenn die Klasse im Jar enthalten ist, lädt der Custom-ClassLoader sie bei Bedarf selbst.
         * Wenn die Klasse nicht im Jar enthalten ist, dann muss sie natürlich extern im Classpath vorhanden sein oder explizit verfügbar gemacht werden.
         */
        try {
            Class<?> regularClass = Class.forName(
                    "demo.RegularClass",
                    false,
                    componentClassLoader
            );

            assertEquals(componentClassLoader, regularClass.getClassLoader());
        } catch (ClassNotFoundException e) {
            fail(e);
        }
    }
}