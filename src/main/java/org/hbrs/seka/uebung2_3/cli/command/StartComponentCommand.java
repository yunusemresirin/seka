package org.hbrs.seka.uebung2_3.cli.command;

import org.hbrs.seka.uebung2_3.core.runtime.RuntimeManager;

public class StartComponentCommand implements Command {

    private final RuntimeManager runtime;
    private final String instanceId;
    private final String componentId;

    public StartComponentCommand(RuntimeManager runtime, String instanceId, String componentId) {
        this.runtime = runtime;
        this.instanceId = instanceId;
        this.componentId = componentId;
    }

    @Override
    public void execute() {
        runtime.startComponent(instanceId, componentId);
    }

}