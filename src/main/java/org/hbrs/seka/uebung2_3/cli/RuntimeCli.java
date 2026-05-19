package org.hbrs.seka.uebung2_3.cli;

import org.hbrs.seka.uebung2_3.cli.command.Command;
import org.hbrs.seka.uebung2_3.cli.command.CommandFactory;
import org.hbrs.seka.uebung2_3.core.runtime.RuntimeManager;
import org.hbrs.seka.uebung2_3.core.runtime.RuntimePersistenceClient;

import java.nio.file.Path;
import java.util.Scanner;

public class RuntimeCli {

    private static final RuntimePersistenceClient PERSISTENCE_CLIENT =
            new RuntimePersistenceClient(Path.of("runtime-snapshot.properties"));

    private static final RuntimeManager RUNTIME = new RuntimeManager(PERSISTENCE_CLIENT.loadSnapshot());

    private static final CommandFactory FACTORY = new CommandFactory(RUNTIME);

    /** Beispiel ↓

      start
      deploy demo C:\Users\Yunus\Projects\components\demo-component-1.0-SNAPSHOT.jar
      start-component demo-1 demo
      start-component demo-2 demo
      status
      dispatch demo
      dispatch demo
      stop-component demo-1
      stop-component demo-2
      delete demo
      stop
      exit

     */
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }

                String[] parts = line.split("\\s+");

                try {
//                  Command + Factory Pattern
                    Command command = FACTORY.create(parts);
                    command.execute(); // -> Invoker

//                  Command-Dispatcher-Struktur
//                    switch (parts[0]) {
//                        /* FA1 */  case "start" -> RUNTIME.start();
//                        /* FA2 */  case "stop" -> RUNTIME.stop();
//                        /* FA3 */  case "deploy" -> RUNTIME.deploy(parts[1], Path.of(parts[2]));
//                        /* FA4 - FA6 */ case "start-component" -> RUNTIME.startComponent(parts[1], parts[2]);
//                        /* FA7 */  case "status" -> printStatus();
//                        /* FA8 */  case "stop-component" -> RUNTIME.stopComponent(parts[1]);
//                        /* FA9 */  case "delete" -> RUNTIME.delete(parts[1]);
//                        /* TA01 */ case "dispatch" -> RUNTIME.dispatch(parts[1]);
//                        case "help" -> System.out.println("Commands: " +
//                                "start, " +
//                                "stop, " +
//                                "status, " +
//                                "deploy <componentId> <jar>, " +
//                                "delete <componentId>, " +
//                                "start-component <instanceId> <componentId>, " +
//                                "stop-component <instanceId>, exit"
//                            );
//                        default -> System.out.println("Unbekannter Befehl");
//                    }

                } catch (Exception e) {
                    System.out.println("Fehler: " + e.getMessage());
                }
            }
        }
    }

}