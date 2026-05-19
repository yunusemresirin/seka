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

    private volatile RuntimeLifecycleState lifecycleState = new StoppedRuntimeState();

    public RuntimeManager(RuntimeSnapshot initialSnapshot) {
        restoreSnapshot(initialSnapshot);
    }

    public RuntimeState state() {
        return lifecycleState.state();
    }

    public synchronized void start() {
        lifecycleState.start(this);
    }

    public synchronized void stop() {
        lifecycleState.stop(this);
    }

    public void deploy(String componentId, Path jarPath) {
        lifecycleState.deploy(this, componentId, jarPath);
    }

    public void delete(String componentId) {
        lifecycleState.delete(this, componentId);
    }

    public void startComponent(String instanceId, String componentId) {
        lifecycleState.startComponent(this, instanceId, componentId);
    }

    public void stopComponent(String instanceId) {
        lifecycleState.stopComponent(this, instanceId);
    }

    public void dispatch(String componentId) {
        lifecycleState.dispatch(this, componentId);
    }

    public List<ComponentStatus> instanceStatuses() {
        return lifecycleState.instanceStatuses(this);
    }

    public List<ComponentDeploymentStatus> deploymentStatuses() {
        return lifecycleState.deploymentStatuses(this);
    }

    void changeState(RuntimeLifecycleState newState) {
        this.lifecycleState = newState;
    }

    void stopAllComponents() {
        componentManager.stopAll();
    }

    void deployComponent(String componentId, Path jarPath) {
        componentManager.deploy(componentId, jarPath);
    }

    void deleteComponent(String componentId) {
        componentManager.delete(componentId);
    }

    void startRuntimeComponent(String instanceId, String componentId) {
        componentManager.start(instanceId, componentId);
    }

    void stopRuntimeComponent(String instanceId) {
        componentManager.stop(instanceId);
    }

    void dispatchToComponent(String componentId) {
        componentManager.dispatch(componentId);
    }

    List<ComponentStatus> componentStatuses() {
        return componentManager.statuses();
    }

    List<ComponentDeploymentStatus> componentDeploymentStatuses() {
        return componentManager.deploymentStatuses();
    }

    void persistSnapshot() {
        persistenceClient.saveSnapshot(createSnapshot());
    }

    private void restoreSnapshot(RuntimeSnapshot snapshot) {
        if (snapshot == null) {
            lifecycleState = new StoppedRuntimeState();
            return;
        }

        if (snapshot.getDeployedComponents() != null && snapshot.getRunningInstances() != null) {
            componentManager.restoreFromSnapshot(
                    snapshot.getDeployedComponents(),
                    snapshot.getRunningInstances()
            );
        }

        lifecycleState = toLifecycleState(snapshot.getState());
    }

    private RuntimeLifecycleState toLifecycleState(RuntimeState state) {
        if (state == RuntimeState.RUNNING || state == RuntimeState.ACTIVE) {
            return new RunningRuntimeState();
        }

        return new StoppedRuntimeState();
    }

    private RuntimeSnapshot createSnapshot() {
        return new RuntimeSnapshot(
                lifecycleState.state(),
                componentManager.snapshotDeployedComponents(),
                componentManager.snapshotRunningInstances()
        );
    }
}