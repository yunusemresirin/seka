package org.hbrs.seka.uebung2_3.core.runtime;

import org.hbrs.seka.uebung2_3.core.component.records.ComponentDeploymentStatus;
import org.hbrs.seka.uebung2_3.core.component.records.ComponentStatus;

import java.nio.file.Path;
import java.util.List;

public class StoppedRuntimeState implements RuntimeLifecycleState {

    @Override
    public RuntimeState state() {
        return RuntimeState.STOPPED;
    }

    @Override
    public void start(RuntimeManager runtime) {
        runtime.changeState(new RunningRuntimeState());
        runtime.persistSnapshot();
    }

    @Override
    public void stop(RuntimeManager runtime) {
        throw new IllegalStateException("Runtime läuft nicht.");
    }

    @Override
    public void deploy(RuntimeManager runtime, String componentId, Path jarPath) {
        throw notRunning();
    }

    @Override
    public void delete(RuntimeManager runtime, String componentId) {
        throw notRunning();
    }

    @Override
    public void startComponent(RuntimeManager runtime, String instanceId, String componentId) {
        throw notRunning();
    }

    @Override
    public void stopComponent(RuntimeManager runtime, String instanceId) {
        throw notRunning();
    }

    @Override
    public void dispatch(RuntimeManager runtime, String componentId) {
        throw notRunning();
    }

    @Override
    public List<ComponentStatus> instanceStatuses(RuntimeManager runtime) {
        throw notRunning();
    }

    @Override
    public List<ComponentDeploymentStatus> deploymentStatuses(RuntimeManager runtime) {
        throw notRunning();
    }

    private IllegalStateException notRunning() {
        return new IllegalStateException("Runtime → " + RuntimeState.STOPPED);
    }
}