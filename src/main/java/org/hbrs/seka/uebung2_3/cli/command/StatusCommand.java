package org.hbrs.seka.uebung2_3.cli.command;

import org.hbrs.seka.uebung2_3.core.component.records.ComponentDeploymentStatus;
import org.hbrs.seka.uebung2_3.core.component.records.ComponentStatus;
import org.hbrs.seka.uebung2_3.core.runtime.RuntimeManager;
import org.hbrs.seka.uebung2_3.core.runtime.RuntimeState;

public class StatusCommand implements Command {

    private final RuntimeManager runtime;

    public StatusCommand(RuntimeManager runtime) {
        this.runtime = runtime;
    }

    @Override
    public void execute() {
        System.out.println("Runtime: " + runtime.state());

        if (runtime.state().equals(RuntimeState.RUNNING)) {
            System.out.println();
            System.out.println("Aktive Instanzen:");
            for (ComponentStatus status : runtime.instanceStatuses()) {
                System.out.println(
                        status.instanceId() + " | " + status.name()
                );
            }

            System.out.println();
            System.out.println("Deployte Komponenten:");
            for (ComponentDeploymentStatus status : runtime.deploymentStatuses()) {
                System.out.println(
                        status.componentId() + " | " +
                                status.jarPath() + " | " +
                                status.state()
                );
            }
        }
    }

}