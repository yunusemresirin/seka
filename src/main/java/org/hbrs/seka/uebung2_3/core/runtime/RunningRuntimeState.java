package org.hbrs.seka.uebung2_3.core.runtime;

import org.hbrs.seka.uebung2_3.core.component.records.ComponentDeploymentStatus;
import org.hbrs.seka.uebung2_3.core.component.records.ComponentStatus;

import java.nio.file.Path;
import java.util.List;

public class RunningRuntimeState implements RuntimeLifecycleState {

    @Override
    public RuntimeState state() {
        return RuntimeState.RUNNING;
    }

    @Override
    public void start(RuntimeManager runtime) {
        throw new IllegalStateException("Runtime läuft bereits.");
    }

    @Override
    public void stop(RuntimeManager runtime) {
        runtime.stopAllComponents();
        runtime.changeState(new StoppedRuntimeState());
        runtime.persistSnapshot();
    }

    @Override
    public void deploy(RuntimeManager runtime, String componentId, Path jarPath) {
        runtime.deployComponent(componentId, jarPath);
        runtime.persistSnapshot();
    }

    @Override
    public void delete(RuntimeManager runtime, String componentId) {
        runtime.deleteComponent(componentId);
        runtime.persistSnapshot();
    }

    @Override
    public void startComponent(RuntimeManager runtime, String instanceId, String componentId) {
        runtime.startRuntimeComponent(instanceId, componentId);
        runtime.persistSnapshot();
    }

    @Override
    public void stopComponent(RuntimeManager runtime, String instanceId) {
        runtime.stopRuntimeComponent(instanceId);
        runtime.persistSnapshot();
    }

    @Override
    public void dispatch(RuntimeManager runtime, String componentId) {
        runtime.dispatchToComponent(componentId);
    }

    @Override
    public List<ComponentStatus> instanceStatuses(RuntimeManager runtime) {
        return runtime.componentStatuses();
    }

    @Override
    public List<ComponentDeploymentStatus> deploymentStatuses(RuntimeManager runtime) {
        return runtime.componentDeploymentStatuses();
    }
}