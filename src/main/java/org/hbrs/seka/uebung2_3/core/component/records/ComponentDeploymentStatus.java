package org.hbrs.seka.uebung2_3.core.component.records;

import org.hbrs.seka.uebung2_3.core.runtime.RuntimeState;

public record ComponentDeploymentStatus(
        String componentId,
        String jarPath,
        RuntimeState state
) {}