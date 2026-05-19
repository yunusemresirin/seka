package org.hbrs.seka.uebung2_3.cli.command;

import org.hbrs.seka.uebung2_3.core.runtime.RuntimeManager;

import java.nio.file.Path;

public class DeployCommand implements Command {

    private final RuntimeManager runtime;
    private final String componentId;
    private final Path jarPath;

    public DeployCommand(RuntimeManager runtime, String componentId, Path jarPath) {
        this.runtime = runtime;
        this.componentId = componentId;
        this.jarPath = jarPath;
    }

    @Override
    public void execute() {
        runtime.deploy(componentId, jarPath);
    }

}