package org.hbrs.seka.uebung2.core.component.records;

import org.hbrs.seka.uebung2.core.runtime.RuntimeState;

public record ComponentStatus(
        String instanceId,
        String name,
        RuntimeState state
) {}