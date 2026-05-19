package org.hbrs.seka.uebung2_3.core.runtime;

import org.hbrs.seka.uebung2_3.core.component.records.ComponentDeploymentStatus;
import org.hbrs.seka.uebung2_3.core.component.records.ComponentStatus;

import java.nio.file.Path;
import java.util.List;

public interface RuntimeLifecycleState {

    RuntimeState state();

    void start(RuntimeManager runtime);

    void stop(RuntimeManager runtime);

    void deploy(RuntimeManager runtime, String componentId, Path jarPath);

    void delete(RuntimeManager runtime, String componentId);

    void startComponent(RuntimeManager runtime, String instanceId, String componentId);

    void stopComponent(RuntimeManager runtime, String instanceId);

    void dispatch(RuntimeManager runtime, String componentId);

    List<ComponentStatus> instanceStatuses(RuntimeManager runtime);

    List<ComponentDeploymentStatus> deploymentStatuses(RuntimeManager runtime);

}