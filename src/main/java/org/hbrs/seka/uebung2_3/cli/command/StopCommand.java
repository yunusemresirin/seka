package org.hbrs.seka.uebung2_3.cli.command;

import org.hbrs.seka.uebung2_3.core.runtime.RuntimeManager;

public class StopCommand implements Command {

    private final RuntimeManager runtime;

    public StopCommand(RuntimeManager runtime) {
        this.runtime = runtime;
    }

    @Override
    public void execute() {
        runtime.stop();
    }
}