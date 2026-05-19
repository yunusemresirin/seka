package org.hbrs.seka.uebung2_3.cli.command;

import org.hbrs.seka.uebung2_3.core.runtime.RuntimeManager;

public class DispatchCommand implements Command {

    private final RuntimeManager runtime;
    private final String componentId;

    public DispatchCommand(RuntimeManager runtime, String componentId) {
        this.runtime = runtime;
        this.componentId = componentId;
    }

    @Override
    public void execute() {
        runtime.dispatch(componentId);
    }

}