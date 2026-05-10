package org.hbrs.seka.uebung2.core.runtime;

import org.hbrs.seka.uebung2.core.component.ComponentManager;
import org.hbrs.seka.uebung2.core.component.records.ComponentStatus;

import java.nio.file.Path;
import java.util.List;

public class RuntimeManager {

    private final ComponentManager componentManager = new ComponentManager();
    private volatile RuntimeState state = RuntimeState.STOPPED;

    public synchronized void start() {
        if (state != RuntimeState.STOPPED) {
            throw new IllegalStateException("Runtime läuft bereits.");
        }

        state = RuntimeState.RUNNING;
    }

    public synchronized void stop() {
        if (state != RuntimeState.RUNNING) {
            throw new IllegalStateException("Runtime läuft nicht.");
        }

        componentManager.stopAll();
        state = RuntimeState.STOPPED;
    }

    public void deploy(String componentId, Path jarPath) {
        ensureRunning();
        componentManager.deploy(componentId, jarPath);
    }

    public void delete(String componentId) {
        ensureRunning();
        componentManager.delete(componentId);
    }

    public void startComponent(String instanceId, String componentId) {
        ensureRunning();
        componentManager.start(instanceId, componentId);
    }

    public void stopComponent(String instanceId) {
        ensureRunning();
        componentManager.stop(instanceId);
    }

    public String dispatch(String componentId) {
        ensureRunning();
        return componentManager.dispatch(componentId);
    }

    public List<ComponentStatus> instanceStatuses() {
        ensureRunning();
        return componentManager.statuses();
    }

    public RuntimeState state() {
        return state;
    }

    private void ensureRunning() {
        if (state != RuntimeState.RUNNING) {
            throw new IllegalStateException("Runtime → " + state);
        }
    }
}