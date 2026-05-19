package org.hbrs.seka.uebung2_3.cli.command;

import org.hbrs.seka.uebung2_3.core.runtime.RuntimeManager;

public class StopComponentCommand implements Command {

    private final RuntimeManager runtime;
    private final String instanceId;

    public StopComponentCommand(RuntimeManager runtime, String instanceId) {
        this.runtime = runtime;
        this.instanceId = instanceId;
    }

    @Override
    public void execute() {
        runtime.stopComponent(instanceId);
    }

}