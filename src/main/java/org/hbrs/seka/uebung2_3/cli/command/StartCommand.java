package org.hbrs.seka.uebung2_3.cli.command;

import org.hbrs.seka.uebung2_3.core.runtime.RuntimeManager;

public class StartCommand implements Command {

    private final RuntimeManager runtime;

    public StartCommand(RuntimeManager runtime) {
        this.runtime = runtime;
    }

    @Override
    public void execute() {
        runtime.start();
    }

}