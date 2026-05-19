package org.hbrs.seka.uebung2_3.cli.command;

import org.hbrs.seka.uebung2_3.core.runtime.RuntimeManager;

import java.nio.file.Path;

public class CommandFactory {

    private final RuntimeManager runtime;

    public CommandFactory(RuntimeManager runtime) {
        this.runtime = runtime;
    }

    public Command create(String[] parts) {
        return switch (parts[0]) {
            case "start" -> new StartCommand(runtime);
            case "stop" -> new StopCommand(runtime);
            case "deploy" -> new DeployCommand(runtime, parts[1], Path.of(parts[2]));
            case "start-component" -> new StartComponentCommand(runtime, parts[1], parts[2]);
            case "status" -> new StatusCommand(runtime);
            case "stop-component" -> new StopComponentCommand(runtime, parts[1]);
            case "delete" -> new DeleteCommand(runtime, parts[1]);
            case "dispatch" -> new DispatchCommand(runtime, parts[1]);
            case "help" -> new HelpCommand();
            default -> throw new IllegalArgumentException("Unbekannter Befehl");
        };
    }

}