package org.hbrs.seka.uebung2_3.cli.command;

public class HelpCommand implements Command {

    @Override
    public void execute() {
        System.out.println("Commands: " +
                "start, " +
                "stop, " +
                "status, " +
                "deploy <componentId> <jar>, " +
                "delete <componentId>, " +
                "start-component <instanceId> <componentId>, " +
                "stop-component <instanceId>, " +
                "dispatch <componentId>, " +
                "help, exit");
    }

}