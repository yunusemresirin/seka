package org.hbrs.seka.uebung2_3.core.runtime;

import org.hbrs.seka.uebung2_3.core.component.ComponentManager;
import org.hbrs.seka.uebung2_3.core.component.records.ComponentDeploymentStatus;
import org.hbrs.seka.uebung2_3.core.component.records.ComponentStatus;

import java.nio.file.Path;
import java.util.List;

public class RuntimeManager {

    private final ComponentManager componentManager = new ComponentManager();
    private final RuntimePersistenceClient persistenceClient =
            new RuntimePersistenceClient(Path.of("runtime-snapshot.properties"));
    private volatile RuntimeState state = RuntimeState.STOPPED;

    public RuntimeManager(RuntimeSnapshot initialSnapshot) {
        restoreSnapshot(initialSnapshot);
    }

    public RuntimeState state() {
        return state;
    }

    public synchronized void start() {
        if (state == RuntimeState.RUNNING) {
            throw new IllegalStateException("Runtime läuft bereits.");
        }

        state = RuntimeState.RUNNING;
        persistSnapshot();
    }

    public synchronized void stop() {
        if (state != RuntimeState.RUNNING) {
            throw new IllegalStateException("Runtime läuft nicht.");
        }

        componentManager.stopAll();
        state = RuntimeState.STOPPED;
        persistSnapshot();
    }

    public void deploy(String componentId, Path jarPath) {
        ensureRunning();
        componentManager.deploy(componentId, jarPath);
        persistSnapshot();
    }

    public void delete(String componentId) {
        ensureRunning();
        componentManager.delete(componentId);
        persistSnapshot();
    }

    public void startComponent(String instanceId, String componentId) {
        ensureRunning();
        componentManager.start(instanceId, componentId);
        persistSnapshot();
    }

    public void stopComponent(String instanceId) {
        ensureRunning();
        componentManager.stop(instanceId);
        persistSnapshot();
    }

    public void dispatch(String componentId) {
        ensureRunning();
        componentManager.dispatch(componentId);
    }

    public List<ComponentStatus> instanceStatuses() {
        ensureRunning();
        return componentManager.statuses();
    }

    public List<ComponentDeploymentStatus> deploymentStatuses() {
        ensureRunning();
        return componentManager.deploymentStatuses();
    }

    private void restoreSnapshot(RuntimeSnapshot snapshot) {
        if (snapshot == null) {
            state = RuntimeState.STOPPED;
            return;
        }

        if (snapshot.getDeployedComponents() != null && snapshot.getRunningInstances() != null) {
            componentManager.restoreFromSnapshot(
                    snapshot.getDeployedComponents(),
                    snapshot.getRunningInstances()
            );
        }

        state = snapshot.getState() != null ? snapshot.getState() : RuntimeState.STOPPED;
    }

    private RuntimeSnapshot createSnapshot() {
        return new RuntimeSnapshot(
                state,
                componentManager.snapshotDeployedComponents(),
                componentManager.snapshotRunningInstances()
        );
    }

    private void persistSnapshot() {
        persistenceClient.saveSnapshot(createSnapshot());
    }

    private void ensureRunning() {
        if (state != RuntimeState.RUNNING) {
            throw new IllegalStateException("Runtime → " + state);
        }
    }
}